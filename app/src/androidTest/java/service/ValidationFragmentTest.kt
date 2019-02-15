package service

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.teamtwothree.kartasvalokapp.presentation.validation.ValidationFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidationFragmentTest {

    val testActivityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    @Rule
    fun rule() = testActivityRule

    @Before
    fun setUp() {

        rule().activity.setFragment(ValidationFragment())
    }
}