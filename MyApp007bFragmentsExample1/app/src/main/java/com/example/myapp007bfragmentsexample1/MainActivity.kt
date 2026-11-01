package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Odsazení obsahu od systémových lišt
        // (spodní okraj si BottomNavigationView řeší sám, proto dole 0)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Spodní navigace přepíná mezi třemi fragmenty
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navToday -> showFragment(TodayFragment())
                R.id.navHistory -> showFragment(HistoryFragment())
                R.id.navStats -> showFragment(StatsFragment())
                else -> return@setOnItemSelectedListener false
            }
            true
        }

        // Výchozí záložka po spuštění
        if (savedInstanceState == null) {
            binding.bottomNav.selectedItemId = R.id.navToday
        }
    }

    // Vymění obsah kontejneru za zadaný fragment
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
