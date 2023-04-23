package server.csvapi.geoJSON;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.Response;
import spark.Route;

/**
 * Load handler class handles API requests for load_file.
 */
public class IngredientsHandler implements Route {
  @Override
  //http://localhost:3232/weather?lat=41.8268&long=-71.4029
  public Object handle(spark.Request request, Response response) throws Exception {
    Map<String, Object> output = new HashMap<String, Object>();

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter serializer = moshi.adapter(Map.class);

    String ingredients = request.queryParams("ingredients");
    output.put("ingredients", ingredients);
    return serializer.toJson(output);
  }
  public HashMap<String, Object> startRequest(String ingredientsRaw)
          throws IOException {
    // Will move to private github folder later
    String key = "255004c1a7344062bc69069ed63ea0e8";
    HashMap<String, Object> output = new HashMap<>();
    List<String> ingredients = Arrays.asList(ingredientsRaw.split("\\s*,\\s*"));

    String requestUrl = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + ingredientsRaw;

    return output;
  }
}
