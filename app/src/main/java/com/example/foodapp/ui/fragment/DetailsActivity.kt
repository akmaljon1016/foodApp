package com.example.foodapp.ui.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foodapp.R
import com.example.foodapp.adapter.PagerAdapter
import com.example.foodapp.data.database.entities.FavouritesEntity
import com.example.foodapp.util.Constants.Companion.RECIPE_RESULT
import com.example.foodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_deatails.*
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity(R.layout.activity_deatails) {
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private var recipeSaved = false
    private var savedRecipeId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        /**Fragments**/
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientFragment())
        fragments.add(InstructionFragment())
        /**Titles**/
        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")
        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT, args.result)
        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

//        val tabStrip: LinearLayout = tabLayout.getChildAt(0) as LinearLayout
//        for (i in 0 until tabStrip.size) {
//            tabStrip.getChildAt(i).setOnLongClickListener(object : View.OnLongClickListener {
//                @RequiresApi(Build.VERSION_CODES.O)
//                override fun onLongClick(p0: View?): Boolean {
//                    if (i == 0) {
//                        Toast.makeText(this@DetailsActivity, "0", Toast.LENGTH_SHORT).show()
//                    }
//                    if (i == 1) {
//                        Toast.makeText(this@DetailsActivity, "1", Toast.LENGTH_SHORT).show()
//                    }
//                    if (i == 2) {
//                        Toast.makeText(this@DetailsActivity, "2", Toast.LENGTH_SHORT).show()
//                    }
//                    return true
//                }
//            })
//        }
//        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//               if (tab?.position==1)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favourites_menu)
        checkSavedRecipes(menuItem)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favourites_menu && !recipeSaved) {
            saveFavourites(item)
        } else if (item.itemId == R.id.save_to_favourites_menu && recipeSaved) {
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem?) {
        mainViewModel.readFavouriteRecipes.observe(this, { favouritesEntity ->
            try {
                for (savedRecipe in favouritesEntity) {
                    if (savedRecipe.result.recipeId == args.result.recipeId) {
                        if (menuItem != null) {
                            changeMenuItemColor(menuItem, R.color.yellow)
                            savedRecipeId = savedRecipe.id
                            recipeSaved=true
                        }
                    }
                    else{
                        if (menuItem != null) {
                            changeMenuItemColor(menuItem,R.color.white)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    private fun saveFavourites(item: MenuItem) {
        val favouriteEntity = FavouritesEntity(0, args.result)
        mainViewModel.insertFavouriteRecipe(favouriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipes saved.")
        recipeSaved = true
    }

    private fun removeFromFavourites(item: MenuItem) {
        val favouriteEntity = FavouritesEntity(savedRecipeId, args.result)
        mainViewModel.deleteFavouriteRecipe(favouriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from favourites")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(detailsLayout, message, Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}