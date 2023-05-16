package server.csvapi.geoJSON;

import com.squareup.moshi.Json;
import java.util.List;

/**
 * GeoJSON class. Contains records of how to shape incoming JSON files from the downloaded geojson.
 */
public class IngredientRecord {

  /**
   * JsonType Record, should not be used except for error checking or converting to new JSON
   *
   * @param type - type field
   */
  public record RecipeIDList(List<RecipeFromIngredients> recipes) {}
  ;

  public record RecipeFromIngredients(
      @Json(name = "id") Integer id, @Json(name = "title") String title) {}

  public record Ingredient(@Json(name = "name") String name, @Json(name = "id") Integer id) {}

  public record Recipe(
      @Json(name = "title") String title,
      @Json(name = "id") Integer id,
      @Json(name = "image") String imageLink,
      @Json(name = "spoonacularSourceUrl") String recipeLink) {}
}
