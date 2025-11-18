package com.example.lab4.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lab4.R
import com.example.lab4.databinding.FragmentLoginBinding
import com.example.lab4.utils.SessionManager
import kotlin.onSuccess


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextNumberPassword.text.toString()

            Log.d("LoginFragment", "Email: $email, Password: $password")

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email and Password are required", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            Log.d("LoginFragment", "authResult: $result")
            result.onSuccess { authResponse ->
                Toast.makeText(requireContext(), "Welcome${authResponse.user.name}", Toast.LENGTH_LONG).show()
                val session = SessionManager(requireContext().applicationContext)
                session.saveAuthToken(authResponse.tokens.accessToken)


                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Login failed: ${error.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
