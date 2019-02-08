package com.teamtwothree.kartasvalokapp.service.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.api.KSApi
import com.teamtwothree.kartasvalokapp.db.KSDao
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.report.Report
import com.teamtwothree.kartasvalokapp.model.user.UserInfo
import com.teamtwothree.kartasvalokapp.service.DataService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.MultipartBody
import org.kodein.di.generic.instance
import java.io.File

class FirebaseDataService : DataService {

    override fun getAllPointDetails(): LiveData<List<PointDetails>> = ksDao.getAllPointDetails()

    val ksApi: KSApi by AppDelegate.getKodein().instance()
    val ksDao: KSDao by AppDelegate.getKodein().instance()

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
                    it.toString(),
                    RequestBody.create(MediaType.parse("image/*"), File(it.toString()))
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
    override fun getUserInfoBlocking(): UserInfo = ksDao.getUserInfoBlocking()

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