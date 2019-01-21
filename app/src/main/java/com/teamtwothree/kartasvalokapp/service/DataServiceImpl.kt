package com.teamtwothree.kartasvalokapp.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.teamtwothree.kartasvalokapp.api.KSApi
import com.teamtwothree.kartasvalokapp.db.KSDao
import com.teamtwothree.kartasvalokapp.model.point.Point
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.report.Report
import com.teamtwothree.kartasvalokapp.model.user.UserInfo
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.MultipartBody
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.File

class DataServiceImpl(override val kodein: Kodein) : DataService, KodeinAware {

    val ksApi: KSApi by kodein.instance()
    val ksDao: KSDao by kodein.instance()

    override fun getAllPoints(): LiveData<List<Point>> {
        GlobalScope.launch { ksDao.insertPoints(ksApi.getAllPoints().await()) }
        return ksDao.getAllPoints()
    }

    override fun flushPoints() = ksApi.deleteAllPoints()

    override fun getPointDetails(id: String): LiveData<PointDetails> {
        GlobalScope.launch {
            ksDao.insertPointDetails(ksApi.getPointDetails(id).await())
        }
        return ksDao.getPointDetailsById(id)
    }

    override fun postReport(report: Report): LiveData<String> {
        val body = MultipartBody.Builder().addFormDataPart("subject", report.subject)
            .addFormDataPart("address", report.address)
            .addFormDataPart("region_name", report.regionName)
            .addFormDataPart("description", report.description)
            .addFormDataPart("name", report.name)
            .addFormDataPart("phone", report.phone)
            .addFormDataPart("email", report.email)
            .addFormDataPart("map_point", report.mapPoint)
            .addFormDataPart("agree", "1")
            .addFormDataPart("participate", report.participate)

        report.photo.forEach {
            body.addPart(
                MultipartBody.Part.createFormData(
                    "photo",
                    it,
                    RequestBody.create(MediaType.parse("image/*"), File(it))
                )
            )
        }
        return MutableLiveData<String>().apply {
            GlobalScope.launch {
                val id = ksApi.postReport(body.setType(MediaType.get("multipart/form-data")).build()).await()
                getPointDetails(id).observeOnce(Observer {
                    it?.also {
                        this@apply.postValue(id) }
                })
            }
        }
    }

    override fun getUserInfo(): LiveData<UserInfo> = ksDao.getUserInfo()
    override fun saveUserInfo(userInfo: UserInfo) = ksDao.insertUserInfo(userInfo)

}

fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            if (t != null) {
                observer.onChanged(t)
                removeObserver(this)
            }
        }
    })
}