package com.faris.firebaseaccounts.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.faris.firebaseaccounts.R
import com.faris.firebaseaccounts.databinding.FragmentUserProfileBinding
import com.faris.firebaseaccounts.viewmodel.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class UserProfileFragment : Fragment() {
    private var _binding :FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        mAuth = FirebaseAuth.getInstance()

        val viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        viewModel.getCurrentUser(mAuth)

        binding.signOutButton.setOnClickListener {
            viewModel.signOut(mAuth)
            findNavController().navigate(R.id.action_userProfileFragment_to_loginFragment)
        }

        viewModel.userWithData.observe(viewLifecycleOwner, Observer {
            binding.emailAdress.text = it.email
            binding.fullName.text = it.fullName
            binding.age.text = it.age
        })


        viewModel.failedToGetdata.observe(viewLifecycleOwner, Observer {
            if (it){
                Snackbar.make(view,"Desila se greska!", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show()
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}