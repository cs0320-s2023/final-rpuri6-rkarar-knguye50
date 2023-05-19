// import React, { useState } from 'react';
// import Select from 'react-select';
// import getelIngs from './Recipes';
// import "../style/ingredients.css"
 

// function InputIngredients() {


//  // interface for Recipe type returned from back-end
//  interface Recipe{
//   title: string;
//   id: string;
//   image: string;
//   spoonacularSourceUrl: string;
//  }

 


//   const [recipes, setRecipes] = useState([]);
//   const handleSubmit = async () => {
//     const selIngs = getSelIngs();
//     console.log({selIngs});
//     const response = await fetch(`http://localhost:3232/Recipe?ingredients=${selIngs}`);
//     console.log("fetching recipes")
//     const recipeJson = await response.json();
//     const recipes:Recipe[] = await recipeJson["recipes"];
//     console.log(recipes)
//     setRecipes(recipes);
//   };
//   const [query, setQuery] = useState("")

 
 
//   return (
//     <div className="App">
//         {/* <div> <h4> Start by clicking the button below to compile Weekly Ad ingredients from Trader Joe's and CVS </h4></div>
//     <a className="scrape-a" onClick={handleScrape}>Scrape ingredients</a>
//     <p> -- </p>
//     <div> <h4> Select the ingredients you would like to use </h4></div>
 
//       <Select
//         className="col"
//         placeholder="Select Option"
//         options={ingOptions}
//         value={ingOptions.filter(obj => selectedIngredients.includes(obj.value))} // set selected values
//         onChange={handleChange} 
//         isMulti
//         isClearable
//       />
 
//       {selectedIngredients && <div style={{ marginTop: 20, lineHeight: '25px' }}>

//       </div>}
//   */}
//     <a className="scrape-a" onClick={handleSubmit}>Find Recipes</a>
//     <p>--</p> 
//       <div>
//     <input className="search" placeholder="Search Recipes" onChange={event => setQuery(event.target.value)} />
//     </div>
//   <p></p>
  
// <h1> Found Recipes: </h1>

// <div>
//     { 
//     recipes.filter(recipe => {
//       if (query === '') {
//         return recipe;
//       } else if (recipe.title.toLowerCase().includes(query.toLowerCase())) {
//         return recipe;
//       }
//     }).map((recipe, index) => (
//       <div className="box" key={index}>
//         <h2>{recipe.title}</h2>
//               <img src={recipe.image} alt={recipe.title} />
//               <p></p>
//               Recipe Link:<a href={recipe.spoonacularSourceUrl}> {recipe.spoonacularSourceUrl}</a>
//       </div>
//     ))
//     }
//     </div> 
//     </div>
//   );
// }
//  export default InputIngredients;
