package com.example.myapp008asharedpref

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp008asharedpref.databinding.FragmentSettingsBinding

// Fragment 2: formulář – STARÁ METODA (SharedPreferences na hlavním vlákně)
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

        // Načtení uložených hodnot do formuláře
        val prefs = ProfilePrefs.get(requireContext())
        binding.etNickname.setText(prefs.getString(ProfilePrefs.KEY_NICKNAME, ""))
        binding.switchHardcoreMode.isChecked = prefs.getBoolean(ProfilePrefs.KEY_HARDCORE, false)

        binding.btnSave.setOnClickListener { saveProfile() }

        // Interaktivní prvek: počítadlo klepnutí
        updateTapCount()
        binding.btnTap.setOnClickListener { tapCount++; updateTapCount() }
        binding.btnTapReset.setOnClickListener { tapCount = 0; updateTapCount() }
    }

    private fun saveProfile() {
        // Simulace náročné práce PŘÍMO na hlavním (UI) vlákně:
        // aplikace na 3 s zamrzne – kolečko se zastaví a tlačítka nereagují
        Thread.sleep(3000)

        // Synchronní zápis – commit() čeká, až se data zapíší na disk
        ProfilePrefs.get(requireContext()).edit()
            .putString(ProfilePrefs.KEY_NICKNAME, binding.etNickname.text.toString().trim())
            .putBoolean(ProfilePrefs.KEY_HARDCORE, binding.switchHardcoreMode.isChecked)
            .commit()

        Toast.makeText(requireContext(), R.string.msg_saved, Toast.LENGTH_SHORT).show()
    }

    private fun updateTapCount() {
        binding.tvTapCount.text = getString(R.string.tap_count, tapCount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
