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
            val SORT_BY = "sort_by"
            val CATEGORIES = "categories"
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

    fun getInt(key: String, defaultValue: Int = 0) : Int {
       return sharedPref.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false) : Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float = 0f) : Float {
        return sharedPref.getFloat(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String) : String {
        return sharedPref.getString(key, defaultValue)
    }
}