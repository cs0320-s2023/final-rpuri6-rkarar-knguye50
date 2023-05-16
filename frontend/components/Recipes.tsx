import React, { useState } from 'react';
import Select from 'react-select';
 
import "../style/ingredients.css"

// import { ingredients } from ... ?
 
import { mockRecipes } from "../data/mockRecipes";
import { ScaleControl } from "react-map-gl";
import makeAnimated from 'react-select/animated';

function Recipes() {
  interface ingOption {
    label: any;
    value: any;
 }
  var defaultIngs = [
    {
      value: "Apple",
      label: "Apple"
    },
    {
      value: "Whole milk",
      label: "Whole milk"
    }
  ];
 
  // set value for default selection
  const [selectedIngredients, setSelectedIngredients] = useState([]);
 
  // handle onChange event of the dropdown
  const handleChange = (e) => {
    setSelectedIngredients(Array.isArray(e) ? e.map(x => x.value) : []);
  }

  var oFlag = 0


  const handleScrape = async() => {
    await fetch('http://127.0.0.1:5000/ingredients-cache', {method: 'GET'})
      .then(response => response.json())
      .then(function(data){
        if (oFlag != 1){
            for (let i=0;i<Object.values(data.result).flat().length;i++){

              var ing: ingOption = 
                { label: Object.values(data.result).flat()[i], value: Object.values(data.result).flat()[i]}
              
              if (! ingOptions.includes(ing)){
                  ingOptions.push(ing)
              }

            }
            console.log(ingOptions)
            oFlag = 1
            return ingOptions
          }
        })
      .catch(error => console.log(error));
  }

  const [recipes, setRecipes] = useState([]);
  const [ingOptions, setIngOptions] = useState(defaultIngs)
  const handleSubmit = async () => {
    console.log({selectedIngredients});
    console.log("trigger")
    const response = await fetch(`http://localhost:3232/Recipe?ingredients=${selectedIngredients}`);
    console.log("trigger t")
    const recipeJson = await response.json();
    console.log("HERE")
    console.log(recipeJson)
    setRecipes(recipeJson);
  };
  const [query, setQuery] = useState("")

 
 
  return (
    <div className="App">
        <div> <h4> Start by clicking the button below to compile available ingredients from Trader Joe's and CVS </h4></div>
    <a className="scrape-a" onClick={handleScrape}>Scrape ingredients</a>
    <p> -- </p>
    <div> <h4> Select your available ingredients </h4></div>
 
      <Select
        className="col"
        placeholder="Select Option"
        options={ingOptions}
         value={ingOptions.filter(obj => selectedIngredients.includes(obj.value))} // set selected values

        onChange={handleChange} // assign onChange function
        isMulti
        isClearable
      />
 
      {selectedIngredients && <div style={{ marginTop: 20, lineHeight: '25px' }}>
        {/* <div><b>Selected Value: </b> {JSON.stringify(selectedIngredients, null, 2)}</div> */}
      </div>}

    <a className="scrape-a" onClick={handleSubmit}>Find Recipes</a>
      <div>
    <input className="search" placeholder="Search Recipes" onChange={event => setQuery(event.target.value)} />
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

      
    </div>
  );
}
 
export default Recipes;
// import React from "react";
// import { useState } from "react";  
// import { mockRecipes } from "../ingOptions/mockRecipes";


// export const Recipes = () => {  
//   const [recipes, setRecipes] = useState([]);
//   const handleSubmit = async (selectedIngredients) => {
//     // replace with back-end call to get the recipes
//     const response = await fetch(`http://localhost:3232/recipes?ingOptions=${selectedIngredients}`);
//     const ingOptions = await response.json();
//     setRecipes(ingOptions);
//   };
//   const [query, setQuery] = useState("")
 
//   return (  
//     <>

//     <div>
//       <input placeholder="Search Recipes" onChange={event => setQuery(event.target.value)} />
//       </div>

//     {
//  mockRecipes.filter(recipe => {
//     if (query === '') {
//       return recipe;
//     } else if (recipe.title.toLowerCase().includes(query.toLowerCase())) {
//       return recipe;
//     }
//   }).map((recipe, index) => (
//     <div className="box" key={index}>
//       <h2>{recipe.title}</h2>
//             <img src={recipe.image} alt={recipe.title} />
//     </div>
//   ))
// }
//     </>
//   );  
// };  

