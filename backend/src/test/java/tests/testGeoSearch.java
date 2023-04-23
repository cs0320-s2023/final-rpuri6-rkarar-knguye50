package tests;

import com.squareup.moshi.Moshi;
import okio.Buffer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import server.csvapi.geoJSON.GeoJSONRecord;
import server.csvapi.geoJSON.GeoSearch;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class testGeoSearch {
    @BeforeAll
    public static void setup_before_everything() {
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    @AfterEach
    public void teardown() {
        // Gracefully stop Spark listening on both endpoints
        Spark.unmap("GeoSearch");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    static private HttpURLConnection apiCall(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL = new URL("http://localhost:"+Spark.port()+"/"+apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        clientConnection.connect();
        return clientConnection;
    }
    // Helper function that determines if every coordinate in the GeoJSON given is within the min/max lat/long
    private boolean isCorrectlyBounded(GeoJSONRecord.GeoJSON featureJSON, double min_lat, double min_long, double max_lat, double max_long) {
        List<GeoJSONRecord.Feature> featuresList = featureJSON.features();
        for(GeoJSONRecord.Feature feature: featuresList) {
            List<Double> coordinate = feature.geometry().coords().get(0).get(0).get(0);
            double currLat = coordinate.get(0);
            double currLong = coordinate.get(1);
            if(!(currLat > min_lat && currLat < max_lat && currLong > min_long && currLong < max_long)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testErrors() throws IOException {
        Spark.get("GeoSearch", new GeoSearch());
        Spark.init();
        Spark.awaitInitialization();
        System.out.println("Server started.");
        HttpURLConnection clientConnection = apiCall("GeoSearch?minLat=30");
        Assert.assertEquals(200, clientConnection.getResponseCode());
        Moshi moshi = new Moshi.Builder().build();
        Map<String, Object> response =
                moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

        Assert.assertNotNull(response);
        Assert.assertEquals("incorrect number of arguments", response.get("error_message"));

        clientConnection = apiCall("GeoSearch?minLat=-90&minLong=-180&maxLat=90&maxLong=-181");
        Assert.assertEquals(200, clientConnection.getResponseCode());
        response =
                moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        Assert.assertNotNull(response);
        Assert.assertEquals("min/max lat/long out of bounds", response.get("error_message"));

        clientConnection = apiCall("GeoSearch?minLat=-90&minLong=-180&maxLat=90&maxLong=string");
        Assert.assertEquals(200, clientConnection.getResponseCode());
        response =
                moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        Assert.assertNotNull(response);
        Assert.assertEquals("lat and long must be Doubles", response.get("error_message"));
    }
    @Test
    public void testFunctionality() throws IOException {
        Spark.get("GeoSearch", new GeoSearch());
        Spark.init();
        Spark.awaitInitialization();
        System.out.println("Server started.");
        // Case for random bound
        HttpURLConnection clientConnection = apiCall("GeoSearch?minLat=-90&minLong=-180&maxLat=-86.758669&maxLong=33.509329");
        Assert.assertEquals(200, clientConnection.getResponseCode());
        Moshi moshi = new Moshi.Builder().build();
        Map<String, Object> response =
                moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

        GeoJSONRecord.GeoJSON featureJSON =  moshi.adapter(GeoJSONRecord.GeoJSON.class).fromJsonValue(response.get("data"));
        Assert.assertTrue(isCorrectlyBounded(featureJSON, -90, -180,86.758669,33.509329));
        Assert.assertTrue(featureJSON.features().size() == 56);
        // Case for nothing in the bounds
        clientConnection = apiCall("GeoSearch?minLat=0&minLong=0&maxLat=0&maxLong=0");
        Assert.assertEquals(200, clientConnection.getResponseCode());
        moshi = new Moshi.Builder().build();
        response =
                moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

        featureJSON =  moshi.adapter(GeoJSONRecord.GeoJSON.class).fromJsonValue(response.get("data"));
        Assert.assertTrue(isCorrectlyBounded(featureJSON, 0, 0,0,0));
        Assert.assertTrue(featureJSON.features().size() == 0);
        // Case for the whole map
        clientConnection = apiCall("GeoSearch?minLat=-90&minLong=-180&maxLat=90&maxLong=180");
        Assert.assertEquals(200, clientConnection.getResponseCode());
        moshi = new Moshi.Builder().build();
        response =
                moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

        featureJSON =  moshi.adapter(GeoJSONRecord.GeoJSON.class).fromJsonValue(response.get("data"));
        Assert.assertTrue(isCorrectlyBounded(featureJSON, 0, 0,0,0));
        Assert.assertTrue(featureJSON.features().size() == 6256);

    }
}