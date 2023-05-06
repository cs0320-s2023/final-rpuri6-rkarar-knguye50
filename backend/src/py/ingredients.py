import requests
import json

# Hard code ingredients for now
ingredients = ["Carrot", "Milk", "Dough"]

key = "25e806a1fad64f40b6b36ae590b8a5f2"
spoonurl = "https://api.spoonacular.com/recipes/complexSearch?apiKey=" + key + "&number=5" + "&query="

# Gets rList, a list of recipe dictionary objects for query
for i in ingredients:
    rObject = requests.get(spoonurl+i).json()
    rList = rObject["results"]
