package com.teamtwothree.kartasvalokapp.service

import android.location.Location
import androidx.lifecycle.LiveData
import com.teamtwothree.kartasvalokapp.model.point.Point
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.report.Report
import com.teamtwothree.kartasvalokapp.model.user.UserInfo
import kotlinx.coroutines.Deferred
import retrofit2.Call

/**
 * Basic Service for server API interaction and storing user info.
 * Obtain an instance in a field of your class via
 * <code> private val dataService: DataService by kodein.instance() </code>
 */
interface DataService {
    /**
     * Gets [PointDetails] info from server API and saves to local DB
     * @param id String with id of requested point
     * @return [PointDetails] from updated DB
     */
    fun getPointDetails(id: String): LiveData<PointDetails>
    /**
     * Returns a List<[PointDetails]>
     */
    fun getAllPointDetails(): LiveData<List<PointDetails>>
    /**
     * Posts Report to server API and saves result in DB.
     * @param report [Report] to be posted
     * @return String with Id, generated by the server or null if posting failed
     */
    fun postReport(report: Report): LiveData<String>
    /**
     * Returns [UserInfo] from local DB
     */
    fun getUserInfo(): LiveData<UserInfo>
    /**
     * Stores [UserInfo] in local DB
     */
    fun saveUserInfo(userInfo: UserInfo)

    /**
     * TESTING API: Method for future AR integration. Returns List<[Point]>
     */
    fun getAllPoints(): LiveData<List<Point>>
    /**
     * TESTING API: Method for Test Server only. Flushes current remote database state
     */
    fun flushPoints(): Call<Void>

//    fun getSanctionedDumps(): LiveData<List<Location>>
}