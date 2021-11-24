package com.example.foodapp.adapter

import android.accessibilityservice.AccessibilityGestureEvent.CREATOR
import android.accessibilityservice.AccessibilityServiceInfo.CREATOR
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodapp.R
import com.example.foodapp.model.ExtendedIngredient
import com.example.foodapp.util.Constants.Companion.BASE_IMAGE_URL
import com.example.foodapp.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*
import org.w3c.dom.Text
import javax.inject.Inject

class IngredientsAdapter @Inject constructor()  :
    RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>(), Parcelable {
    private var ingredientsList = emptyList<ExtendedIngredient>()

    constructor(parcel: Parcel) : this() {
        //ingredientsList = parcel.createTypedArrayList(ExtendedIngredient().CREATOR)
    }

    init {
        Log.d("TAG",ingredientsList.toString())
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredients_row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.ingredient_image.load(BASE_IMAGE_URL + ingredientsList[position].image.toString())
        //holder.itemView.ingredient_image.load(BASE_IMAGE_URL+"apple.jpg")
        Log.d("TAG",ingredientsList[position].image)
        holder.itemView.ingredient_name.text = ingredientsList[position].name.capitalize()
        holder.itemView.ingredient_amount.text = ingredientsList[position].amount.toString()
        holder.itemView.ingredient_unit.text = ingredientsList[position].unit
        holder.itemView.ingredient_consistency.text = ingredientsList[position].consistency
        holder.itemView.ingredient_original.text = ingredientsList[position].original
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(ingredientsList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IngredientsAdapter> {
        override fun createFromParcel(parcel: Parcel): IngredientsAdapter {
            return IngredientsAdapter(parcel)
        }

        override fun newArray(size: Int): Array<IngredientsAdapter?> {
            return arrayOfNulls(size)
        }
    }
}