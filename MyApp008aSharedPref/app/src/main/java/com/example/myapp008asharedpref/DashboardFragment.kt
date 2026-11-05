package com.example.myapp008asharedpref

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp008asharedpref.databinding.FragmentDashboardBinding

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
    }

    // Při každém zobrazení načteme aktuální hodnoty
    override fun onResume() {
        super.onResume()
        val prefs = ProfilePrefs.get(requireContext())
        val nickname = prefs.getString(ProfilePrefs.KEY_NICKNAME, "").orEmpty()
        val hardcore = prefs.getBoolean(ProfilePrefs.KEY_HARDCORE, false)

        binding.tvNickname.text = nickname.ifEmpty { getString(R.string.no_nickname) }
        binding.tvHardcore.text = getString(if (hardcore) R.string.hardcore_on else R.string.hardcore_off)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
