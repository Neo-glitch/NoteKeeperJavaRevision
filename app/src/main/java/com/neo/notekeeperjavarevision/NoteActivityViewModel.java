package com.neo.notekeeperjavarevision;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;


/**
 * ViewModel class used for NoteActivity and only solves prob on config changes and not sys cleanup prob
 */
public class NoteActivityViewModel extends ViewModel {
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.neo.notekeeperjavarevision.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.neo.notekeeperjavarevision.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.neo.notekeeperjavarevision.ORIGINAL_NOTE_TEXT";

    // var storing original values of note when entering this activity from click on a note
    public String mOriginalNoteCourseId;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;

    // true if ViewModel is a new instance
    public boolean mIsNewlyCreated = true;


    // saves viewModel fields in the bundle
    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    // restores the default val back and set to the ViewModel prop
    public void restoreState(Bundle inState){
        mOriginalNoteCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle = inState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = inState.getString(ORIGINAL_NOTE_TEXT);
    }
}
