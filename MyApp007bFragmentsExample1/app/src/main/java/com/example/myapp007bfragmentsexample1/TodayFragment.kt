package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.FragmentTodayBinding

// Fragment 1: formulář pro zápis tréninku
class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener { saveTraining() }
    }

    private fun saveTraining() {
        // Desetinná čárka i tečka
        val distance = binding.etDistance.text.toString().replace(',', '.').toDoubleOrNull()
        val minutes = binding.etMinutes.text.toString().toIntOrNull()

        // Kontrola vstupů
        if (distance == null || distance <= 0) {
            binding.etDistance.error = getString(R.string.err_distance)
            return
        }
        if (minutes == null || minutes <= 0) {
            binding.etMinutes.error = getString(R.string.err_minutes)
            return
        }

        val type = selectedTypeName()

        TrainingRepository.add(type, distance, minutes, binding.etNote.text.toString().trim())
        Toast.makeText(requireContext(), R.string.msg_saved, Toast.LENGTH_SHORT).show()

        // Vyčištění formuláře
        binding.etDistance.text.clear()
        binding.etMinutes.text.clear()
        binding.etNote.text.clear()
    }

    // Název vybraného druhu aktivity
    private fun selectedTypeName(): String = getString(
        when (binding.rgType.checkedRadioButtonId) {
            R.id.rbBike -> R.string.type_bike
            R.id.rbSwim -> R.string.type_swim
            R.id.rbWalk -> R.string.type_walk
            else -> R.string.type_run
        }
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
