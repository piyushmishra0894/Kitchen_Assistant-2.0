package com.example.zaheenkhan.kitchenassistant;

import android.content.Context;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RecipeApiClient {

    public RecipeApiClient(Context ctx)
    {
        Context mCtx = ctx;
        queue = Volley.newRequestQueue(mCtx.getApplicationContext());
    }

    /*
    * To use functions in this class, please override onSuccess callback as input.
    * See examples in each functions' comment
    */
    public interface VolleyCallback {
        void onSuccess(JSONObject result);
        void onSuccess(JSONArray result);
    }

    /*
    * Params:
    * @id - The id of the recipe.
    * @includeNutrition - Include nutrition data to the recipe information. Nutrition data is per serving.
    * If you want the nutrition data for the entire recipe, just multiply by the number of servings.
    *
    * Example of returned JSON:
    *  {
    *          "vegetarian": false,
    *          "vegan": false,
    *          "glutenFree": true,
    *          "dairyFree": true,
    *          "veryHealthy": false,
    *          "cheap": false,
    *          "veryPopular": false,
    *          "sustainable": false,
    *          "weightWatcherSmartPoints": 21,
    *          "gaps": "no",
    *          "lowFodmap": false,
    *          "ketogenic": false,
    *          "whole30": false,
    *          "servings": 10,
    *          "sourceUrl": "http://www.epicurious.com/recipes/food/views/Char-Grilled-Beef-Tenderloin-with-Three-Herb-Chimichurri-235342",
    *          "spoonacularSourceUrl": "https://spoonacular.com/char-grilled-beef-tenderloin-with-three-herb-chimichurri-156992",
    *          "aggregateLikes": 0,
    *          "creditText": "Epicurious",
    *          "sourceName": "Epicurious",
    *          "extendedIngredients": [
    *      {
    *          "id": 13926,
    *              "aisle": "Meat",
    *              "image": "https://spoonacular.com/cdn/ingredients_100x100/beef-tenderloin.jpg",
    *              "name": "beef tenderloin",
    *              "amount": 3.5,
    *              "unit": "pound",
    *              "unitShort": "lb",
    *              "unitLong": "pounds",
    *              "originalString": "1 3 1/2-pound beef tenderloin",
    *              "metaInformation": []
    *      },
    *      {
    *          "id": 1022068,
    *              "aisle": "Oil, Vinegar, Salad Dressing",
    *              "image": "https://spoonacular.com/cdn/ingredients_100x100/red-wine-vinegar.jpg",
    *              "name": "red wine vinegar",
    *              "amount": 3,
    *              "unit": "tablespoons",
    *              "unitShort": "T",
    *              "unitLong": "tablespoons",
    *              "originalString": "3 tablespoons Sherry wine vinegar or red wine vinegar",
    *              "metaInformation": [
    *                                     "red"
    *                                 ]
    *      }
    *  ],
    *      "id": 156992,
    *          "title": "Char-Grilled Beef Tenderloin with Three-Herb Chimichurri",
    *          "readyInMinutes": 45,
    *          "image": "https://spoonacular.com/recipeImages/char-grilled-beef-tenderloin-with-three-herb-chimichurri-156992.jpg",
    *          "imageType": "jpg",
    *          "instructions": "PreparationFor spice rub: Combine all ingredients in small bowl.                                                                            Do ahead: Can be made 2 days ahead. Store airtight at room temperature.                                    For chimichurri sauce:                                        Combine first 8 ingredients in blender; blend until almost smooth. Add 1/4 of parsley, 1/4 of cilantro, and 1/4 of mint; blend until incorporated. Add remaining herbs in 3 more additions, pureeing until almost smooth after each addition.                                                                            Do ahead: Can be made 3 hours ahead. Cover; chill.                                    For beef tenderloin:                                        Let beef stand at room temperature 1 hour.                                                                            Prepare barbecue (high heat). Pat beef dry with paper towels; brush with oil. Sprinkle all over with spice rub, using all of mixture (coating will be thick). Place beef on grill; sear 2 minutes on each side. Reduce heat to medium-high. Grill uncovered until instant-read thermometer inserted into thickest part of beef registers 130F for medium-rare, moving beef to cooler part of grill as needed to prevent burning, and turning occasionally, about 40 minutes. Transfer to platter; cover loosely with foil and let rest 15 minutes. Thinly slice beef crosswise. Serve with chimichurri sauce.                                                                            *Available at specialty foods stores and from tienda.com."
    *  }
    *
    * Example of how to use the SearchRecipes functions:
    *
    * GetRecipeInfo(10, false, new VolleyCallback(){
    *     @Override
    *     public void onSuccess(JSONObject result){
    *         // TODO: Handle returned JSONObject
    *     }
    * });
    */
    public void GetRecipeInfo
            (Integer id, Boolean includeNutrition, final VolleyCallback callback) {
        String url = BASE_URL + id + "/information?" +
                "includeNutrition=" + includeNutrition;

        getJsonRequest(url, callback);
    }

    /*
    * Params:
    * @ingredients - A comma-separated list of ingredients that the recipes should contain.
    * @fillIngredients - Add information about the used and missing ingredients in each recipe.
    * @number - The maximal number of recipes to return (default = 5).
    * @ranking - Whether to maximize used ingredients (1) or minimize missing ingredients (2) first.

    * Example of returned JSONArray:
    * [
    *   {
    *       "id": 641803,
    *           "title": "Easy & Delish! ~ Apple Crumble",
    *           "image": "https://spoonacular.com/recipeImages/Easy---Delish--Apple-Crumble-641803.jpg",
    *           "usedIngredientCount": 3,
    *           "missedIngredientCount": 4,
    *           "likes": 1
    *   },
    *   {
    *       "id": 645152,
    *           "title": "Grandma's Apple Crisp",
    *           "image": "https://spoonacular.com/recipeImages/Grandmas-Apple-Crisp-645152.jpg",
    *           "usedIngredientCount": 3,
    *           "missedIngredientCount": 6,
    *           "likes": 1
    *   },
    *   {
    *       "id": 657563,
    *           "title": "Quick Apple Ginger Pie",
    *           "image": "https://spoonacular.com/recipeImages/Quick-Apple-Ginger-Pie-657563.jpg",
    *           "usedIngredientCount": 3,
    *           "missedIngredientCount": 6,
    *           "likes": 1
    *   },
    *   {
    *       "id": 639487,
    *           "title": "Cinnamon Sugar Fried Apples",
    *           "image": "https://spoonacular.com/recipeImages/Cinnamon-Sugar-Fried-Apples-639487.jpg",
    *           "usedIngredientCount": 3,
    *           "missedIngredientCount": 8,
    *           "likes": 46
    *   },
    *   {
    *       "id": 643426,
    *           "title": "Fresh Apple Cake With Caramel Sauce",
    *           "image": "https://spoonacular.com/recipeImages/Fresh-Apple-Cake-With-Caramel-Sauce-643426.jpg",
    *           "usedIngredientCount": 3,
    *           "missedIngredientCount": 12,
    *           "likes": 9
    *   }
    * ]
    *
    * Example of how to use the SearchRecipes functions:
    *
    * SearchRecipesByIngredients("apple,banana", true, 50, 0, new VolleyCallback(){
    *     @Override
    *     public void onSuccess(JSONArray result){
    *         // TODO: Handle returned JSONArray
    *     }
    * });
    */
    public void SearchRecipesByIngredients
            (String ingredients, Boolean fillIngredients, Integer num, Integer ranking, final VolleyCallback callback) {
        String url = BASE_URL + "findByIngredients?" +
                "fillIngredients=" + fillIngredients +
                "&ingredients=" + ingredients +
                "&limitLicense=" + "false" +
                "&number=" + num +
                "&ranking=" + ranking;

        getJsonArrayRequest(url, callback);
    }

    /*
    * Params:
    * @query - The (natural language) recipe search query.
    *
    * @cuisine - The cuisine(s) of the recipes. One or more (comma separated) of the following:
    * african, chinese, japanese, korean, vietnamese, thai, indian, british, irish,
    * french, italian, mexican, spanish, middle eastern, jewish, american, cajun,
    * southern, greek, german, nordic, eastern european, caribbean, or latin american.
    *
    * @diet - The diet to which the recipes must be compliant.
    * Possible values are: pescetarian, lacto vegetarian, ovo vegetarian, vegan, and vegetarian.
    *
    * @intolerance - A comma-separated list of intolerance.
    * All found recipes must not have ingredients that could cause problems for people with one of the given tolerances.
    * Possible values are: dairy, egg, gluten, peanut, sesame, seafood, shellfish, soy, sulfite, tree nut, and wheat.
    *
    * @num - The number of results to return (between 0 and 100).
    * @offset - The number of results to skip (between 0 and 900).
    *
    * Example of returned JSON:
    *   {
    *       "results": [
    *       {
    *           "id": 541691,
    *               "title": "Black Bean Mole Burgers",
    *               "readyInMinutes": 45,
    *               "image": "black-bean-mole-burgers-541691.jpg",
    *               "imageUrls": [
    *           "black-bean-mole-burgers-541691.jpg"
    *     ]
    *       },
    *       {
    *           "id": 34035,
    *               "title": "Sprouted Lentil Veggie Burger",
    *               "readyInMinutes": 30,
    *               "image": "sprouted_lentil_veggie_burger-34035.jpg",
    *               "imageUrls": [
    *           "sprouted_lentil_veggie_burger-34035.jpg",
    *                   "sprouted-lentil-veggie-burger-2-34035.jpg"
    *     ]
    *       },
    *       {
    *           "id": 766301,
    *               "title": "Queso Cheese Burgers",
    *               "readyInMinutes": 60,
    *               "image": "queso-cheese-burgers-766301.jpg",
    *               "imageUrls": [
    *           "queso-cheese-burgers-766301.jpg"
    *     ]
    *       },
    *       {
    *           "id": 761774,
    *               "title": "Simple Soybean Burgers",
    *               "readyInMinutes": 45,
    *               "image": "simple-soybean-burgers-761774.jpg",
    *               "imageUrls": [
    *           "simple-soybean-burgers-761774.jpg"
    *     ]
    *       }
    *   ],
    *       "baseUri": "https://spoonacular.com/recipeImages/",
    *           "offset": 0,
    *           "number": 10,
    *           "totalResults": 10,
    *           "processingTimeMs": 323,
    *           "expires": 1473587241426,
    *           "isStale": false
    *   }
    *
    * Example of how to use the SearchRecipes functions:
    *
    * SearchRecipes("Cheese Burgers", "", "", "", 50, 0, new VolleyCallback(){
    *     @Override
    *     public void onSuccess(JSONArray result){
    *         // TODO: Handle returned JSONArray
    *     }
    * });
    */
    public void SearchRecipes
            (String query, String cuisine, String diet, String intolerance, Integer num, Integer offset, final VolleyCallback callback) {
        String url = BASE_URL + "search?" +
                "cuisine=" + cuisine +
                "diet=" + diet +
                "&instructionsRequired=" + "false" +
                "&intolerances=" + intolerance +
                "&limitLicense=" + "false" +
                "&number=" + num +
                "&offset=" + offset +
                "&query=" + query;

        getJsonRequest(url, callback);
    }


    private void getJsonRequest(String url, final VolleyCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Mashape-Key", X_MASHAPE_KEY);
                params.put("Accept", "application/json");
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void getJsonArrayRequest(String url, final VolleyCallback callback) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-Mashape-Key", X_MASHAPE_KEY);
                params.put("Accept", "application/json");
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
    private String X_MASHAPE_KEY = "o7ztm7FmTxmshRo6Tg5OVwpx85bvp1NC6QAjsnoNWCnKuYoOQ6";
    private RequestQueue queue;
}
