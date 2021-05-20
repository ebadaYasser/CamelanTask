package com.example.camelantask.di

import com.example.camelantask.utils.PrefManager
import org.koin.dsl.module

val appModule = module {
    single { PrefManager(get()) }
}