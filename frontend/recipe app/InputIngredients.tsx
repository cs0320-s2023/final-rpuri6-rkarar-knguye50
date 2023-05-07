import { MultiSelect } from "@progress/kendo-react-dropdowns"; 
import '@progress/kendo-theme-default/dist/all.css'; 
import React from "react";
// import { ingredients } from ... ?
import { useState } from "react";  
  
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
  const handleSearch = async (ingredients) => {
    const response = await fetch(`https://api.example.com/recipes?ingredients=${ingredients}`);
    const data = await response.json();
    setRecipes(data);
  };
 
  const handleSubmit = (event) => {
    handleSearch(selectedIngredients);
    // handle getting the list of recipes
     event.preventDefault();
  };

  // source for multi-select: https://www.telerik.com/blogs/quick-guide-dropdown-menus-react 
  return (  
    <><form className="k-form k-my-8">
      <label className="k-label k-mb-3">Input your ingredients here</label>
      <MultiSelect data={ingredients} value={selectedIngredients} onChange={onChange} autoClose={false} />
      <button type="submit" onClick={handleSubmit}>Search Recipes</button>
    </form>
    
    <ul>
        {recipes.map((recipe) => (
          <li key={recipe.id}>
            <h2>{recipe.title}</h2>
            <img src={recipe.image} alt={recipe.title} />
            <ul>
              {recipe.ingredients.map((ingredient) => (
                <li key={ingredient}>{ingredient}</li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    
    </>
  );  
};  