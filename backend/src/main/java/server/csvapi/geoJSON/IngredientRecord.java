package server.csvapi.geoJSON;

import com.squareup.moshi.Json;
import java.util.List;
import java.util.Map;

/**
 * GeoJSON class. Contains records of how to shape incoming JSON files from the downloaded geojson.
 */
public class IngredientRecord {

  /**
   * JsonType Record, should not be used except for error checking or converting to new JSON
   *
   * @param type - type field
   */
  public record Recipe(@Json(name = "title") String title, @Json(name = "usedIngredients") List<Ingredient> usedIngredients) {

  }
  public record Ingredient() {

  }
}