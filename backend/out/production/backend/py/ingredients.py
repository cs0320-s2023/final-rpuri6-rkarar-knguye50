import string
import requests
import json
import re
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer


# Hard code ingredients to generate dummy docs
ingredients = ["Carrot", "Milk", "Dough"]

key = "25e806a1fad64f40b6b36ae590b8a5f2"
spoonurl = "https://api.spoonacular.com/recipes/complexSearch?apiKey=" + key + "&number=5" + "&addRecipeInformation=true" + "&query="

documents = []
rList = []

# Gets rList, a list of recipe dictionary objects for query
# Random function of just getting corpus for search
for i in ingredients:
    rObject = requests.get(spoonurl+i).json()
    rList.append(rObject["results"])
    for recipe in rObject["results"]:
        documents.append(recipe["summary"])

#Clean the corpus
documents_clean = []
for document in documents:
    # Remove Unicode
    document_test = re.sub(r'[^\x00-\x7F]+', ' ', document)
    # Remove Mentions
    document_test = re.sub(r'@\w+', '', document_test)
    # Lowercase the document
    document_test = document_test.lower()
    # Remove punctuations
    document_test = re.sub(r'[%s]' % re.escape(string.punctuation), ' ', document_test)
    # Lowercase the numbers
    document_test = re.sub(r'[0-9]', '', document_test)
    # Remove the doubled space
    document_test = re.sub(r'\s{2,}', ' ', document_test)
    documents_clean.append(document_test)
    # print("----")
    # print(document)

vectorizer = TfidfVectorizer()

X = vectorizer.fit_transform(documents_clean)
X = X.T.toarray()

df = pd.DataFrame(X, index=vectorizer.get_feature_names())

def search_query(query, dataframe):
  query = [query]
  q_vec = vectorizer.transform(query).toarray().reshape(dataframe.shape[0],)
  sim = {}
  # Calculate the similarity
  for i in range(len(documents_clean)):
    sim[i] = np.dot(dataframe.loc[:, i].values, q_vec) / np.linalg.norm(dataframe.loc[:, i]) * np.linalg.norm(q_vec)
  
  # Sort the values 
  sim_sorted = sorted(sim.items(), key=lambda x: x[1], reverse=True)
  # Print the articles and their similarity values

  print(sim_sorted)
  for k, v in sim_sorted:
    if v != 0.0:
      print("SS:", v)
      print(documents_clean[k])
      print()

    
# Add The Query
q1 = 'carrot'
# Call the function
search_query(q1, df)
