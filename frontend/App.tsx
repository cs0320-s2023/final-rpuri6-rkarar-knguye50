import React, { useEffect, useState } from "react";
import "./style/App.css";
import { IngredientsMultiSelect } from "./recipe app/InputIngredients";
import { Recipes } from "./recipe app/Recipes";


/**
 * App class that creates the MapBox element that displays
 * the map from react-map-gl.
 * @returns the App div html element
 */

function App() {
  return (
    <div className="App">
      <IngredientsMultiSelect></IngredientsMultiSelect>
      <Recipes></Recipes>
    </div>
  );
}

export default App;
