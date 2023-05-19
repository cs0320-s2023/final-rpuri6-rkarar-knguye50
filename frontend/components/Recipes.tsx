import React, { useState } from 'react';
import Select from 'react-select';
 
import "../style/ingredients.css"
 
/** Handles the front-end logic and rendering of all components */
function Recipes() {

  // interface for each ingredient that would show up in the ingredients dropdown
  interface ingOption {
    label: any;
    value: any;
 }

 // interface for Recipe type returned from back-end
 interface Recipe{
  title: string;
  id: string;
  image: string;
  spoonacularSourceUrl: string;
 }

 // ingredients that show up on the drop-down before actual ingredients are scraped
  const defaultIngs:ingOption[] = [
    {
      value: "Apple",
      label: "Apple"
    },
    {
      value: "Whole milk",
      label: "Whole milk"
    }
  ];
 
  // state const for ingredients selected from dropdown
  const [selectedIngredients, setSelectedIngredients] = useState([]);
 

  // handle onChange event of the dropdown
  const handleChange = (e) => {
    // setSelectedIngredients(Array.isArray(e) ? e.map(x => x.value) : []);
    setSelectedIngredients([...e.map(x => x.value)]);
  }

  // handles clicking the button to scrape ingredients using cached values accessesed from back-end
  const handleScrape = async() => {
    await fetch('http://127.0.0.1:5000/ingredients-cache', {method: 'GET'})
      .then(response => response.json())
      .then(function(data){
      // accesses the value of each element from the most recent cached call
      Object.values(data.result.at(-1)).forEach(ingName =>  {console.log(ingName)
      var ingObj: ingOption = 
             { label: ingName, value: ingName}
      ingOptions.push(ingObj);})
         })
      .catch(error => console.log(error));
  }

  const [recipes, setRecipes] = useState([{title:"mock recipe", image:"s", id: "sd", spoonacularSourceUrl:"xx"}]);
  const [ingOptions, setIngOptions] = useState(defaultIngs)
  const handleSubmit = async () => {
    console.log({selectedIngredients});
    const response = await fetch(`http://localhost:3232/Recipe?ingredients=${selectedIngredients}`);
    console.log("fetching recipes")
    const recipeJson = await response.json();
    const recipes:Recipe[] = await recipeJson["recipes"];
    console.log(recipes)
    setRecipes(recipes);
  };
  const [query, setQuery] = useState("")

 
 
  return (
    <div className="App">
        <div> <h4> Start by clicking the button below to compile Weekly Ad products from Trader Joe's and CVS </h4></div>
    <a className="scrape-a" onClick={handleScrape}>Scrape ingredients</a>
    <p> -- </p>
    <div> <h4> Select the ingredients you would like to use: </h4></div>
 
      <Select
        className="col"
        placeholder="Select Option"
        options={ingOptions}
        value={ingOptions.filter(obj => selectedIngredients.includes(obj.value))} // set selected values
        onChange={handleChange} 
        isMulti
        isClearable
      />
 
      {selectedIngredients && <div style={{ marginTop: 20, lineHeight: '25px' }}>

      </div>}

    <a className="scrape-a" onClick={handleSubmit}>Find Recipes</a>
      <div>
      <p></p>
    <input className="search" placeholder="Search Recipes" onChange={event => setQuery(event.target.value)} />
    </div>
    <p>--</p>

<div>
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
              <p></p>
              Recipe Link:<a href={recipe.spoonacularSourceUrl}> {recipe.spoonacularSourceUrl}</a>
      </div>
    ))
    }
    </div> 
    </div>
  );
}
 
export default Recipes;