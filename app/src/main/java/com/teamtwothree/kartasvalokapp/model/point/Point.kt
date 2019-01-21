package com.teamtwothree.kartasvalokapp.model.point

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points")
data class Point(
    @PrimaryKey val id: String,
    val status: String,
    val mapPoint: String
)
