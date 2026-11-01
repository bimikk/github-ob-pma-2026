package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.FragmentHistoryBinding

// Fragment 2: seznam všech zapsaných tréninků
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSample.setOnClickListener {
            TrainingRepository.addSamples(
                getString(R.string.type_run), getString(R.string.type_bike),
                getString(R.string.type_swim), getString(R.string.type_walk)
            )
            showTrainings()
        }
    }

    // Při každém zobrazení načteme aktuální data
    override fun onResume() {
        super.onResume()
        showTrainings()
    }

    private fun showTrainings() {
        val trainings = TrainingRepository.all()
        binding.tvEmpty.isVisible = trainings.isEmpty()
        binding.btnSample.isVisible = trainings.isEmpty()
        binding.llItems.removeAllViews()

        // Pro každý trénink vytvoříme jeden TextView
        val padding = (12 * resources.displayMetrics.density).toInt()
        trainings.forEach { t ->
            val text = getString(R.string.history_item, t.type, t.date, t.distanceKm.km(), t.minutes, performance(t)) +
                if (t.note.isNotEmpty()) "\n${t.note}" else ""
            val item = TextView(requireContext()).apply {
                this.text = text
                textSize = 16f
                setPadding(0, padding, 0, padding)
            }
            binding.llItems.addView(item)
        }
    }

    // Výkon podle druhu aktivity: kolo = km/h, plavání = min/100 m, jinak min/km
    private fun performance(t: Training): String = when (t.type) {
        getString(R.string.type_bike) -> getString(R.string.speed_kmh, t.speedKmh().km())
        getString(R.string.type_swim) -> getString(R.string.pace_100m, t.pace(0.1))
        else -> getString(R.string.pace_km, t.pace())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
