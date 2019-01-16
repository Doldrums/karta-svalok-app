package com.teamtwothree.kartasvalokapp

import android.app.Application
import android.arch.persistence.room.Room
import com.teamtwothree.kartasvalokapp.db.KSDatabase
import com.teamtwothree.kartasvalokapp.di.networkModule
import org.kodein.di.Kodein

class AppDelegate : Application() {

    override fun onCreate() {
        super.onCreate()

        val kodein: Kodein = Kodein {
            import(networkModule)
            Room.databaseBuilder(this@AppDelegate, KSDatabase::class.java, "ksdb")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}