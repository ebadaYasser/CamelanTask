package com.example.camelantask.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.camelantask.utils.AppPreferenceConstants.FILE_NAME
import com.example.camelantask.utils.AppPreferenceConstants.MODE_VALUE

class PrefManager(context: Context) {
    private lateinit var sharedPreferences: SharedPreferences

    init {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setSingleMode(singleMode: Boolean) =
        sharedPreferences.edit().putBoolean(MODE_VALUE, singleMode).apply()

    fun isSingleMode(): Boolean =
        sharedPreferences.getBoolean(AppPreferenceConstants.MODE_VALUE, false)
}