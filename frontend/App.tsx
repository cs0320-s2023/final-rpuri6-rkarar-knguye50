import React, { useEffect, useState } from "react";
import "./style/App.css";
// import  InputIngredients from "./components/InputIngredients";
import Recipes from "./components/Recipes";



/**
 * App class that creates the MapBox element that displays
 * the map from react-map-gl.
 * @returns the App div html element
 */

function App() {

  return (
    <div className="App">

      <Recipes></Recipes>
      {/* <InputIngredients></InputIngredients> */}
      {/* <IngredientsMultiSelect></IngredientsMultiSelect> */}
      
    </div>
  );
}

export default App;
