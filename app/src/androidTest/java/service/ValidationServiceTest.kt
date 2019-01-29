package service

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.service.ValidationService
import com.teamtwothree.kartasvalokapp.service.FirebaseValidationService
import com.teamtwothree.kartasvalokapp.service.ValidationState
import kotlinx.coroutines.runBlocking
import org.junit.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ValidationServiceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val service : ValidationService = FirebaseValidationService()

    @Test
    fun returnFailedOnAlreadyReportedCloserThan200() =
        runBlocking {
            val data = service.isNotAlreadyReported( 53.206539, 45.067333)
            val result = getResultOrNull(data)
            Assert.assertTrue(result == ValidationState.FAILED)
        }
    @Test
    fun returnSuccessOnAlreadyReportedFartherThan200() =
        runBlocking {
            val data = service.isNotAlreadyReported( 53.225872, 45.061399)
            val result = getResultOrNull(data)
            Assert.assertTrue(result == ValidationState.SUCCESS)
        }

    @Test
    fun returnFailedOnSanctionedCloserThan200() =
        runBlocking {
            val data = service.isUnsanctioned( 54.6478, 21.0766)
            val result = getResultOrNull(data)
            Assert.assertTrue(result == ValidationState.FAILED)
        }
    @Test
    fun returnSuccessOnSanctionedFartherThan200() =
        runBlocking {
            val data = service.isUnsanctioned( 54.648596, 21.081807)
            val result = getResultOrNull(data)
            Assert.assertTrue(result == ValidationState.SUCCESS)
        }

    private fun <T> getResultOrNull(data: LiveData<T>): T? {
        var result: T? = null
        val latch = CountDownLatch(1)
        data.observeForever {
            it?.apply {
                result = it
                Log.d("VALIDATION_TEST: ", result.toString())
                if(it != ValidationState.VALIDATING) {
                    latch.countDown()
                }
            }
        }
        latch.await(10, TimeUnit.SECONDS)
        return result
    }

}