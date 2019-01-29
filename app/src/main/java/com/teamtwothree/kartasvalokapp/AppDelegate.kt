package com.teamtwothree.kartasvalokapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.teamtwothree.kartasvalokapp.db.KSDao
import com.teamtwothree.kartasvalokapp.db.KSDatabase
import com.teamtwothree.kartasvalokapp.di.networkModule
import com.teamtwothree.kartasvalokapp.service.ValidationService
import com.teamtwothree.kartasvalokapp.service.FirebaseValidationService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppDelegate : Application() {
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: AppDelegate

        fun applicationContext() : Context {
            return instance.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()

        val kodein: Kodein = Kodein {
            import(networkModule)
            bind<KSDao>() with singleton {
                Room.databaseBuilder(this@AppDelegate, KSDatabase::class.java, "ksdb")
                    .fallbackToDestructiveMigration()
                    .build().getKSDao()
            }
            bind<AppDelegate>() with provider { this@AppDelegate }
            bind<ValidationService>() with singleton { FirebaseValidationService() }
        }
    }

}