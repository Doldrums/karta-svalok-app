package com.teamtwothree.kartasvalokapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.teamtwothree.kartasvalokapp.db.KSDao
import com.teamtwothree.kartasvalokapp.db.KSDatabase
import com.teamtwothree.kartasvalokapp.di.networkModule
import com.teamtwothree.kartasvalokapp.service.*
import com.teamtwothree.kartasvalokapp.service.data.FirebaseDataService
import com.teamtwothree.kartasvalokapp.service.generation.TestReportService
import com.teamtwothree.kartasvalokapp.service.validation.FirebaseValidationService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppDelegate : Application() {
    init {
        instance = this
    }

    val kodein: Kodein = Kodein {
        import(networkModule)
        bind<KSDao>() with singleton {
            Room.databaseBuilder(this@AppDelegate, KSDatabase::class.java, "ksdb")
                .fallbackToDestructiveMigration()
                .build().getKSDao()
        }
        bind<AppDelegate>() with provider { this@AppDelegate }
        bind<ValidationService>() with singleton { FirebaseValidationService() }
        bind<DataService>() with singleton { FirebaseDataService() }
        bind<ReportService>() with singleton { TestReportService() }
        bind<AppStateService>() with singleton { AppStateService() }
        bind<FusedLocationProviderClient>() with singleton { LocationServices.getFusedLocationProviderClient(applicationContext) }
    }

    companion object {
        private lateinit var instance: AppDelegate

        fun applicationContext() : Context {
            return instance.applicationContext
        }
        fun getKodein() : Kodein {
            return instance.kodein
        }
    }

}