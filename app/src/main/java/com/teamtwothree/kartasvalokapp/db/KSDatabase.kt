package com.teamtwothree.kartasvalokapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teamtwothree.kartasvalokapp.model.point.Point
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.user.UserInfo

@Database(entities = [PointDetails::class, UserInfo::class, Point::class], version = 1)
@TypeConverters(Converters::class)
abstract class KSDatabase : RoomDatabase(){
    abstract fun getKSDao(): KSDao
}