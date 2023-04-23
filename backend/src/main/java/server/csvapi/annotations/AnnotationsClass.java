package server.csvapi.annotations;


/**
 * Class that wraps the AnnotationInstance record.
 */
public class AnnotationsClass {

  /**
   * Record of an instance of an annotation. Stores coordinates and annotation for each annotation.
   *
   * @param latitude   the latitude the annotation was made
   * @param longitude  the longitude the annotation was made
   * @param annotation the annotation's string
   */
  public record AnnotationInstance(Double latitude, Double longitude, String annotation) {

  }
}
