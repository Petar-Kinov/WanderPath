package com.example.wanderpath.domain.auth

import com.example.wanderpath.data.model.User
import android.graphics.Bitmap
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderpath.R
import com.example.wanderpath.data.auth.AuthRepository
import com.example.wanderpath.ui.login.LoginFormState
import com.example.wanderpath.ui.login.LoginResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.wanderpath.data.Result
import com.example.wanderpath.ui.login.LoggedInUserView

private const val TAG = "AuthViewModel"
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _signUpResult = MutableLiveData<FirebaseUser>()
    val signUpIsResult: LiveData<FirebaseUser> = _signUpResult

    fun login(username: String, password: String) {
        if (isPasswordValid(username) && isPasswordValid(password)) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = authRepository.login(username, password)
                if (result is Result.Success) {
                    _loginResult.postValue(LoginResult(success = result.data.displayName?.let {
                        LoggedInUserView(
                            displayName = it
                        )
                    }))
                } else {
                    _loginResult.postValue(LoginResult(error = R.string.login_failed))
                    Log.d("TAG", "login: ${_loginResult.value}")
                }
            }
        }
    }


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun signup(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.signUp(user)
            if (result is Result.Success) {
                _signUpResult.postValue(result.data)
                _loginResult.postValue(LoginResult(success = result.data.displayName?.let {
                    LoggedInUserView(
                        displayName = it
                    )
                }))
            } else {
                Log.d(TAG, "signup: Sign up failed ${(result as Result.Error).exception}")
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun deleteUser() {
        authRepository.deleteUser()
    }
}