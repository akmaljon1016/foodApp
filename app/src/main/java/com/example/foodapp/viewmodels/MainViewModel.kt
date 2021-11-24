package com.example.foodapp.viewmodels

import android.app.Application
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foodapp.data.Repository
import com.example.foodapp.data.database.entities.FavouritesEntity
import com.example.foodapp.data.database.entities.RecipesEntity
import com.example.foodapp.model.FoodRecipe
import com.example.foodapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /**ROOM DATABASE**/
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    val readFavouriteRecipes: LiveData<List<FavouritesEntity>> =
        repository.local.readFavouriteRecipes().asLiveData()

    fun insertRecipes(recipesEntity: RecipesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipes(recipesEntity)
    }

    fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavouriteRecipes(favouritesEntity)
        }

    fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavouriteRecipe(favouritesEntity)
        }

    fun deleteAllFavouriteRecipe() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllfavouriteRecipes()
        }


    /**RETROFIT**/
    var recipesResponce: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(queries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponce.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponce.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponce.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipe(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponce.value = NetworkResult.Error(e.message)
            }
        } else {
            recipesResponce.value = NetworkResult.Error("No Internet Connection")
        }
    }


    private suspend fun searchRecipesSafeCall(queries: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(queries)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchRecipesResponse.value = NetworkResult.Error("Recipes not found1")
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheRecipe(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("TimeOut")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API key limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }
//    private fun handleSearchRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
//        when {
//            response.message().toString().contains("timeout") -> {
//                return NetworkResult.Error("TimeOut")
//            }
//            response.code() == 402 -> {
//                return NetworkResult.Error("API key limited")
//            }
//            response.body()!!.results.isNullOrEmpty() -> {
//                return NetworkResult.Error("Recipes not found")
//            }
//            response.isSuccessful -> {
//                val foodRecipes = response.body()
//                return NetworkResult.Success(foodRecipes!!)
//            }
//            else -> {
//                return NetworkResult.Error(response.message())
//            }
//        }
//    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}