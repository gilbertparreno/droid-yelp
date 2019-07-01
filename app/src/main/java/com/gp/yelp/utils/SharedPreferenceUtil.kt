package com.gp.yelp.utils

import android.content.Context
import java.lang.IllegalStateException

class SharedPreferenceUtil(context: Context) {

    val sharedPref = context.getSharedPreferences(
            "filter_", Context.MODE_PRIVATE)

    class Key {
        companion object {
            val RADIUS = "radius"
            val OPEN_NOW = "open_now"
        }
    }

    fun write(key: String, value: Any?) {
        with (sharedPref.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> {
                    throw IllegalStateException("Invalid value.")
                }
            }
            commit()
        }
    }

    fun getInt(key: String, defaultValue: Int) : Int {
       return sharedPref.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean) : Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }
}