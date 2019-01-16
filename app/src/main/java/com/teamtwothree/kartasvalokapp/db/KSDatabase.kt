package com.teamtwothree.kartasvalokapp.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.user.UserInfo

@Database(entities = [PointDetails::class, UserInfo::class], version = 1)
abstract class KSDatabase : RoomDatabase(){
    abstract fun getKSDao(): KSDao
}