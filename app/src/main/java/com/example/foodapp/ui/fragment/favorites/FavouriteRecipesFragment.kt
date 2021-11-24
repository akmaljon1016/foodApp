package com.example.foodapp.ui.fragment.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.adapter.FavouriteRecipesAdapter
import com.example.foodapp.databinding.FragmentFavouriteRecipesBinding
import com.example.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favourite_recipes.view.*
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private val mAdapter: FavouriteRecipesAdapter by lazy { FavouriteRecipesAdapter(requireActivity()) }
    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner=this
        binding.mainViewModel=mainViewModel
        binding.mAdapter=mAdapter
        setupRecyclerview(binding.favouriteRecipesRecyclerView)
//        mainViewModel.readFavouriteRecipes.observe(viewLifecycleOwner, { favouritesEntity ->
//            mAdapter.setData(favouritesEntity)
//        })
        return binding.root
    }

    private fun setupRecyclerview(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}