package com.teamtwothree.kartasvalokapp.service

import com.teamtwothree.kartasvalokapp.api.KSApi
import com.teamtwothree.kartasvalokapp.db.KSDao
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.model.report.Report
import kotlinx.coroutines.Deferred
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.MultipartBody
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.File


class PointServiceImpl(override val kodein: Kodein) : PointService, KodeinAware {

    private val ksApi: KSApi by kodein.instance()
    private val ksDao: KSDao by kodein.instance()

    override fun getAllPoints() = ksApi.getAllPoints()
    override fun flushPoints() = ksApi.deleteAllPoints()
    override fun getPointDetails(id: String): Deferred<PointDetails> = ksApi.getPointDetails(id)
    override fun postReport(report: Report): Deferred<String> {
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
        return ksApi.postReport(body.setType(MediaType.get("multipart/form-data")).build())
    }
}