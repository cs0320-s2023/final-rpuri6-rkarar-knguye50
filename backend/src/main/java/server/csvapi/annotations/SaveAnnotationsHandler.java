package server.csvapi.annotations;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.csvapi.annotations.AnnotationsClass.AnnotationInstance;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Save annotations handler that updates and maintains all stored annotations from the frontend.
 */
public class SaveAnnotationsHandler implements Route {

  // instance variable of the annotations list.
  public List<AnnotationInstance> annotations;

  /**
   * Serialize function using moshi to convert a Map containing the result of the request, error
   * messages, etc. into a JSON string for the frontend.
   *
   * @param response - map to be converted
   * @return JSON string
   */
  public static String serialize(Map<String, Object> response) {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(type);
    return adapter.toJson(response);
  }

  /**
   * Handle method that serves to save the annotations from a frontend session in the server.
   *
   * @param request  - used to determine the contents of the annotations sent by the frontend.
   * @param response - unused
   * @return - JSON string response for frontend.
   */
  @Override
  public Object handle(Request request, Response response) {
    String annotationsSent = request.queryMap().value("Annotations");
    Map<String, Object> output = new HashMap<>();
    if (annotationsSent == null) {
      // if annotations sent are empty AND no annotations in server,
      // it is assumed that the user has cleared their annotations and is saving.
      if (this.annotations.size() != 0) {
        this.annotations.clear();
        output.put("result", "success");
        return serialize(output);
      }
      // otherwise, warning message to user is sent via frontend
      output.put("result", "error_bad_json");
      output.put("message", "no annotations to save");
      return serialize(output);
    }

    try {
      this.parse(annotationsSent);
    } catch (IOException e) {
      // if parse fails on annotations sent, an error message is sent to user via frontend
      output.put("result", "error_bad_json");
      output.put("message", "failed to parse through annotations");
      return serialize(output);
    }
    output.put("result", "success");
    return serialize(output);
  }

  /**
   * Parsing function uses Moshi to convert the frontend's sent annotations to to a list of
   * annotation instances to be saved as this.annotations.
   *
   * @param json - json string of annotation sent by frontend
   * @throws IOException - thrown by fromJson method
   */
  public void parse(String json) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(List.class, AnnotationInstance.class);
    try {
      JsonAdapter<List<AnnotationInstance>> adapter = moshi.adapter(type);
      this.annotations = adapter.fromJson(json);
      System.out.println(this.annotations);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Getter method used by LoadAnnotationsHandler to get the server's annotations.
   *
   * @return - the list of annotations
   */
  public List<AnnotationInstance> getAnnotations() {
    return this.annotations;
  }

}
