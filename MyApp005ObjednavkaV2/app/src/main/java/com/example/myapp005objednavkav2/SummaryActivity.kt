package com.example.myapp005objednavkav2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp005objednavkav2.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Odsazení obsahu od systémových lišt
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Načtení dat z Intentu
        val model = intent.getStringExtra(EXTRA_MODEL).orEmpty()
        val price = intent.getStringExtra(EXTRA_PRICE) ?: "0"

        // Složení textu s vybranou výbavou
        val extras = buildList {
            if (intent.getBooleanExtra(EXTRA_COLOR, false)) add(getString(R.string.extra_color))
            if (intent.getBooleanExtra(EXTRA_LACES, false)) add(getString(R.string.extra_laces))
            if (intent.getBooleanExtra(EXTRA_BAG, false)) add(getString(R.string.extra_bag))
        }.joinToString(", ").ifEmpty { getString(R.string.summary_no_extras) }

        binding.tvModel.text = getString(R.string.summary_model, model)
        binding.tvExtras.text = getString(R.string.summary_extras, extras)
        binding.tvTotal.text = getString(R.string.price_total, price)

        // Návrat k úpravě – zavře obrazovku, výběr na první obrazovce zůstane
        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        // Klíče pro data předávaná v Intentu
        const val EXTRA_MODEL = "extra_model"
        const val EXTRA_COLOR = "extra_color"
        const val EXTRA_LACES = "extra_laces"
        const val EXTRA_BAG = "extra_bag"
        const val EXTRA_PRICE = "extra_price"
    }
}
