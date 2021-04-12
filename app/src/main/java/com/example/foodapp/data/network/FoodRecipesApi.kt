package com.example.foodapp.data.network

import com.example.foodapp.model.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {
     @GET("/recipes/complexSearch")
     suspend fun getRecipe(
          @QueryMap queries:Map<String,String>
     ):Response<FoodRecipe>
}