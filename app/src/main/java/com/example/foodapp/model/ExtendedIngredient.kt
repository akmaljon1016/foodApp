package com.example.foodapp.model


import com.google.gson.annotations.SerializedName

data class ExtendedIngredient(

    @SerializedName("amount")
    val amount: Float,
    @SerializedName("consistency")
    val consistency: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("unit")
    val unit: String
)