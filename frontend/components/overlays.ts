import { FeatureCollection } from "geojson";
import { FillLayer } from "react-map-gl";
/**
 * Gear-up function to check if a json object is a Feature Collection
 * @param json
 * @returns
 */
function isFeatureCollection(json: any): json is FeatureCollection {
  return json.type === "FeatureCollection";
}

/**
 * Overlays redlining regions on map based on what was returned by
 * server. By default, calls on bounding box of maximum size to include all
 * redlining data possible.
 * @returns
 */
export function overlayData(): Promise<GeoJSON.FeatureCollection | undefined> {
  return new Promise<GeoJSON.FeatureCollection | undefined>(
    (resolve, reject) => {
      const url: string =
        "http://localhost:3232/GeoSearch?minLat=-90&minLong=-180&maxLat=90&maxLong=180";
      fetch(url)
        .then((result) => result.json())
        .then((json) => {
          resolve(isFeatureCollection(json.data) ? json.data : undefined);
        })
        .catch(() => {
          resolve(undefined);
        });
    }
  );
}

/**
 * Gear-up provided redlining overlay.
 */
const propertyName = "holc_grade";
export const geoLayer: FillLayer = {
  id: "geo_data",
  type: "fill",
  paint: {
    "fill-color": [
      "match",
      ["get", propertyName],
      "A",
      "#5bcc04",
      "B",
      "#04b8cc",
      "C",
      "#e9ed0e",
      "D",
      "#d11d1d",
      "#ccc",
    ],
    "fill-opacity": 0.2,
  },
};
