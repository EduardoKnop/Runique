package com.example.run.domain

import com.example.core.domain.location.LocationTimestamp

data class RunData(
    val distanceMeters: Int = 0,
    val locations: List<List<LocationTimestamp>> = emptyList()
)
