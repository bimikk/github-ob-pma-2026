package com.example.myapp008asharedpref

import android.content.Context
import android.content.SharedPreferences

// Jedno místo pro název souboru a klíče SharedPreferences
object ProfilePrefs {
    const val FILE = "gamer_profile"
    const val KEY_NICKNAME = "nickname"
    const val KEY_HARDCORE = "hardcore_mode"

    fun get(context: Context): SharedPreferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
}
