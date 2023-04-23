package server.csvapi.geoJSON;

import com.squareup.moshi.Json;
import java.util.List;
import java.util.Map;

/**
 * GeoJSON class. Contains records of how to shape incoming JSON files from the downloaded geojson.
 */
public class GeoJSONRecord {

  /**
   * JsonType Record, should not be used except for error checking or converting to new JSON
   *
   * @param type - type field
   */
  public record GeoJSON(@Json(name = "type") String type, @Json(name = "features")List<Feature> features) {

  }

    /**
   * Feature Record. Shapes a nested portion of the JSON
   *
   * @param type     - type field
   * @param geometry - geometry field
   */
  public record Feature(
      @Json(name = "type") String type,
      @Json(name = "geometry") Geometry geometry,
      @Json(name = "properties") FeatureProperties properties) {

  }

    /**
   * Geometry Record. Shapes a nested portion of the JSON
   *
   * @param type        - type field
   * @param coordinates - coordinates field
   */
  public record Geometry(
      @Json(name = "type") String type,
      @Json(name = "coordinates") List<List<List<List<Double>>>> coords) {

  }

    public record FeatureProperties(
      @Json(name = "state") String state,
      @Json(name = "city") String city,
      @Json(name = "name") String name,
      @Json(name = "holc_id") String holc_id,
      @Json(name = "holc_grade") String holc_grade,
      @Json(name = "neighborhood_id") Double neighborhood_id,
      @Json(name = "area_description_data") Map<String, String> description
  ) {

  }

}