import com.squareup.moshi.Moshi;
import okio.Buffer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import server.recipeAPI.ingredientJSON.IngredientsHandler;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class testIngredients {
    @BeforeAll
    public static void setup_before_everything() {
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    @AfterEach
    public void teardown() {
        // Gracefully stop Spark listening on both endpoints
        Spark.unmap("Recipe");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    static private HttpURLConnection apiCall(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL = new URL("http://localhost:"+Spark.port()+"/"+apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        clientConnection.connect();
        return clientConnection;
    }

    @Test
    public void testErrors() throws IOException {
        Spark.get("Recipe", new IngredientsHandler());
        Spark.init();
        Spark.awaitInitialization();
        System.out.println("Server started.");
        HttpURLConnection clientConnection = apiCall("Recipe?ingredients=");
        Assert.assertEquals(200, clientConnection.getResponseCode());
        Moshi moshi = new Moshi.Builder().build();
        Map<String, Object> response =
                moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

        Assert.assertNotNull(response);
        Assert.assertEquals("no ingredients received", response.get("error_message"));
    }

}