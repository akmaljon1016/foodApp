package com.example.foodapp.di

import com.example.foodapp.adapter.IngredientsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object AdapterModules {

    @JvmStatic
    @Provides
    fun provideAdapter(): IngredientsAdapter {
        return IngredientsAdapter()
    }
}