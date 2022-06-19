package com.faris.firebaseaccounts.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.faris.firebaseaccounts.R
import com.faris.firebaseaccounts.databinding.FragmentLoginBinding
import com.faris.firebaseaccounts.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        mAuth = FirebaseAuth.getInstance()

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        val viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.forgottenPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailAdress.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty()) {
                binding.emailAdress.error = "Unesite email!"
                binding.emailAdress.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailAdress.error = "Unesite validan email!"
                binding.emailAdress.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.password.error = "Unesite password!"
                binding.password.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.password.error = "Unesite password duzi od 6!"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            viewModel.email = email
            viewModel.password = password

            viewModel.userLogin(mAuth)
        }

        viewModel.isAbleToLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_loginFragment_to_userProfileFragment)
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show()
        })

        viewModel.emailVerified.observe(viewLifecycleOwner, Observer {
            if (!it){
                Snackbar.make(view, "Molimo verifikujte vas racun!", Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show()
                binding.progressBar.visibility = View.GONE
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}