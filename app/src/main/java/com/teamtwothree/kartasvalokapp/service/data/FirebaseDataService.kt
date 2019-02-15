package com.teamtwothree.kartasvalokapp.service.data

import androidx.lifecycle.LiveData
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

    private val ksApi: KSApi by AppDelegate.getKodein().instance()
    private val ksDao: KSDao by AppDelegate.getKodein().instance()

    override fun getPointDetails(id: String): LiveData<PointDetails> {
        GlobalScope.launch {
            ksDao.insertPointDetails(ksApi.getPointDetails(id).await())
        }
        return ksDao.getPointDetailsById(id)
    }

    override suspend fun postReport(report: Report): String {
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
                    File(it.path).path,
                    RequestBody.create(MediaType.parse("image/*"), File(it.path))
                )
            )
        }

        val id = ksApi.postReport(body.setType(MediaType.get("multipart/form-data")).build()).await()
        ksDao.insertPointDetails(ksApi.getPointDetails(id).await())
        return id
    }

    override fun getAllPointDetails(): LiveData<List<PointDetails>> = ksDao.getAllPointDetails()
    override fun getUserInfo(): LiveData<UserInfo> = ksDao.getUserInfo()
    override fun saveUserInfo(userInfo: UserInfo) = ksDao.insertUserInfo(userInfo)
    override fun getUserInfoBlocking(): UserInfo = ksDao.getUserInfoBlocking()
    override fun flushPoints() = ksApi.deleteAllPoints()
}
