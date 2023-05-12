import { MultiSelect } from "@progress/kendo-react-dropdowns"; 
import '@progress/kendo-theme-default/dist/all.css'; 
import React from "react";
import "../style/ingredients.css"
// import { ingredients } from ... ?
import { useState } from "react";  
import { mockRecipes } from "../data/mockRecipes";
import { ScaleControl } from "react-map-gl";

var rList = []
var oFlag = 0
var ingredients: any[] = ["Carrots"]
const handleScrape = async() => {
  await fetch('http://127.0.0.1:5000/ingredients-cache', {method: 'GET'})
    .then(response => response.json())
    .then(function(data){
      if (oFlag != 1){
          for (let i=0;i<Object.values(data.result).flat().length;i++){
            ingredients.push(Object.values(data.result).flat()[i])
          }
          console.log(ingredients)
          oFlag = 1
          return ingredients
        }
      })
    .catch(error => console.log(error));
}

// let callback = (data) => {
//   console.log(Object.values(data.result))
//   rList = Object.values(data.result)
//   return rList
// };
// const ingredients = data.result
// var data;
// fetch("http://127.0.0.1:5000/ingredients", {method:'GET'}).then(function(response){
//   console.log("HERE")
//   console.log(JSON.parse(response.json))
//   return response.json}).then(function(data){console.log(data.g)})


// replace with imported list of scraped ingredients. how to make this list dynamic?
export const IngredientsMultiSelect = () => {  
  const [selectedIngredients, setSelectedIngredients] = useState([]);  
  const onChange = event => setSelectedIngredients([...event.value]); 
  const [recipes, setRecipes] = useState([]);
  const handleSubmit = async (selectedIngredients) => {
    // replace with back-end call to get the recipes
    console.log("trigger")
    const response = await fetch(`http://localhost:3232/Recipe?ingredients=${selectedIngredients}`);
    console.log("trigger t")
    const data = await response.json();
    console.log("HERE")
    console.log(data)
    setRecipes(data);
  };
  const [query, setQuery] = useState("")
 

 

  // source for multi-select: https://www.telerik.com/blogs/quick-guide-dropdown-menus-react 
  return (  
    <><form className="k-form k-my-8">
      <label className="k-label k-mb-3">Input your ingredients here</label>
      <div className="inline">
      <MultiSelect className="col" data={ingredients} value={selectedIngredients} onChange={onChange} autoClose={false} />
      <button className="submit" onClick={handleSubmit}>Find Recipes</button>
      <a className="scrape-a" onClick={handleScrape}>Scrape Recipes</a>
      </div>
    </form>

    <div>
    <input placeholder="Search Recipes" onChange={event => setQuery(event.target.value)} />
    </div>

  {
recipes.filter(recipe => {
  if (query === '') {
    return recipe;
  } else if (recipe.title.toLowerCase().includes(query.toLowerCase())) {
    return recipe;
  }
}).map((recipe, index) => (
  <div className="box" key={index}>
    <h2>{recipe.title}</h2>
          <img src={recipe.image} alt={recipe.title} />
  </div>
))
}
    </>

 
  );  
};  