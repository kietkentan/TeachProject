package com.khtn.teachproject.helper

import android.content.Context
import android.content.SharedPreferences
import com.khtn.teachproject.utils.SharedPrefConstants

class SharedPreferencesManager(context: Context) {
    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        val sharedPreferences = context.getSharedPreferences(SharedPrefConstants.LOCAL_SHARED_PREF, Context.MODE_PRIVATE)
        this.pref = sharedPreferences
        this.editor = sharedPreferences.edit()
    }

    fun saveByKey(key: String?, value: String?): Boolean {
        return try {
            editor.putString(key, value).apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun retrieveByKey(key: String?): String? {
        return pref.getString(key, "")
    }

    fun clearData() {
        try {
            val edit = pref.edit()
            if (edit != null) {
                val clear = edit.clear()
                clear?.apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}