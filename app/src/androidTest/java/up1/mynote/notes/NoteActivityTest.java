package up1.mynote.notes;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import up1.mynote.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.intent.Checks.checkArgument;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class NoteActivityTest {

    @Rule
    public ActivityTestRule<NoteActivity> mNoteActivityActivityTestRule = new ActivityTestRule<>(NoteActivity.class);


    @Test
    public void clickAddNewNoteButton_should_openAddNotePage() {
        onView(withId(R.id.fab_add_notes)).perform(click());
        onView(withId(R.id.add_note_title)).check(matches(isDisplayed()));
    }

    @Test
    public void addNewNote_should_showInNoteList() {
        String newNoteTitle = "Hello Espresso";
        String newNoteDescription = "UI Testing for Android app";

        //Click add new note button
        onView(withId(R.id.fab_add_notes)).perform(click());

        //Fill in data
        onView(withId(R.id.add_note_title)).perform(typeText(newNoteTitle));
        onView(withId(R.id.add_note_description)).perform(typeText(newNoteDescription));

        //Save note
        onView(withId(R.id.fab_add_notes)).perform(click());

        //Scroll to new note
        onView(withId(R.id.notes_list)).perform(scrollTo(hasDescendant(withText(newNoteDescription))));

        //Verify data
        onView(withItemText(newNoteDescription)).check(matches(isDisplayed()));
    }

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }
}