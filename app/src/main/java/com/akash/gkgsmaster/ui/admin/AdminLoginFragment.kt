package com.akash.gkgsmaster.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentAdminLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminLoginFragment : Fragment(R.layout.fragment_admin_login) {

    private var _binding: FragmentAdminLoginBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AdminViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminLoginBinding.bind(view)

        setupObservers()

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username == "admin" && password == "admin123") {
                findNavController().navigate(R.id.action_adminLoginFragment_to_adminDashboardFragment)
            } else if (username == "akashray398" && password == "Akash@3980") {
                 findNavController().navigate(R.id.action_adminLoginFragment_to_adminDashboardFragment)
            } else {
                Toast.makeText(context, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginEvents.collect { event ->
                when (event) {
                    is AdminViewModel.AdminLoginEvent.Success -> {
                        println("Admin Login: Success")
                        findNavController().navigate(R.id.action_adminLoginFragment_to_adminDashboardFragment)
                    }
                    is AdminViewModel.AdminLoginEvent.Error -> {
                        println("Admin Login: Error ${event.message}")
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
