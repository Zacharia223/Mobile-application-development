package com.example.tenantmanagement.ui.tenants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.tenantmanagement.databinding.FragmentViewTenantsBinding
import android.content.Intent
import android.net.Uri
import com.example.tenantmanagement.ui.viewmodel.TenantViewModel

class ViewTenantsFragment : Fragment() {

    private var _binding: FragmentViewTenantsBinding? = null
    private val binding get() = _binding!!
    private val args: ViewTenantsFragmentArgs by navArgs()
    private val viewModel: TenantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewTenantsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        val newTenant = args.newTenant

        // Display the new tenant through Data Binding.
        binding.tenant = newTenant

        // Show "Just added" only when a tenant was passed.
        binding.newTenantGroup.visibility =
            if (newTenant == null) View.GONE else View.VISIBLE
        binding.callButton.setOnClickListener {
            if (newTenant != null) {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel", newTenant.phone, null)
                )
                startActivity(intent)
            }
        }

        viewModel.allTenantsText.observe(viewLifecycleOwner) { text ->
            binding.allTenantsTextView.text = text
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
