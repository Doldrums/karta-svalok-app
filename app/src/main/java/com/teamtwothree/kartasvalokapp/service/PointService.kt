package com.teamtwothree.kartasvalokapp.service

import com.teamtwothree.kartasvalokapp.model.point.Point
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.report.Report
import kotlinx.coroutines.Deferred
import retrofit2.Call

interface PointService {
    fun getAllPoints(): Deferred<List<Point>>
    fun getPointDetails(id: String): Deferred<PointDetails>
    fun postReport(report: Report): Deferred<String>
    fun flushPoints(): Call<Void>
}