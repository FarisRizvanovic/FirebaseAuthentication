package com.faris.firebaseaccounts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {

    var email = ""

    val emailSent = MutableLiveData<Boolean>()

    val error = MutableLiveData<String>()

    fun resetPassword(mAuth: FirebaseAuth, testE : String){
        mAuth.sendPasswordResetEmail(testE).addOnCompleteListener {
            if (it.isSuccessful){
                emailSent.value = true
            }
            if (!it.isSuccessful){
                error.value = it.exception!!.message
            }
        }
    }
}