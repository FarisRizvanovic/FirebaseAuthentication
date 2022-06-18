package com.faris.firebaseaccounts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    val isAbleToLogin = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    var email = ""
    var password = ""

    fun userLogin(mAuth: FirebaseAuth){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                isAbleToLogin.value = true
            }else{
                isAbleToLogin.value = false
                error.value = it.exception!!.message
            }

        }
    }
}