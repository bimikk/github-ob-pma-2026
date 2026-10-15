package com.example.myapp005objednavkav2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp005objednavkav2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Přístup k prvkům layoutu výhradně přes ViewBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Odsazení obsahu od systémových lišt
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Změna modelu (lambda) -> jiný obrázek a přepočet ceny
        binding.rgModel.setOnCheckedChangeListener { _, checkedId ->
            val image = when (checkedId) {
                R.id.rbNike -> R.drawable.nike_bota
                R.id.rbHoka -> R.drawable.hoka_bota
                else -> R.drawable.adidas_bota
            }
            binding.ivShoe.setImageResource(image)
            updatePrice()
        }

        // Každá změna výbavy (lambda) -> přepočet ceny
        listOf(binding.cbColor, binding.cbLaces, binding.cbBag).forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ -> updatePrice() }
        }

        // Potvrzení -> předání modelu, výbavy a ceny na rekapitulaci přes Intent
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(this, SummaryActivity::class.java).apply {
                putExtra(SummaryActivity.EXTRA_MODEL, selectedModelName())
                putExtra(SummaryActivity.EXTRA_COLOR, binding.cbColor.isChecked)
                putExtra(SummaryActivity.EXTRA_LACES, binding.cbLaces.isChecked)
                putExtra(SummaryActivity.EXTRA_BAG, binding.cbBag.isChecked)
                putExtra(SummaryActivity.EXTRA_PRICE, totalPrice().toString())
            }
            startActivity(intent)
        }

        updatePrice()
    }

    // Název vybraného modelu pro rekapitulaci
    private fun selectedModelName(): String = when (binding.rgModel.checkedRadioButtonId) {
        R.id.rbNike -> "Nike"
        R.id.rbHoka -> "Hoka"
        else -> "Adidas"
    }

    // Výpočet celkové ceny: základ podle modelu + příplatky
    private fun totalPrice(): Int {
        var price = when (binding.rgModel.checkedRadioButtonId) {
            R.id.rbNike -> PRICE_NIKE
            R.id.rbHoka -> PRICE_HOKA
            else -> PRICE_ADIDAS
        }
        if (binding.cbColor.isChecked) price += PRICE_COLOR
        if (binding.cbLaces.isChecked) price += PRICE_LACES
        if (binding.cbBag.isChecked) price += PRICE_BAG
        return price
    }

    // Zobrazení průběžné ceny
    private fun updatePrice() {
        binding.tvPrice.text = getString(R.string.price_total, totalPrice().toString())
    }

    companion object {
        // Ceny v Kč
        private const val PRICE_ADIDAS = 2490
        private const val PRICE_NIKE = 2990
        private const val PRICE_HOKA = 3490
        private const val PRICE_COLOR = 200
        private const val PRICE_LACES = 90
        private const val PRICE_BAG = 250
    }
}
