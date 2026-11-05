package com.example.myapp008bdatastore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp008bdatastore.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Fragment 2: formulář – NOVÁ METODA (DataStore v coroutine, hlavní vlákno volné)
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // Počítadlo pro interaktivní prvek
    private var tapCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Načtení uložených hodnot do formuláře (jednorázově, asynchronně)
        viewLifecycleOwner.lifecycleScope.launch {
            val profile = ProfileStore.profile(requireContext()).first()
            binding.etNickname.setText(profile.nickname)
            binding.switchHardcoreMode.isChecked = profile.hardcore
        }

        binding.btnSave.setOnClickListener { saveProfile() }

        // Interaktivní prvek: počítadlo klepnutí – funguje i během ukládání
        updateTapCount()
        binding.btnTap.setOnClickListener { tapCount++; updateTapCount() }
        binding.btnTapReset.setOnClickListener { tapCount = 0; updateTapCount() }
    }

    private fun saveProfile() {
        val profile = Profile(
            binding.etNickname.text.toString().trim(),
            binding.switchHardcoreMode.isChecked
        )
        // Během ukládání nejde tlačítko zmáčknout podruhé
        binding.btnSave.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch {
            // Simulace náročné práce – delay() NEBLOKUJE hlavní vlákno,
            // kolečko se dál točí a ostatní prvky reagují
            delay(3000)
            ProfileStore.save(requireContext(), profile)

            binding.btnSave.isEnabled = true
            Snackbar.make(binding.settingsRoot, R.string.msg_saved, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_back) {
                    // Zpět na Dashboard s výpisem
                    parentFragmentManager.popBackStack()
                }
                .show()
        }
    }

    private fun updateTapCount() {
        binding.tvTapCount.text = getString(R.string.tap_count, tapCount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
