package com.khtn.teachproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khtn.teachproject.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo,
): ViewModel() {
    private val _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean>
        get() = _login

    fun login(
        email: String,
        password: String,
        save: Boolean
    ) {
        /*
            true: Success
            false: Fail
        */
        _login.value = false
        authRepo.loginUser(email, password, save) {
            _login.value = it
        }
    }
}