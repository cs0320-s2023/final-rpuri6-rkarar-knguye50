package server.recipeAPI.ingredientJSON;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okio.Buffer;

public class IngredientFunc {
  private int responseCode = 0;
  private String responseBody = "";

  public IngredientFunc() {}

  public <T> List<T> readListJson(String url, Class<T> typeClass) throws IOException {
    // Creates a new connection and returns the serialized json obtained from the call
    URL requestUrl = new URL(url);
    HttpURLConnection clientConnection = (HttpURLConnection) requestUrl.openConnection();
    clientConnection.connect();
    List<T> response = null;
    responseCode = clientConnection.getResponseCode();
    responseBody = clientConnection.getResponseMessage();
    if (responseCode == 200) {
      Moshi moshi = new Moshi.Builder().build();
      Type listClass = Types.newParameterizedType(List.class, typeClass);
      JsonAdapter<List<T>> serializer = moshi.adapter(listClass);
      try {
        response = serializer.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      } catch (IOException e) {
        clientConnection.disconnect();
        System.out.println("READJSON FAIL");
        System.out.println(e.getMessage());
        return response;
      }
    }
    clientConnection.disconnect();
    return response;
  }

  public <T> T readJson(String url, Class<T> typeClass) throws IOException {
    // Creates a new connection and returns the serialized json obtained from the call
    URL requestUrl = new URL(url);
    HttpURLConnection clientConnection = (HttpURLConnection) requestUrl.openConnection();
    clientConnection.connect();
    T response = null;
    responseCode = clientConnection.getResponseCode();
    responseBody = clientConnection.getResponseMessage();
    if (responseCode == 200) {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<T> serializer = moshi.adapter(typeClass);
      try {
        response = serializer.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      } catch (IOException e) {
        clientConnection.disconnect();
        System.out.println("READJSON FAIL");
        System.out.println(e.getMessage());
      }
    }
    clientConnection.disconnect();
    return response;
  }
  // Gets Recipe IDs based on ingredients given
  private ArrayList<Integer> getRecipeIDs(String firstURL) throws IOException {
    ArrayList<Integer> recipeIDs = new ArrayList<>();

    List<IngredientRecord.RecipeFromIngredients> firstList;
    try {
      firstList = readListJson(firstURL, IngredientRecord.RecipeFromIngredients.class);
    } catch (IOException e) {
      recipeIDs.add(0, -1);
      return recipeIDs;
    }
    if(responseCode != 200) {
      recipeIDs.add(0, -1);
      return recipeIDs;
    }
    for (int i = 0; i < firstList.size(); i++) {
      recipeIDs.add(firstList.get(i).id());
    }
    return recipeIDs;
  }
  // Makes 2nd API call to grab detailed recipe information given recipe IDs
  private ArrayList<IngredientRecord.Recipe> getRecipes(ArrayList<Integer> recipeIds)
      throws IOException {
    ArrayList<IngredientRecord.Recipe> recipes = new ArrayList<>();
    // Will move to private github folder later
    for (Integer recipeID : recipeIds) {
      String recipeURL =
          "https://api.spoonacular.com/recipes/"
              + recipeID.toString()
              + "/information?"
              + "&apiKey="
              + Constants.key;
      IngredientRecord.Recipe recipe = readJson(recipeURL, IngredientRecord.Recipe.class);
      recipes.add(recipe);
    }
    return recipes;
  }

  public HashMap<String, Object> startRequest(String ingredientsRaw) throws IOException {
    // Will move to private github folder later
    HashMap<String, Object> output = new HashMap<>();
    // Converts the raw ingredient string from front end to an array (Expected Format:
    // apple,milk,cereal)
    // Ingredients that are multiple words (tomato juice) will follow the format (tomato+juice)
    if (ingredientsRaw.isEmpty()) {
      output.put("error_message", "no ingredients received");
      return output;
    }
    final String ingredients = ingredientsRaw.replace(",", ",+");
    if (ingredients.isEmpty()) {
      output.put("error_message", "no ingredients recieved (after parse)");
      return output;
    }
    // First call to get recipe list based on ingredients
    String requestUrl1 =
        "https://api.spoonacular.com/recipes/findByIngredients?ingredients="
            + ingredients
            + "&apiKey="
            + Constants.key
            + "&number="
            + Constants.num_recipes;

    ArrayList<Integer> recipeIDs = getRecipeIDs(requestUrl1);
    if (responseCode != 200) {
      output.put("error_message", "Something went wrong with the API Call to findByIngredients");
      output.put("Api Call: ", requestUrl1);
      output.put("error code", responseCode);
      output.put("error message", responseBody);
      return output;
    }
    if (recipeIDs.size() == 0) {
      output.put("error_message", "No suitable recipes found (ingredient error)");
      return output;
    }
    if (recipeIDs.get(0) == -1) {
      output.put("error_message", "getrecipeID error caught, check console");
      return output;
    }


    ArrayList<IngredientRecord.Recipe> recipes = getRecipes(recipeIDs);
    // ERROR CHECK 2nd CALL HERE
    if (responseCode != 200) {
      output.put("error_message", "Something went wrong with the API Call to recipes/information");
      output.put("recipe list", recipes);
      output.put("error code", responseCode);
      output.put("error message", responseBody);
      return output;
    }
    if (recipes.size() == 0) {
      output.put("error_message", "No suitable recipes found");
      output.put("Ingredients Given", ingredients);
      return output;
    }
    if (recipes.size() > Constants.num_recipes) {
      output.put("error_message", "more recipes returned than allowed for");
      output.put("Ingredients Given", ingredients);
      output.put("recipe list", recipes);
      return output;
    }
    output.put("recipes", recipes);

    return output;
  }
}
