package com.example.myapp007bfragmentsexample1

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Jeden zapsaný trénink
data class Training(
    val type: String,
    val distanceKm: Double,
    val minutes: Int,
    val note: String,
    val date: String
) {
    // Tempo jako "m:ss" na zadanou vzdálenost v km (1.0 = na km, 0.1 = na 100 m)
    fun pace(perKm: Double = 1.0): String {
        val totalSeconds = (minutes * 60 / distanceKm * perKm).toInt()
        return String.format(Locale.getDefault(), "%d:%02d", totalSeconds / 60, totalSeconds % 60)
    }

    // Průměrná rychlost v km/h
    fun speedKmh(): Double = distanceKm / (minutes / 60.0)
}

// Sdílené úložiště dat pro všechny fragmenty (drží se v paměti po dobu běhu aplikace)
object TrainingRepository {

    private val trainings = mutableListOf<Training>()

    fun add(type: String, distanceKm: Double, minutes: Int, note: String) {
        val date = SimpleDateFormat("d. M. yyyy HH:mm", Locale.getDefault()).format(Date())
        trainings.add(Training(type, distanceKm, minutes, note, date))
    }

    // Ukázková data pro vyzkoušení aplikace
    fun addSamples(run: String, bike: String, swim: String, walk: String) {
        trainings.add(Training(walk, 4.2, 50, "Procházka se psem", "27. 10. 2026 18:10"))
        trainings.add(Training(run, 7.5, 41, "Lehký výběh", "28. 10. 2026 07:30"))
        trainings.add(Training(bike, 32.0, 75, "Okruh kolem přehrady", "30. 10. 2026 16:45"))
        trainings.add(Training(swim, 1.5, 38, "", "31. 10. 2026 19:00"))
        trainings.add(Training(run, 10.2, 58, "Tempový běh", "1. 11. 2026 08:15"))
    }

    // Nejnovější trénink jako první
    fun all(): List<Training> = trainings.reversed()

    fun count(): Int = trainings.size
    fun totalKm(): Double = trainings.sumOf { it.distanceKm }
    fun totalMinutes(): Int = trainings.sumOf { it.minutes }
    fun longest(): Training? = trainings.maxByOrNull { it.distanceKm }
    fun byType(): Map<String, List<Training>> = trainings.groupBy { it.type }
}

// Formátování km na jedno desetinné místo
fun Double.km(): String = String.format(Locale.getDefault(), "%.1f", this)
