package com.faris.firebaseaccounts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faris.firebaseaccounts.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {

    val isRegistered = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    var fullName = ""
    var age = ""
    var email = ""
    var password = ""

    fun register(mAuth: FirebaseAuth) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = User(fullName, age, email)
                FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(user)
                    .addOnCompleteListener {
                        isRegistered.value = it.isSuccessful
                    }
            } else {
                isRegistered.value = false
                error.value = it.exception!!.message.toString()
            }
        }
    }
}