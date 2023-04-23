package server;

import static spark.Spark.after;

import server.csvapi.annotations.LoadAnnotationsHandler;
import server.csvapi.annotations.SaveAnnotationsHandler;
import server.csvapi.geoJSON.IngredientsHandler;
import spark.Spark;

/**
 * Server class that begins the server. Running main() will begin the server on localhost with port
 * 3232. Serves both the frontend map and bounding-box API.
 */
public class Server {

  /**
   * Main method that starts the server.
   *
   * @param args - terminal arguments, not used here
   */
  public static void main(String[] args) {
    Spark.port(3232);
    after((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "*");
    });
    Spark.get("ingredients", new IngredientsHandler());
    /*
     * Note: the frontend uses the GeoSearch handler using the following request
     * that is equivalent to loading in all possible coordinates.
     * http://localhost:3232/GeoSearch?minLat=-90&minLong=-180&maxLat=90&maxLong=180
     */
    SaveAnnotationsHandler saveAnnotationsHandler = new SaveAnnotationsHandler();
    Spark.get("saveAnnotations", saveAnnotationsHandler);
    Spark.get("load", new LoadAnnotationsHandler(saveAnnotationsHandler));
    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}
