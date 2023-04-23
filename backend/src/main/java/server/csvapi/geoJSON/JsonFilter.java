package server.csvapi.geoJSON;

import java.util.List;

public class JsonFilter {

  private final double min_lat;
  private final double min_long;

  private final double max_lat;
  private final double max_long;

  public JsonFilter(double min_lat, double min_long, double max_lat, double max_long) {
    this.min_lat = min_lat;
    this.min_long = min_long;
    this.max_lat = max_lat;
    this.max_long = max_long;
  }

  // Logic taken from https://github.com/cs0320-s2023/sprint-5-khu21-klee161/blob/master/backend/src/main/java/edu/brown/cs/student/main/API/BboxRequest.java
  public boolean contains(List<List<Double>> featureCoordinates) {
    for (List<Double> coordinate : featureCoordinates) {
      Double currLat = coordinate.get(0);
      Double currLong = coordinate.get(1);
      //if this coordinate is out of bounds, return false
      if (!(currLat > this.min_lat
          && currLat < this.max_lat && currLong > this.min_long && currLong < this.max_long)) {
        return false;
      }
    }
    //at this point all the points are within the bbox
    return true;
  }
}
