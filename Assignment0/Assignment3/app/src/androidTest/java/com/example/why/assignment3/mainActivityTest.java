package com.example.why.assignment3;

        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.support.v4.widget.DrawerLayout;

        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import static android.support.test.espresso.Espresso.onData;
        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.action.ViewActions.click;
        import static android.support.test.espresso.assertion.ViewAssertions.matches;
        import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
        import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;
        import static android.support.test.espresso.matcher.ViewMatchers.withText;
        import static org.hamcrest.Matchers.allOf;
        import static org.hamcrest.Matchers.anything;

/** Tests related to interacting with {@link DrawerLayout}  */
@RunWith (AndroidJUnit4.class)
public class mainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    //test profile if all things loading right
    @Test
    public void testBProfile(){
        onView(withText("Profile")).perform(click());
        onView(allOf(withId(R.id.profile_repos), withText("https://github.com/yaxtang"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.profile_name), withText("yaxtang"))).check(matches(isDisplayed()));
    }

    /**
     * Test following and follow together to see if it workds
     */
    @Test
    public void testCFollowing() {
        onView(withText("Following")).perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.listview_following))).atPosition(0)
                .onChildView(allOf(withId(R.id.list_item_username),withText("hwu63"))).check(matches(isDisplayed()));
        onData(anything())
                .inAdapterView(allOf(withId(R.id.listview_following))).atPosition(0)
                .onChildView(withId(R.id.list_item_follower_unfollow))
                .perform(click());
    }
    @Test
    public void testDFollowers() {
        onView(withText("Following")).perform(click());
        onView(withText("Followers")).perform(click());

        // onView(withId(R.id.avatar)).check(matches(isClickable()));
        onView(allOf(withId(R.id.username), withText("hwu63"))).check(matches(isDisplayed()));
        onData(anything())
                .inAdapterView(allOf(withId(R.id.listview_follow))).atPosition(2)
                .onChildView(allOf(withId(R.id.username), withText("hwu63")))
                .check(matches(isDisplayed()));
        onData(anything())
                .inAdapterView(allOf(withId(R.id.listview_follow))).atPosition(0)
                .onChildView(allOf(withId(R.id.username), withText("xycFromUIUC")))
                .check(matches(isDisplayed()));
        onData(anything())
                .inAdapterView(allOf(withId(R.id.listview_follow))).atPosition(0)
                .onChildView(allOf(withId(R.id.list_item_follower_avatar)))
                .check(matches(isClickable()));
        onData(anything())
                .inAdapterView(allOf(withId(R.id.listview_follow))).atPosition(2)
                .onChildView(allOf(withId(R.id.list_item_follower_unfollow)))
                .perform(click());
        onView(withText("Following")).perform(click());
        onView(allOf(withId(R.id.username), withText("hwu63"))).check(matches(isDisplayed()));
    }

    @Test
    public void testEPublic() {
        onView(withText("Public Repositories")).perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.listview))).atPosition(0)
                .onChildView(allOf(withId(R.id.icon)))
                .perform(click());
        onData(anything())
                .inAdapterView(allOf(withId(R.id.listview))).atPosition(3)
                .onChildView(allOf(withId(R.id.icon)))
                .perform(click());
    }
}

