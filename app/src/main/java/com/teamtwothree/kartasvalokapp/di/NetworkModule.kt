package com.teamtwothree.kartasvalokapp.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.teamtwothree.kartasvalokapp.BuildConfig
import com.teamtwothree.kartasvalokapp.api.KSApi
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = Kodein.Module("network") {
    bind<Gson>() with singleton {
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }
    bind<OkHttpClient>() with singleton {
        OkHttpClient().newBuilder().build()
    }
    bind<Retrofit>() with singleton {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(instance())
            .addConverterFactory(GsonConverterFactory.create(instance()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
    bind<KSApi>() with singleton { instance<Retrofit>().create(KSApi::class.java) }
}