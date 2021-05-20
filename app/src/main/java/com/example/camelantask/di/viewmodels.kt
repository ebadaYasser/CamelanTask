package com.example.camelantask.di

import com.example.camelantask.home.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val  viewModels = module {
    viewModel { HomeViewModel(get()) }
}