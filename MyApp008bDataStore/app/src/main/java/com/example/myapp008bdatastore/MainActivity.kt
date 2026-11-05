package com.example.myapp008bdatastore

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp008bdatastore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Odsazení obsahu od systémových lišt
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Při prvním spuštění zobrazíme Dashboard
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DashboardFragment())
                .commit()
        }
    }

    // Otevře Nastavení; tlačítko Zpět (systémové i ve Snackbaru) vrátí na Dashboard
    fun openSettings() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }
}
