package up1.mynote.notedetail;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import up1.mynote.R;
import up1.mynote.data.Note;
import up1.mynote.data.NoteRepository;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static up1.mynote.addnote.AddNoteScreenTest.hasDrawable;

@RunWith(AndroidJUnit4.class)
public class NoteDetailScreenTest {

    @Rule
    public ActivityTestRule<NoteDetailActivity> mNoteDetailActivityTestRule = new ActivityTestRule<>(NoteDetailActivity.class, true, false);

    public static final String TITLE = "TEST";
    public static final String DESCRIPTION = "TEST DESC";
    private static Note stubNote = new Note(TITLE, DESCRIPTION, "file:///android_asset/ic_launcher.png");

    @Before
    public void initialDataOfNote() {
        NoteRepository.getInstance().saveNote(stubNote);

        Intent startIntent = new Intent();
        startIntent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, stubNote.getId());
        mNoteDetailActivityTestRule.launchActivity(startIntent);

        registerIdlingResource();
    }

    @Test
    public void detailOfNote_should_shownOnUScreen() {
        onView(withId(R.id.note_detail_title)).check(matches(withText(TITLE)));
        onView(withId(R.id.note_detail_description)).check(matches(withText(DESCRIPTION)));
        onView(withId(R.id.note_detail_image)).check(matches(allOf(
                hasDrawable(),
                isDisplayed())));
    }

    private void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mNoteDetailActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mNoteDetailActivityTestRule.getActivity().getCountingIdlingResource());
    }

}