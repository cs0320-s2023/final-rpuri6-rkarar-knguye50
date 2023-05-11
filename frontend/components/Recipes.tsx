
// import React from "react";
// import { useState } from "react";  
// import { mockRecipes } from "../data/mockRecipes";


// export const Recipes = () => {  
//   const [recipes, setRecipes] = useState([]);
//   const handleSubmit = async (selectedIngredients) => {
//     // replace with back-end call to get the recipes
//     const response = await fetch(`http://localhost:3232/recipes?ingredients=${selectedIngredients}`);
//     const data = await response.json();
//     setRecipes(data);
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

