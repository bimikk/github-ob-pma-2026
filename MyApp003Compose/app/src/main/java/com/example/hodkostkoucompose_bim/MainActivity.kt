package com.example.hodkostkoucompose_bim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodkostkoucompose_bim.ui.theme.DiceNormal
import com.example.hodkostkoucompose_bim.ui.theme.DiceNormalDark
import com.example.hodkostkoucompose_bim.ui.theme.DiceSix
import com.example.hodkostkoucompose_bim.ui.theme.HodKostkouComposeBimTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

// Unicode symboly stěn kostky 1–6
private val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

// Parametry animace: 10 změn s prodlevou 250 ms
private const val ANIMATION_STEPS = 10
private const val ANIMATION_DELAY_MS = 250L

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HodKostkouComposeBimTheme {
                // Scaffold předá innerPadding = odsazení od systémových lišt
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiceScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Hlavní obrazovka: nadpis, kostka, výsledek, tlačítko a statistika
@Composable
fun DiceScreen(modifier: Modifier = Modifier) {
    // Stav obrazovky – při jeho změně Compose sám překreslí UI
    var diceValue by remember { mutableIntStateOf(1) }
    var isRolling by remember { mutableStateOf(false) }
    var lastResult by remember { mutableIntStateOf(0) } // 0 = zatím se nehodilo

    // Vylepšení: kolikrát padla každá stěna (index 0 = jednička)
    val faceCounts = remember { mutableStateListOf(0, 0, 0, 0, 0, 0) }

    val scope = rememberCoroutineScope()

    // Hod: 10 náhodných „probliknutí“ a pak výsledná hodnota
    fun rollDice() {
        scope.launch {
            isRolling = true
            repeat(ANIMATION_STEPS) {
                diceValue = (1..6).random()
                delay(ANIMATION_DELAY_MS)
            }
            val result = (1..6).random()
            diceValue = result
            lastResult = result
            faceCounts[result - 1]++
            isRolling = false
        }
    }

    // Barva kostky – šestka po dokončení hodu svítí zlatě
    val diceColor = when {
        !isRolling && lastResult == 6 -> DiceSix
        isSystemInDarkTheme() -> DiceNormalDark
        else -> DiceNormal
    }

    // Text pod kostkou podle stavu
    val resultText = when {
        isRolling -> stringResource(R.string.result_rolling)
        lastResult == 0 -> stringResource(R.string.result_start)
        lastResult == 6 -> stringResource(R.string.result_six)
        else -> stringResource(R.string.result_value, lastResult)
    }

    // Souhrn statistiky: počet hodů a průměr
    val total = faceCounts.sum()
    val average = if (total == 0) "–" else String.format(
        Locale.getDefault(), "%.2f",
        faceCounts.withIndex().sumOf { (i, c) -> (i + 1) * c }.toDouble() / total
    )

    // Svislé rozložení, vše vystředěné
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        // Velký symbol kostky – čte se ze stavu diceValue
        Text(
            text = diceSymbols[diceValue - 1],
            fontSize = 160.sp,
            color = diceColor
        )

        Text(
            text = resultText,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tlačítko je během animace zakázané
        Button(onClick = { rollDice() }, enabled = !isRolling) {
            Text(
                text = stringResource(R.string.btn_roll),
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        // Vylepšení: statistika hodů
        Text(
            text = stringResource(R.string.stats_title),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 32.dp)
        )
        // Dva řádky po třech stěnách
        diceSymbols.indices.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                row.forEach { i ->
                    Text(
                        text = "${diceSymbols[i]} ${faceCounts[i]}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Text(
            text = stringResource(R.string.stats_summary, total, average),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        TextButton(
            onClick = { for (i in faceCounts.indices) faceCounts[i] = 0 },
            enabled = !isRolling
        ) {
            Text(stringResource(R.string.btn_reset))
        }
    }
}

// Náhled v Android Studiu (Design / Split)
@Preview(showBackground = true)
@Composable
fun DiceScreenPreview() {
    HodKostkouComposeBimTheme {
        DiceScreen()
    }
}
