package up1.mynote.addnote;

import android.app.Activity;
import android.provider.MediaStore;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import up1.mynote.R;

import static android.app.Instrumentation.ActivityResult;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AddNoteScreenTest {

    @Rule
    public IntentsTestRule<AddNoteActivity> mAddNoteActivityIntentsTestRule = new IntentsTestRule<>(AddNoteActivity.class);

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(mAddNoteActivityIntentsTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void addImageToNewNote_should_showInScreen() {
        ActivityResult result = new ActivityResult(Activity.RESULT_OK, null);
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        //Initial screen of add new note :: image should not display
        onView(withId(R.id.add_note_image_thumbnail)).check(matches(not(isDisplayed())));

        //Select menu Take a photo
        openActionBarOverflowOrOptionsMenu(getTargetContext());
        onView(withText(R.string.take_picture)).perform(click());

        //Verify image on screen
        onView(withId(R.id.add_note_image_thumbnail)).perform(scrollTo()).check(matches(allOf(hasDrawable(), isDisplayed())));

    }

    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mAddNoteActivityIntentsTestRule.getActivity().getCountingIdlingResource());
    }

}