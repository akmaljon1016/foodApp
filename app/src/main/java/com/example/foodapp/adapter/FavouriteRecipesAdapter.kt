package com.example.foodapp.adapter

import android.view.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.database.entities.FavouritesEntity
import com.example.foodapp.databinding.FavouriteRecipesRowLayoutBinding
import com.example.foodapp.ui.fragment.favorites.FavouriteRecipesFragmentDirections
import com.example.foodapp.util.RecipesDiffUtil

class FavouriteRecipesAdapter : RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>(),ActionMode.Callback {

    private var favouriteRecipes = emptyList<FavouritesEntity>()

    class MyViewHolder(private val binding: FavouriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favouritesEntity: FavouritesEntity) {
            binding.favouriteEntity = favouritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FavouriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedRecipe = favouriteRecipes[position]
        holder.bind(selectedRecipe)
        /**
         * Single Click Listener
         * **/
        holder.itemView.setOnClickListener {
            val action =
                FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDeatailsActivity(
                    selectedRecipe.result
                )
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = favouriteRecipes.size

    fun setData(newFavouriteRecipes: List<FavouritesEntity>) {
        val FavouriteRecipesDiffUtill = RecipesDiffUtil(favouriteRecipes, newFavouriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(FavouriteRecipesDiffUtill)
        favouriteRecipes = newFavouriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)

    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
    }
}