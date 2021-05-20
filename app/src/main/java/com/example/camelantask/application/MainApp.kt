package com.example.camelantask.application

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.example.camelantask.di.appModule
import com.example.camelantask.di.network
import com.example.camelantask.di.viewModels
import com.example.camelantask.network.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initClass()
        startKoin {
            androidContext(this@MainApp)
            modules(listOf(appModule,viewModels, network))
        }
    }

    fun initClass() {
        ApiClient.initApiClient()
    }
}