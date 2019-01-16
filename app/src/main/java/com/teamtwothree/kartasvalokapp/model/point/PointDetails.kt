package com.teamtwothree.kartasvalokapp.model.point

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.teamtwothree.kartasvalokapp.model.report.Report

@Entity (tableName = "point_details")
data class PointDetails (@PrimaryKey val id: String,
                         val subject: String,
                         val description: String,
                         val address: String,
                         val name: String,
                         val email: String,
                         val phone: String,
                         val regionName: String,
                         val agree: String,
                         val participate: String,
                         val photo: List<String>,
                         val report: Report,
                         val mapPoint: String,
                         val status: String,
                         val createdAt: String,
                         val updatedAt: String,
                         val comment: String,
                         val regionId: Int,
                         val photoAdmin: List<String>)

