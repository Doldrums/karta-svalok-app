package service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider

import com.teamtwothree.kartasvalokapp.db.KSDao
import com.teamtwothree.kartasvalokapp.db.KSDatabase
import com.teamtwothree.kartasvalokapp.di.networkModule
import com.teamtwothree.kartasvalokapp.model.report.Report
import com.teamtwothree.kartasvalokapp.model.user.UserInfo
import com.teamtwothree.kartasvalokapp.service.DataService
import com.teamtwothree.kartasvalokapp.service.FirebaseDataService
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class DataServiceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var kodein: Kodein
    lateinit var dataService: DataService

    val report = Report(
        "тема", "описание", "адрес",
        "fgs", "f@d.s", "123", "регион", "1", "1",
        "54.72263743392473,20.502462387084964",
        emptyList()
    )

    val userInfo = UserInfo("Test", "test@test.test", "1234567890")

    @Before
    fun setup() {
        kodein = Kodein {
            import(networkModule)
            bind<KSDatabase>() with singleton {
                Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), KSDatabase::class.java)
                    .allowMainThreadQueries().build()
            }
            bind<KSDao>() with singleton { instance<KSDatabase>().getKSDao() }
        }
        dataService = FirebaseDataService(kodein)
    }

    @After
    fun finish() {
        dataService.flushPoints().execute()
    }

    @Test
    fun postReport() =
        runBlocking {
            val id = getResultOrNull(dataService.postReport(report))
            Assert.assertTrue(!id.isNullOrBlank())
        }

    @Test
    fun getReport() =
        runBlocking {
            val id = getResultOrNull(dataService.postReport(report))
            val pointDetails = id?.let { getResultOrNull(dataService.getPointDetails(id)) }
            Assert.assertTrue(pointDetails?.id == id)
        }

    @Test
    fun getAll() =
        runBlocking {
            val id = getResultOrNull(dataService.postReport(report))
            val list = getResultOrNull(dataService.getAllPointDetails())
            Assert.assertTrue(list?.get(0)?.id == id)
        }

    @Test
    fun saveAndGetUser() =
        runBlocking {
            dataService.saveUserInfo(userInfo)
            val savedUserInfo = getResultOrNull(dataService.getUserInfo())
            Assert.assertTrue(savedUserInfo?.name == userInfo.name)
        }

    private fun <T> getResultOrNull(data: LiveData<T>): T? {
        var result: T? = null
        val latch = CountDownLatch(1)
        data.observeForever {
            it?.apply {
                result = it
                latch.countDown()
            }
        }
        latch.await(10, TimeUnit.SECONDS)
        return result
    }
}