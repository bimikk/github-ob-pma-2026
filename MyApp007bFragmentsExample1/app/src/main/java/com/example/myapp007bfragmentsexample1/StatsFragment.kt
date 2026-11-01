package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.FragmentStatsBinding

// Fragment 3: souhrnné statistiky ze všech tréninků
class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showStats()
    }

    private fun showStats() {
        val repo = TrainingRepository
        val hasData = repo.count() > 0
        binding.tvByTypeTitle.isVisible = hasData
        binding.tvByType.isVisible = hasData

        if (!hasData) {
            binding.tvSummary.text = getString(R.string.stats_empty)
            return
        }

        // Celkové souhrny
        val minutes = repo.totalMinutes()
        val longest = repo.longest()!!
        binding.tvSummary.text = listOf(
            getString(R.string.stats_count, repo.count()),
            getString(R.string.stats_distance, repo.totalKm().km()),
            getString(R.string.stats_time, minutes / 60, minutes % 60),
            getString(R.string.stats_longest, longest.distanceKm.km(), longest.type)
        ).joinToString("\n")

        // Rozpad podle druhu aktivity
        binding.tvByType.text = repo.byType().entries.joinToString("\n") { (type, list) ->
            getString(R.string.stats_type_row, type, list.size, list.sumOf { it.distanceKm }.km())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
