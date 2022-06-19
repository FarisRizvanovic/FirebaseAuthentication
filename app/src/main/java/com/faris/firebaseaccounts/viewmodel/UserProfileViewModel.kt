package com.faris.firebaseaccounts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faris.firebaseaccounts.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel(){

    val userWithData = MutableLiveData<User>()

    val failedToGetdata = MutableLiveData<Boolean>()

    fun signOut(mAuth: FirebaseAuth){
        mAuth.signOut()
    }

    fun getCurrentUser(mAuth: FirebaseAuth){
        viewModelScope.launch {
            val user = mAuth.currentUser
            val reference = FirebaseDatabase.getInstance().getReference("Users")
            val userId = user!!.uid

            reference.child(userId).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue(User::class.java)

                    if (userProfile !=null){
                        userWithData.value = userProfile
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    failedToGetdata.value = true
                }

            })
        }
    }
}