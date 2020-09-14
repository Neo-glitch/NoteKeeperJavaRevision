package com.neo.notekeeperjavarevision;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

// added manually
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.*;



@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    static DataManager sDataManager;

    @BeforeClass
    public static void classSetUp(){
        sDataManager = DataManager.getInstance();
    }

    // activity to run test with
    @Rule
    public ActivityTestRule<NoteListActivity> mNoteListActivityActivityTestRule = new ActivityTestRule<>(NoteListActivity.class);

    @Test
    public void createNewNote(){
        // gets View(used only when we want to perform a number of things on a View matched)
//        ViewInteraction fabNewNote = onView(withId(R.id.fab));
//        fabNewNote.perform(click());

        final CourseInfo course = sDataManager.getCourse("java_lang");
        String noteTitle = "Test note Title";
        String noteText = "This is the body of my test note";

        onView(withId(R.id.fab)).perform(click());

        // finds through the adapterView(spinner) and find the one holding class obj of courseInfo and == course # e.g of using hamcrest matcher
        onData(allOf(instanceOf(CourseInfo.class), equalTo(course))).perform(click()).perform(click());
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(course.getTitle()))));

        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle))
                .check(matches(withText(containsString(noteTitle))));

        onView(withId(R.id.text_note_text)).perform(typeText(noteText), closeSoftKeyboard());
        onView(withId(R.id.text_note_text)).check(matches(withText(containsString(noteText))));

        pressBack();

        // check that new note in list of notes matches with value passed in text
        int noteIndex = sDataManager.getNotes().size() - 1;
        NoteInfo note = sDataManager.getNotes().get(noteIndex);

        assertEquals(course, note.getCourse());
        assertEquals(noteTitle, note.getTitle());
        assertEquals(noteText, note.getText());

    }

}