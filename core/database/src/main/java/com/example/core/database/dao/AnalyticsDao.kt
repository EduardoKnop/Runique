package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.core.database.entity.DateDistanceAvg

@Dao
interface AnalyticsDao {

    @Query("SELECT SUM(distanceMeters) FROM runentity")
    suspend fun getTotalDistance(): Int

    @Query("SELECT SUM(durationMillis) FROM runentity")
    suspend fun getTotalTimeRun(): Long

    @Query("SELECT MAX(maxSpeedKmh) FROM runentity")
    suspend fun getMaxRunSpeed(): Double

    @Query("SELECT AVG(distanceMeters) FROM runentity")
    suspend fun getAvgDistancePerRun(): Double

    @Query("SELECT STRFTIME('%Y-%m-%d', dateTimeUtc) AS date, AVG(distanceMeters) AS distanceMeters " +
            "FROM runentity " +
            "GROUP BY date " +
            "ORDER BY dateTimeUtc")
    suspend fun getAvgDistancePerRunOverTime(): List<DateDistanceAvg>

    @Query("SELECT AVG((durationMillis) / 60000.0) / (distanceMeters / 1000.0) FROM runentity")
    suspend fun getAvgPacePerRun(): Double

    @Query("SELECT MIN(dateTimeUtc) FROM runentity")
    suspend fun getFirstRunDate(): String
}