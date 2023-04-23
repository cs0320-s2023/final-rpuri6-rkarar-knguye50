package server.csvapi.annotations;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.List;
import server.csvapi.annotations.AnnotationsClass.AnnotationInstance;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler for loading annotations stored in previous sessions on the frontend website (for User
 * Story 5, Stakeholder 2). Called by the web app when loading the page for the first time in a
 * session to retrieve all annotations stored on the server.
 */
public class LoadAnnotationsHandler implements Route {

  private final SaveAnnotationsHandler annotationSaver; // save handler has saved annotations.

  /**
   * Constructor. Ensures that the load handler is linked to the save handler that updates and
   * maintains the list of all annotations made.
   */
  public LoadAnnotationsHandler(SaveAnnotationsHandler annotationSaver) {
    this.annotationSaver = annotationSaver;
  }

  /**
   * Uses moshi to convert from a List of annotations to a JSON string. Allows the frontend to load
   * in all saved annotations from prior sessions.
   *
   * @param instances list of annotation instances to send
   * @return JSON string conversion
   */
  public static String toJson(List<AnnotationInstance> instances) {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(List.class, AnnotationInstance.class);
    JsonAdapter<List<AnnotationInstance>> adapter = moshi.adapter(type);
    return adapter.toJson(instances);
  }

  /**
   * Handle method to send all saved annotation instances to the frontend.
   *
   * @param request  - normally has arguments of request, none used here.
   * @param response - unused here
   * @return - JSON string, if no annotations exist yet, empty string
   */
  @Override
  public Object handle(Request request, Response response) {
    List<AnnotationInstance> annotations = this.annotationSaver.getAnnotations();
    // if no annotations have been saved yet, send back empty string.
    if (annotations == null) {
      return "";
    }
    return toJson(annotations);
  }


}
