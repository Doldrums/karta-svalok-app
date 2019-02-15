package service

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.teamtwothree.kartasvalokapp.service.ValidationService
import com.teamtwothree.kartasvalokapp.service.validation.FirebaseValidationService
import com.teamtwothree.kartasvalokapp.service.common.OperationState
import kotlinx.coroutines.runBlocking
import org.junit.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule



class ValidationServiceTest {

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val service : ValidationService = FirebaseValidationService()

    @Test
    fun returnFailedOnAlreadyReportedCloserThan200() =
        runBlocking {
            val data = service.isNotAlreadyReported( 53.206539, 45.067333)
            val result = getResultOrNull(data)
            Assert.assertTrue(result == OperationState.FAILED)
        }
    @Test
    fun returnSuccessOnAlreadyReportedFartherThan200() =
        runBlocking {
            val data = service.isNotAlreadyReported( 53.225872, 45.061399)
            val result = getResultOrNull(data)
            Assert.assertTrue(result == OperationState.SUCCESS)
        }

    @Test
    fun returnFailedOnSanctionedCloserThan200() =
        runBlocking {
            val data = service.isUnsanctioned( 54.6478, 21.0766)
            val result = getResultOrNull(data)
            Assert.assertTrue(result == OperationState.FAILED)
        }
    @Test
    fun returnSuccessOnSanctionedFartherThan200() =
        runBlocking {
            val data = service.isUnsanctioned( 54.648596, 21.081807)
            val result = getResultOrNull(data)
            Assert.assertTrue(result == OperationState.SUCCESS)
        }

    private fun <T> getResultOrNull(data: LiveData<T>): T? {
        var result: T? = null
        val latch = CountDownLatch(1)
        data.observeForever {
            it?.apply {
                result = it
                Log.d("VALIDATION_TEST: ", result.toString())
                if(it != OperationState.VALIDATING) {
                    latch.countDown()
                }
            }
        }
        latch.await(120, TimeUnit.SECONDS)
        return result
    }
    @Test
    fun getLabelsForImage() =
        runBlocking {
            val list = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list()
            val path =  "file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/171006-443-to7bN83pAcHC3CgI.JPG"
            val imageUri = Uri.parse(path)
            val data = service.imageContainsDump(listOf(imageUri))
            val result = getResultOrNull(data)
            Assert.assertTrue(result!! == OperationState.SUCCESS)
        }



}