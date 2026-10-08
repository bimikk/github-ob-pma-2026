package com.example.myapp004objednavka

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp004objednavka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Radio -> změna obrázku
        binding.rgPizza.setOnCheckedChangeListener { _, checkedId ->
            val image = when (checkedId) {
                R.id.rbSalami -> R.drawable.pizza_salami
                R.id.rbFormaggi -> R.drawable.pizza_formaggi
                else -> R.drawable.pizza_margherita
            }
            binding.ivPizza.setImageResource(image)
            updatePrice()
        }

        // Checkboxy -> přepočet ceny
        listOf(binding.cbCheese, binding.cbOlives, binding.cbChili, binding.cbLarge).forEach {
            it.setOnCheckedChangeListener { _, _ -> updatePrice() }
        }

        binding.btnOrder.setOnClickListener { showSummary() }

        updatePrice()
    }

    private fun basePrice(): Int = when (binding.rgPizza.checkedRadioButtonId) {
        R.id.rbSalami -> 179
        R.id.rbFormaggi -> 199
        else -> 159
    }

    private fun totalPrice(): Int {
        var price = basePrice()
        if (binding.cbCheese.isChecked) price += 30
        if (binding.cbOlives.isChecked) price += 20
        if (binding.cbChili.isChecked) price += 10
        if (binding.cbLarge.isChecked) price += 60
        return price
    }

    private fun updatePrice() {
        binding.tvPrice.text = getString(R.string.price_label, totalPrice())
    }

    private fun showSummary() {
        val pizzaName = when (binding.rgPizza.checkedRadioButtonId) {
            R.id.rbSalami -> getString(R.string.pizza_salami)
            R.id.rbFormaggi -> getString(R.string.pizza_formaggi)
            else -> getString(R.string.pizza_margherita)
        }

        val extras = listOf(binding.cbCheese, binding.cbOlives, binding.cbChili, binding.cbLarge)
            .filter { it.isChecked }
            .joinToString(", ") { it.text }
            .ifEmpty { getString(R.string.summary_no_extras) }

        binding.tvSummary.text = getString(R.string.summary_text, pizzaName, extras, totalPrice())
    }
}