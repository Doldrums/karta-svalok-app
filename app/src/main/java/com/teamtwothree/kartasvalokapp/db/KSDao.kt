package com.teamtwothree.kartasvalokapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teamtwothree.kartasvalokapp.model.point.Point
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.user.UserInfo

@Dao
interface KSDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(userInfo: UserInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPoints(points: List<Point>)

    @Query("select * from points")
    fun getAllPoints(): LiveData<List<Point>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPointDetails(pointDetails: PointDetails)

    @Query("select * from user_info where id = 0")
    fun getUserInfo(): LiveData<UserInfo>

    @Query("select * from user_info where id = 0")
    fun getUserInfoBlocking(): UserInfo

    @Query("select * from point_details where id = :id")
    fun getPointDetailsById(id: String): LiveData<PointDetails>

    @Query("select * from point_details where id = :id")
    fun getRawPointDetailsById(id: String): PointDetails

    @Query("select * from point_details")
    fun getAllPointDetails(): LiveData<List<PointDetails>>

}