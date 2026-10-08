package com.example.hodkostkouxml_bim

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Unicode symboly stěn kostky 1–6
    private val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

    // Parametry animace: 10 změn s prodlevou 250 ms
    private val animationSteps = 10
    private val animationDelayMs = 250L

    // Vylepšení: kolikrát padla každá stěna (index 0 = jednička)
    private val faceCounts = IntArray(6)

    // Prvky rozhraní získané přes findViewById
    private lateinit var tvDice: TextView
    private lateinit var tvResult: TextView
    private lateinit var tvStats: TextView
    private lateinit var tvSummary: TextView
    private lateinit var btnRoll: Button
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Odsazení obsahu od systémových lišt
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Napojení prvků z XML layoutu
        tvDice = findViewById(R.id.tvDice)
        tvResult = findViewById(R.id.tvResult)
        tvStats = findViewById(R.id.tvStats)
        tvSummary = findViewById(R.id.tvSummary)
        btnRoll = findViewById(R.id.btnRoll)
        btnReset = findViewById(R.id.btnReset)

        btnRoll.setOnClickListener { rollDice() }
        btnReset.setOnClickListener {
            faceCounts.fill(0)
            updateStats()
        }

        updateStats()
    }

    // Hod: 10 náhodných „probliknutí“ a pak výsledná hodnota
    private fun rollDice() {
        lifecycleScope.launch {
            // Během animace jsou tlačítka zakázaná
            btnRoll.isEnabled = false
            btnReset.isEnabled = false
            tvResult.text = getString(R.string.result_rolling)

            repeat(animationSteps) {
                showValue(randomValue(), highlight = false)
                delay(animationDelayMs)
            }

            // Výsledný hod – uložíme do statistiky
            val result = randomValue()
            showValue(result, highlight = true)
            faceCounts[result - 1]++
            tvResult.text = if (result == 6) {
                getString(R.string.result_six)
            } else {
                getString(R.string.result_value, result)
            }
            updateStats()

            btnRoll.isEnabled = true
            btnReset.isEnabled = true
        }
    }

    // Náhodná hodnota 1–6
    private fun randomValue(): Int = (1..6).random()

    // Imperativní aktualizace: ručně přepíšeme text a barvu TextView
    private fun showValue(value: Int, highlight: Boolean) {
        tvDice.text = diceSymbols[value - 1]
        val color = if (highlight && value == 6) R.color.dice_six else R.color.dice_normal
        tvDice.setTextColor(ContextCompat.getColor(this, color))
    }

    // Vypíše počty jednotlivých stěn, celkový počet hodů a průměr
    private fun updateStats() {
        // Dva řádky po třech stěnách, aby se text vešel na šířku displeje
        tvStats.text = diceSymbols.indices.chunked(3).joinToString("\n") { row ->
            row.joinToString("    ") { i -> "${diceSymbols[i]} ${faceCounts[i]}" }
        }
        val total = faceCounts.sum()
        val sum = faceCounts.withIndex().sumOf { (i, count) -> (i + 1) * count }
        val average = if (total == 0) "–" else String.format(Locale.getDefault(), "%.2f", sum.toDouble() / total)
        tvSummary.text = getString(R.string.stats_summary, total, average)
    }
}
