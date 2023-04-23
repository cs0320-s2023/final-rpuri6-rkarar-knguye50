package server.csvapi.geoJSON;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Load handler class handles API requests for load_file.
 */
public class GeoSearch implements Route {

  /**
   * Handles the request sent from server.
   *
   * @param request  - contains user parameters for loading file
   * @param response - not used, originally from handle() function signature
   * @return JSON string for output
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    // Converts file to string for moshi
    String redliningData = "";
    try {
      Reader reader = new FileReader("backend/src/data/fullDownload.json", StandardCharsets.UTF_8);
      BufferedReader bf = new BufferedReader(reader);
      redliningData = bf.readLine();
    } catch (IOException e) {
      try {
        Reader reader = new FileReader("src\\data\\fullDownload.json", StandardCharsets.UTF_8);
        BufferedReader bf = new BufferedReader(reader);
        redliningData = bf.readLine();
      } catch (IOException error) {
        System.err.println("Could Not read GEO JSON Data.");
      }
    }
    Map<String, Object> output = new HashMap<>();

    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> serializer = moshi.adapter(type);

    String min_lat = request.queryParams("minLat");
    String min_long = request.queryParams("minLong");
    String max_lat = request.queryParams("maxLat");
    String max_long = request.queryParams("maxLong");

    // if there are an incorrect number of parameters, throw an error
    if (!(request.queryParams().size() == 4)) {
      output.put("result", "error_bad_request");
      output.put("error_message", "incorrect number of arguments");
      output.put("minLat", min_lat);
      output.put("minLong", min_long);
      output.put("maxLat", max_lat);
      output.put("maxLong", max_long);
      return serializer.toJson(output);
    }
    Double minLatDouble, minLongDouble, maxLatDouble, maxLongDouble;

    try {
      minLatDouble = Double.parseDouble(min_lat);
      minLongDouble = Double.parseDouble(min_long);
      maxLatDouble = Double.parseDouble(max_lat);
      maxLongDouble = Double.parseDouble(max_long);
    } catch (NumberFormatException e) {
      // if lat or long are not doubles, throw an error
      output.put("result", "error_bad_request");
      output.put("error_message", "lat and long must be Doubles");
      output.put("minLat", min_lat);
      output.put("minLong", min_long);
      output.put("maxLat", max_lat);
      output.put("maxLong", max_long);
      return serializer.toJson(output);
    }
    if (minLatDouble > 90 || minLongDouble > 180 || maxLatDouble < -90 || maxLongDouble < -180) {
      output.put("result", "error_bad_request");
      output.put("error_message", "min/max lat/long out of bounds");
      return serializer.toJson(output);
    }
    // Filters based on lat/long given
    GeoJSONRecord.GeoJSON featureJSON = moshi.adapter(GeoJSONRecord.GeoJSON.class)
        .fromJson(redliningData);
    List<GeoJSONRecord.Feature> featuresList = featureJSON.features();

    List<GeoJSONRecord.Feature> filteredFeatures = new ArrayList<>();
    try {
      JsonFilter FilterRequest =
          new JsonFilter(
              minLatDouble, minLongDouble,
              maxLatDouble, maxLongDouble);

      for (GeoJSONRecord.Feature feature : featuresList) {
        if (feature.geometry() != null) {
          if (FilterRequest.contains(feature.geometry().coords().get(0).get(0))) {
            filteredFeatures.add(feature);
          }
        }
      }
      GeoJSONRecord.GeoJSON geoJSONData = new GeoJSONRecord.GeoJSON("FeatureCollection",
          filteredFeatures);
      output.put("result", "success");
      output.put("data", geoJSONData);
      return serializer.toJson(output);
    } catch (Exception e) {
      output.put("error_datasource", e.getMessage());
      return serializer.toJson(output);
    }

  }

}
