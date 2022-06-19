package com.faris.firebaseaccounts.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.faris.firebaseaccounts.R
import com.faris.firebaseaccounts.databinding.FragmentRegisterBinding
import com.faris.firebaseaccounts.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        mAuth = FirebaseAuth.getInstance()

        val viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.registerButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val fullName = binding.fullName.text.toString()
            val age = binding.age.text.toString()

            if (fullName.isEmpty()) {
                binding.fullName.error = "Puno ime je obavezno!"
                binding.fullName.requestFocus()
                return@setOnClickListener
            }
            if (age.isEmpty()) {
                binding.age.error = "Broj godina je obavezan!"
                binding.age.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.email.error = "Email je obavezan!"
                binding.email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.email.error = "Molimo unesite vazeci email!"
                binding.email.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.password.error = "Password je obavezan!"
                binding.password.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.password.error = "Password mora biti duzi od 6!"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            viewModel.email = email
            viewModel.password = password
            viewModel.fullName = fullName
            viewModel.age = age

            viewModel.register(mAuth)

        }

        viewModel.isRegistered.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(view,"Uspjesno registrovan korisnik", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.GREEN).show()
                binding.progressBar.visibility = View.GONE
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view,it, Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show()
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}