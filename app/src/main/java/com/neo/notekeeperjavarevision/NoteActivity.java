package com.neo.notekeeperjavarevision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_POSITION = "com.neo.notekeeperjavarevision.NOTE_POSITION";
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private int POSITION_NOT_SET;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private Spinner mSpinnerCourses;

    // to store note position of new Note
    private int mNotePosition;
    // true when the cancel menu item is tapped
    private boolean mIsCancelling;

    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        // logic ensures that we only restore state on sys cleanup(ViewModel is newly created) and not configChange
        if (savedInstanceState != null && mViewModel.mIsNewlyCreated) {
            mViewModel.restoreState(savedInstanceState);
        }

        mViewModel.mIsNewlyCreated = false;
        mSpinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        // init the array adapter for the spinner
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        saveOriginalNoteValues();

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        if (!mIsNewNote) {
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mViewModel.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    // used to store original note values when we enter this activity in var, and if user cancels out we just set it back to
    // default var or original note values and save in the prop in the ViewModel fields
    private void saveOriginalNoteValues() {
        if (mIsNewNote)
            return;

        mViewModel.mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();


    }

    @Override
    protected void onPause() {
        if (mIsCancelling) {
            if (mIsNewNote) {
                DataManager.getInstance().removeNote(mNotePosition);
            } else {
                storePreviousNoteValues();
            }
        } else {
            saveNote();
        }
        super.onPause();
    }


    // sets the noteValues back to original note Values and store since dm is singleton
    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);
    }


    private void saveNote() {
        // saves the note , since dm class has one instance created until app closes
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    // pop the View in this activity with needed values
    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(mNote.getCourse());

        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    // gets intent extras passed to this activity if any
    private void readDisplayStateValues() {
        Intent intent = getIntent();
        POSITION_NOT_SET = -1;
        mNotePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = mNotePosition == POSITION_NOT_SET;
        if (mIsNewNote) {
            createNewNote();
        }
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);


    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        // gets pos of new note
        mNotePosition = dm.createNewNote();
//        mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    // sends email using implicit note
    private void sendEmail() {
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "checkout what I learnt in this noteKeeper course \""
                + course.getTitle() + "\"\n" + mTextNoteText.getText().toString();

        // implicit intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");                          // mime Type for email
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);             // subject of email
        intent.putExtra(Intent.EXTRA_TEXT, text);                   // text of the mail or body
        startActivity(intent);


    }
}