package com.example.foodapp.ui.fragment.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodapp.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*
import java.lang.Exception


class RecipesBottomSheet : BottomSheetDialogFragment() {

    lateinit var recipesViewModel: RecipesViewModel
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.recipes_bottom_sheet, container, false)
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, Observer {
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType
            updateChip(it.selectedMealTypeId, mView.mealType_chipGroup)
            updateChip(it.selectedDietTypeId, mView.dietType_chipGroup)
        })
        mView.mealType_chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val checkedMealType = chip.text.toString().toLowerCase()
            mealTypeChip = checkedMealType
            mealTypeChipId = checkedId
        }
        mView.dietType_chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val checkedMealType = chip.text.toString().toLowerCase()
            dietTypeChip = checkedMealType
            dietTypeChipId = checkedId
        }
        mView.apply_btn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            val action=RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }
        return mView
    }

    private fun updateChip(chipId: Int, chipgroup: ChipGroup) {
        if (chipId != null) {
            try {
                chipgroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet",e.message.toString())
            }
        }
    }
}