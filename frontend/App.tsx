import React, { useEffect, useState } from "react";
import "./style/App.css";
import MapBox from "./components/MapBox";

/**
 * App class that creates the MapBox element that displays
 * the map from react-map-gl.
 * @returns the App div html element
 */

function App() {
  return (
    <div className="App">
      <MapBox></MapBox>
    </div>
  );
}

export default App;
