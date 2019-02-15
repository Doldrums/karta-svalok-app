package service

import android.R
import android.view.ViewGroup
import android.widget.FrameLayout
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.service.DataService
import com.teamtwothree.kartasvalokapp.service.ReportService
import com.teamtwothree.kartasvalokapp.service.ValidationService
import org.kodein.di.generic.instance


/**
 * Used as container to test fragments in isolation with Espresso
 */
@RestrictTo(RestrictTo.Scope.TESTS)
class SingleFragmentActivity : AppCompatActivity() {

    private val validationService: ValidationService by AppDelegate.getKodein().instance()
    private val dataService: DataService by AppDelegate.getKodein().instance()
    val reportService: ReportService by AppDelegate.getKodein().instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = FrameLayout(this)
        content.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        content.id = R.id.content
        setContentView(content)
        reportService.newReport(emptyList())
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.content, fragment, "TEST")
            .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment).commit()
    }
}