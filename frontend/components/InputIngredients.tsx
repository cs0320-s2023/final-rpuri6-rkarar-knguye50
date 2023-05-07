import { MultiSelect } from "@progress/kendo-react-dropdowns"; 
import '@progress/kendo-theme-default/dist/all.css'; 
import React from "react";
// import { ingredients } from ... ?
import { useState } from "react";  
import { mockRecipes } from "../data/mockRecipes";


// replace with imported list of scraped ingredients. how to make this list dynamic?
const ingredients = [  
  "Flour",
  "Sugar", 
  "Milk"
];  
  
export const IngredientsMultiSelect = () => {  
  const [selectedIngredients, setSelectedIngredients] = useState([]);  
  const onChange = event => setSelectedIngredients([...event.value]); 
  const [recipes, setRecipes] = useState([]);
  const handleSubmit = async (selectedIngredients) => {
    // replace with back-end call to get the recipes
    const response = await fetch(`http://localhost:3232/recipes?ingredients=${selectedIngredients}`);
    const data = await response.json();
    setRecipes(data);
  };
 

  // source for multi-select: https://www.telerik.com/blogs/quick-guide-dropdown-menus-react 
  return (  
    <><form className="k-form k-my-8">
      <label className="k-label k-mb-3">Input your ingredients here</label>
      <div className="inline">
      <MultiSelect className="col" data={ingredients} value={selectedIngredients} onChange={onChange} autoClose={false} />
      <button className="submit" onClick={handleSubmit}>Find Recipes</button>
      </div>
    </form>
    </>
  );  
};  