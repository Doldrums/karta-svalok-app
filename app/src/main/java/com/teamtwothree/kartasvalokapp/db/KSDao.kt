package com.teamtwothree.kartasvalokapp.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.user.UserInfo

@Dao
interface KSDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(userInfo: UserInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPointDetails(pointDetails: PointDetails)

    @Query("select * from user_info")
    fun getUserInfo(): LiveData<UserInfo>

    @Query("select * from point_details where id = :id")
    fun getPointDetailsById(id: String): LiveData<PointDetails>

    @Query("select * from point_details")
    fun getAllPointDetails(): LiveData<List<PointDetails>>

}