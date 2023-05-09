package com.khtn.teachproject.repo

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

class AuthRepoImp(
    /*private val auth: FirebaseAuth,*/
    private val appPreferences: SharedPreferences,
): AuthRepo {
    override fun loginUser(
        email: String,
        password: String,
        save: Boolean,
        result: (done: Boolean) -> Unit
    ) {
        result.invoke(true)
    }
}