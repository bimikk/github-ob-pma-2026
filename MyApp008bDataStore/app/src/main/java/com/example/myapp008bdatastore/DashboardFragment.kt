package com.example.myapp008bdatastore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapp008bdatastore.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch

// Fragment 1: výpis uloženého profilu
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEdit.setOnClickListener { (requireActivity() as MainActivity).openSettings() }

        // Sledujeme Flow z DataStore – výpis se obnoví sám po každé změně
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ProfileStore.profile(requireContext()).collect { profile ->
                    binding.tvNickname.text = profile.nickname.ifEmpty { getString(R.string.no_nickname) }
                    binding.tvHardcore.text =
                        getString(if (profile.hardcore) R.string.hardcore_on else R.string.hardcore_off)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
