package com.example.camelantask.di

import com.example.camelantask.network.ApiClient
import com.example.camelantask.repository.PlacesRepository
import org.koin.dsl.module

val network = module {
    single { getApiClient() }
    single { PlacesRepository(get()) }
}

private fun getApiClient() = ApiClient.apiClient
