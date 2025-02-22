package com.example.analytics.presentation

import java.time.LocalDate

data class AnalyticsDashboardState(
    val totalDistanceRun: String,
    val totalTimeRun: String,
    val fastestEverRun: String,
    val avgDistance: String,
    val avgPace: String,
    val avgDistancePerRun: List<Pair<LocalDate, Double>>
)
