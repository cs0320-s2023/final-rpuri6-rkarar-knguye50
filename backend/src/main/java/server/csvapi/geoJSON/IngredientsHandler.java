package server.csvapi.geoJSON;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.*;

import spark.Response;
import spark.Route;
/**
 * Load handler class handles API requests for load_file.
 */
public class IngredientsHandler implements Route {
  @Override
  public Object handle(spark.Request request, Response response) throws Exception {
    Map<String, Object> output = new HashMap<String, Object>();

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter serializer = moshi.adapter(Map.class);
    IngredientFunc ingredientFunc = new IngredientFunc();

    String ingredients = request.queryParams("ingredients");
    output = ingredientFunc.startRequest(ingredients);
    return serializer.toJson(output);
  }


}

