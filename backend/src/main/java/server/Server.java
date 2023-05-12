package server;

import static spark.Spark.after;
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
      response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
      response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
    });
    Spark.get("Recipe", new IngredientsHandler());

    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}
