package com.teamtwothree.kartasvalokapp.service

import androidx.lifecycle.LiveData
import com.teamtwothree.kartasvalokapp.model.point.Point
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.report.Report
import com.teamtwothree.kartasvalokapp.model.user.UserInfo
import kotlinx.coroutines.Deferred
import retrofit2.Call

interface DataService {
    fun getAllPoints(): LiveData<List<Point>>
    fun getPointDetails(id: String): LiveData<PointDetails>
    fun postReport(report: Report): LiveData<String>
    fun flushPoints(): Call<Void>
    fun getUserInfo(): LiveData<UserInfo>
    fun saveUserInfo(userInfo: UserInfo)
}