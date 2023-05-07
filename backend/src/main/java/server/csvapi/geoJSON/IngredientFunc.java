package server.csvapi.geoJSON;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okio.Buffer;
public class IngredientFunc {
    private int responseCode = 0;
    private String responseBody = "";
    private String firstUrl = "";
    private String secondUrl = "";
    public IngredientFunc() {

    }
    public <T> T readJson(String url, Class<T> typeClass) throws IOException {
        //Creates a new connection and returns the serialized json obtained from the call
        URL requestUrl = new URL(url);
        HttpURLConnection clientConnection = (HttpURLConnection) requestUrl.openConnection();
        clientConnection.connect();
        T response = null;
        responseCode = clientConnection.getResponseCode();
        responseBody = clientConnection.getResponseMessage();
        System.out.println(responseCode);
        if (responseCode == 200) {
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<T> serializer = moshi.adapter(typeClass);
            response = serializer.fromJson(
                    new Buffer().readFrom(clientConnection.getInputStream()));
        }
        clientConnection.disconnect();
        System.out.println("READJSON SUCCESS");
        return response;
    }
    private ArrayList<Integer> getRecipeIDs(String firstURL) throws IOException {
        HashMap<String, Object> output = new HashMap<>();
        ArrayList<Integer> recipeIDs = new ArrayList<Integer>();
        System.out.println("GOT TO 42");
        System.out.println(firstURL);
        IngredientRecord.RecipeIDList firstList = readJson(firstURL,IngredientRecord.RecipeIDList.class);
        System.out.println(firstList.recipeIDList().size());
        return recipeIDs;
    }
    public HashMap<String, Object> startRequest(String ingredientsRaw)
            throws IOException {
        // Will move to private github folder later
        String key = "255004c1a7344062bc69069ed63ea0e8";
        HashMap<String, Object> output = new HashMap<>();
        // Converts the raw ingredient string from front end to an array
        String ingredients = ingredientsRaw.replace(",", ",+");
        //First call to get recipe list based on ingredients
        String requestUrl1 = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + ingredients + "&apiKey=" + key
                                + "&number=1";
        output.put("URL", requestUrl1);

        ArrayList<Integer> recipeIDs = getRecipeIDs(requestUrl1);
        System.out.println("GOT TO 59");
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

        output.put("recipe IDS", recipeIDs);

        return output;
    }
}
