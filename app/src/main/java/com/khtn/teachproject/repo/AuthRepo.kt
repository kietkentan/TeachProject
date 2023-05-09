package com.khtn.teachproject.repo

interface AuthRepo {
    fun loginUser(email: String, password: String, save: Boolean, result: (done: Boolean) -> Unit)
}