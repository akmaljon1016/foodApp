package com.example.foodapp.ui.fragment.foodjoke

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.foodapp.NetworkResult
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentFoodJokeBinding
import com.example.foodapp.util.Constants.Companion.API_KEY
import com.example.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()

    private var _binding: FragmentFoodJokeBinding? = null
    val binding get() = _binding!!

    private var foodJoke = "No Food Joke"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFoodJokeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)

        mainViewModel.getFoodJoke(API_KEY)

        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is com.example.foodapp.util.NetworkResult.Success -> {
                    binding.foodJokeTextview.text = response.data?.text
                    Toast.makeText(requireContext(), response.data?.text, Toast.LENGTH_SHORT).show()
                    if (response.data!=null){
                        foodJoke=response.data.text
                    }
                }
                is com.example.foodapp.util.NetworkResult -> {
                    loadFromCache()
                    Toast.makeText(requireContext(), "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                is com.example.foodapp.util.NetworkResult.Loading -> {
                    Log.d("FoodJokeFragment", "Loading")
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()

                }

            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT,foodJoke)
                this.type="text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFromCache() {
        mainViewModel.readFoodJoke.observe(viewLifecycleOwner, { database ->
            if (!database.isNullOrEmpty()) {
                binding.foodJokeTextview.text = database[0].foodJoke.text
                foodJoke=database[0].foodJoke.text
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}