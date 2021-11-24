package com.example.foodapp.data.database.entities

import androidx.core.widget.TextViewCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapp.model.Result
import com.example.foodapp.util.Constants.Companion.FAVOURITE_RECIPES_TABLE

@Entity(tableName = FAVOURITE_RECIPES_TABLE)
class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var result:Result
)