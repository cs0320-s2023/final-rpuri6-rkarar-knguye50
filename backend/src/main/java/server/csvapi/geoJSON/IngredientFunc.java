package server.csvapi.geoJSON;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.squareup.moshi.Types;
import okio.Buffer;
public class IngredientFunc {
    private int responseCode = 0;
    private String responseBody = "";
    //Number of recipes displayed per api call, default to 5
    private int numRecipes = 5;
    //api key
    private String key = "255004c1a7344062bc69069ed63ea0e8";
    public IngredientFunc() {

    }
    public <T> List<T> readListJson(String url, Class<T> typeClass) throws IOException {
        //Creates a new connection and returns the serialized json obtained from the call
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
                response = serializer.fromJson(
                        new Buffer().readFrom(clientConnection.getInputStream()));
                System.out.println(response.toString());
            } catch (IOException e) {
                clientConnection.disconnect();
                System.out.println("READJSON FAIL");
                System.out.println(e.getMessage());
            }
        }
        clientConnection.disconnect();
        return response;
    }
    public <T> T readJson(String url, Class<T> typeClass) throws IOException {
        //Creates a new connection and returns the serialized json obtained from the call
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
                response = serializer.fromJson(
                        new Buffer().readFrom(clientConnection.getInputStream()));
            } catch (IOException e) {
                clientConnection.disconnect();
                System.out.println("READJSON FAIL");
                System.out.println(e.getMessage());
            }
        }
        clientConnection.disconnect();
        return response;
    }
    private ArrayList<Integer> getRecipeIDs(String firstURL) throws IOException {
        ArrayList<Integer> recipeIDs = new ArrayList<>();

        List<IngredientRecord.RecipeFromIngredients> firstList;
        try {
            firstList = readListJson(firstURL,IngredientRecord.RecipeFromIngredients.class);
        } catch (IOException e) {
            recipeIDs.add(0, -1);
            System.out.println(e.getMessage());
            return recipeIDs;
        }
        for(int i = 0; i < firstList.size(); i++) {
            recipeIDs.add(firstList.get(i).id());
        }
        System.out.println(recipeIDs);
        return recipeIDs;
    }

    private ArrayList<IngredientRecord.Recipe> getRecipes(ArrayList<Integer> recipeIds) throws IOException {
        ArrayList<IngredientRecord.Recipe> recipes = new ArrayList<>();
        // Will move to private github folder later
        for (Integer recipeID : recipeIds) {
            String recipeURL =  "https://api.spoonacular.com/recipes/" + recipeID.toString() +"/information?" + "&apiKey=" + key;
            System.out.println(recipeURL);
            IngredientRecord.Recipe recipe = readJson(recipeURL, IngredientRecord.Recipe.class);
            recipes.add(recipe);

        }
        return recipes;
    }
    public HashMap<String, Object> startRequest(String ingredientsRaw)
            throws IOException {
        // Will move to private github folder later
        HashMap<String, Object> output = new HashMap<>();
        // Converts the raw ingredient string from front end to an array (Expected Format: apple,milk,cereal)
        String ingredients = ingredientsRaw.replace(",", ",+");
        //First call to get recipe list based on ingredients
        String requestUrl1 = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + ingredients + "&apiKey=" + key
                                + "&number=" + numRecipes;

        ArrayList<Integer> recipeIDs = getRecipeIDs(requestUrl1);

        if(recipeIDs.get(0) == -1) {
            output.put("error", "getrecipeID error caught");
            return output;
        }
        if (responseCode != 200) {
            output.put("Error", "Something went wrong with the API Call to findByIngredients");
            output.put("Api Call: ", requestUrl1);
            output.put("error code", responseCode);
            output.put("error message", responseBody);
            return output;
        }
        if (recipeIDs.size() == 0 ) {
            output.put("Error", "No suitable recipes found");
            return output;
        }
        ArrayList<IngredientRecord.Recipe> recipes = getRecipes(recipeIDs);
        //ERROR CHECK 2nd CALL HERE
        output.put("recipes", recipes);

        return output;
    }
}
