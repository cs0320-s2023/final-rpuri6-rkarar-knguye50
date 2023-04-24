import React, { useRef, useEffect, useState } from "react";
import Map, {
  ViewStateChangeEvent,
  MapLayerMouseEvent,
  Source,
  Layer,
  Popup,
  ScaleControl,
  PointLike,
  MapRef,
} from "react-map-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import token from "../private/token";
import { overlayData, geoLayer } from "./overlays";
import { Banner } from "./Banner";
import { AddAnno } from "./AddAnnoButton";
import { InputPrompt } from "./InputPrompt";
import { SaveMessage } from "./SaveMessage";

// interface for each annotation
export interface annotationInfo {
  latitude: number;
  longitude: number;
  annotation: string;
}

/**
 * MapBox function creates map box element for the frontend. Annotations are handled here
 * and essentially all visible parts of the front end are put together here.
 * @returns Mapbox element
 */
export default function MapBox() {
  const mapRef = useRef<MapRef>(null);
  const [contents, setContents] = useState("");
  const [showPopup, setShowPopup] = useState(false);
  const [showInputPrompt, setShowInputPrompt] = useState(false);
  const [annotationHistory, setAnnotationHistory] = useState<annotationInfo[]>(
    []
  );
  const [showConfirm, setShowConfirm] = useState(false);
  const [confirmMessage, setConfirmMessage] = useState("");
  const [clickData, setClickData] = useState<{ [key: string]: number }>({
    lon: 0,
    lat: 0,
  });
  const [overlay, setOverlay] = useState<GeoJSON.FeatureCollection | undefined>(
    undefined
  );
  const [viewState, setViewState] = useState({
    // starting coordinate point and zoom of frontend map ~ from gear-up
    longitude: 39.95233,
    latitude: -75.16379,
    zoom: 10,
  });

  useEffect(() => {
    // Fetch annotations from the backend only on start-up
    const fetchAnnotations = async () => {
      try {
        const response = await fetch("http://localhost:3232/load");
        const annotations = await response.json();
        setAnnotationHistory(annotations);
      } catch (error) {
        console.error("Failed to fetch annotations:", error);
      }
    };

    fetchAnnotations();
  }, []);

  /**
   * Handles mouse clicks on map; allows for navigation with mouse.
   * @param e MapLayerMouseEvent where features are received.
   */
  function onMapClick(e: MapLayerMouseEvent) {
    // from handout's explanation for the S-DIST requirement. Using here to get
    // import info on map loaction, coordinates for User Story 5
    const bbox: [PointLike, PointLike] = [
      [e.point.x, e.point.y],
      [e.point.x, e.point.y],
    ];
    // coordinate data where user clicked
    setClickData({
      latitude: e.lngLat.lat,
      longitude: e.lngLat.lng,
    });

    if (mapRef.current != null) {
      // using reference to the displayed mapbox
      // selected features established if query is on geo_data layer
      const features = mapRef.current.queryRenderedFeatures(bbox, {
        layers: ["geo_data"],
      });
      // if in geoJSON, show popup for annotations
      setShowPopup(features[0] !== undefined);
    }
  }
  useEffect(() => {
    // calls on overlay.ts methods to get geoJSON data from backend.
    overlayData().then((result) => {
      setOverlay(result);
    });
  }, []);

  //////////////////////////////////////
  //       Annotation Functions       //
  //////////////////////////////////////

  /**
   * Converts the current annotation history into the URL to be
   * used in the handleSave function when sending annotations to the backend.
   * @returns the url to be fetched
   */
  function convertAnnotations(): string {
    let history: annotationInfo[] = annotationHistory;
    if (history.length === 0) {
      return "http://localhost:3232/saveAnnotations";
    }
    let decoded: string = decodeURIComponent(JSON.stringify(history));
    let ret: string =
      "http://localhost:3232/saveAnnotations?Annotations=".concat(decoded);
    return decodeURI(ret); // this double-decode occured because of some extensive debugging
  }

  /**
   * Deletes an annotation from the annotation history. Triggers removal from screen.
   * @param item - annotation to be removed
   */
  function deleteAnno(item: annotationInfo) {
    const newHistory = annotationHistory.filter((element) => element !== item);
    setAnnotationHistory(newHistory);
  }

  /**
   * Initiates call to backend to save annotations for future sessions.
   */
  function handleSave() {
    let stringAnnotation = convertAnnotations();
    console.log(stringAnnotation);
    fetch(stringAnnotation)
      .then((response) => response.json())
      .then((responseJSON) => {
        setShowConfirm(true);
        if (responseJSON.result === "success") {
          setConfirmMessage("Annotations saved!");
        } else {
          setConfirmMessage(
            "No annotations to save. Make sure there are annotations to save!"
          );
        }
      });
  }

  return (
    <div id="mapContainer">
      <div className="info">
        <Banner viewState={viewState} />
        {showPopup && <AddAnno setShowInputPrompt={setShowInputPrompt} />}
      </div>
      <div className="annotationData">
        <button onClick={() => handleSave()}>Save Annotations</button>
      </div>
      {showInputPrompt && (
        <InputPrompt
          setShowInputPrompt={setShowInputPrompt}
          setContents={setContents}
          contents={contents}
          setAnnotationHistory={setAnnotationHistory}
          annotationHistory={annotationHistory}
          clickData={clickData}
        />
      )}
      {showConfirm && (
        <SaveMessage
          setShowConfirm={setShowConfirm}
          confirmMsg={confirmMessage}
        />
      )}
      {/* <Map
        ref={mapRef}
        onClick={onMapClick}
        longitude={viewState.longitude}
        latitude={viewState.latitude}
        zoom={viewState.zoom}
        onMove={(e: ViewStateChangeEvent) => setViewState(e.viewState)}
        mapboxAccessToken={token}
        style={{ width: window.innerWidth, height: window.innerHeight }}
        mapStyle={"mapbox://styles/mapbox/dark-v10"}
      >
        <ScaleControl />
        
        {annotationHistory.map((item) => (
          <Popup
            key={`${item.latitude}-${item.longitude}`}
            longitude={item.longitude}
            latitude={item.latitude}
            anchor="bottom"
            closeOnClick={false}
            onClose={() => deleteAnno(item)}
            focusAfterOpen={false}
          >
            {item.annotation}
          </Popup>
        ))}
        <Source id="geo_data" type="geojson" data={overlay}>
          <Layer {...geoLayer} />
        </Source>
      </Map> */}
    </div>
  );
}
