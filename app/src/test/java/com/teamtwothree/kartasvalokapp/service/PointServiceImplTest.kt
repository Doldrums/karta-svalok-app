package com.teamtwothree.kartasvalokapp.service

import com.teamtwothree.kartasvalokapp.di.networkModule
import com.teamtwothree.kartasvalokapp.model.report.Report
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein

class PointServiceImplTest {

    lateinit var pointService : PointService
    val report = Report(
        "тема", "описание", "адрес",
        "fgs", "f@d.s", "123", "регион", "1", "1",
        "54.72263743392473,20.502462387084964",
        emptyList()
    )
    @Before
    fun setup() {
        val kodein: Kodein = Kodein {
            import(networkModule)
        }
        pointService = PointServiceImpl(kodein)
    }

    @After
    fun finish() {
        pointService.flushPoints().execute()
    }

    @Test
    fun postReport() {
        runBlocking {
            val id = pointService.postReport(report).await()
            Assert.assertTrue(!id.isEmpty())
        }
    }

    @Test
    fun getReport() {
        runBlocking {
            val id = pointService.postReport(report).await()
            val pointDetails = pointService.getPointDetails(id).await()
            Assert.assertTrue(pointDetails.id == id)
        }
    }
}