package com.example.foodapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.IngredientsAdapter
import com.example.foodapp.model.Result
import com.example.foodapp.util.Constants.Companion.RECIPE_RESULT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ingredient.view.*
import javax.inject.Inject

@AndroidEntryPoint
class IngredientFragment : Fragment() {

    //val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    @Inject
    lateinit var mAdapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ingredient, container, false)
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT)
        setupRecyclerView(view)
        myBundle?.extendedIngredients!!.let { mAdapter.setData(it) }
        //Toast.makeText(requireContext(),myBundle.toString(), Toast.LENGTH_SHORT).show()
        return view
    }

    private fun setupRecyclerView(view: View) {
        view.ingredients_recyclerview.adapter = mAdapter
        view.ingredients_recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }
}