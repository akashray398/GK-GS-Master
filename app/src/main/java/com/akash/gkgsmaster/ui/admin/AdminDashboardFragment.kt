package com.akash.gkgsmaster.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.FragmentAdminDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminDashboardFragment : Fragment(R.layout.fragment_admin_dashboard) {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminDashboardBinding.bind(view)

        binding.cvManageBooklets.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_adminBookletFragment)
        }

        binding.cvManageQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_adminQuizFragment)
        }

        binding.cvManageAffairs.setOnClickListener {
            println("Navigating to Manage Current Affairs")
            Toast.makeText(context, "Manage Current Affairs Coming Soon", Toast.LENGTH_SHORT).show()
        }
        
        binding.cvBackupRestore.setOnClickListener {
            println("Navigating to Backup/Restore")
            Toast.makeText(context, "Database Tools Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
