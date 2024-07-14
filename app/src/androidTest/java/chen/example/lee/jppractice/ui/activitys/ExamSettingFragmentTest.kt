package chen.example.lee.jppractice.ui.activitys


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.lee.jppractice.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ExamSettingFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun examSettingFragmentTest() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.examSettingFragment), withContentDescription("測驗"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigation),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val button = onView(
            allOf(
                withId(R.id.practicetriggerButton), withText("開始練習"),
                withParent(withParent(withId(R.id.navHostFragment))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val linearLayout = onView(
            allOf(
                withId(R.id.wordPicker),
                withParent(withParent(withId(R.id.navHostFragment))),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val linearLayout2 = onView(
            allOf(
                withId(R.id.tonePicker),
                withParent(withParent(withId(R.id.navHostFragment))),
                isDisplayed()
            )
        )
        linearLayout2.check(matches(isDisplayed()))

        val linearLayout3 = onView(
            allOf(
                withId(R.id.countPicker),
                withParent(withParent(withId(R.id.navHostFragment))),
                isDisplayed()
            )
        )
        linearLayout3.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.practicetriggerButton), withText("開始練習"),
                withParent(withParent(withId(R.id.navHostFragment))),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
