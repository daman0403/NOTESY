package com.example.android.notesy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "NotesPrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";
    private LinearLayout notesContainer;
    private List<Notes> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesContainer = findViewById(R.id.notesContainer);
        Button saveButton = findViewById(R.id.saveButton);

        notesList = new ArrayList<>();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        loadNoteFromPrefrences();
        displayNotes();

    }

    private void displayNotes() {
        for(Notes note: notesList){
            createNoteView(note);
        }
    }

    private void loadNoteFromPrefrences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT, 0);
        for(int i = 0; i< noteCount; i++){
            String tittle = sharedPreferences.getString("note_tittle_"+i, "");
            String content = sharedPreferences.getString("note_content_"+i, "");

            Notes note = new Notes();
            note.setTittle(tittle);
            note.setContent(content);

            notesList.add(note);
        }
    }

    private void saveNote(){
        EditText tittleEditText = findViewById(R.id.tittleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        String tittle = tittleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if(!tittle.isEmpty() && !content.isEmpty()){
            Notes note = new Notes();

            note.setTittle(tittle);
            note.setContent(content);

            notesList.add(note);
            saveNoteToPrefrence();

            createNoteView(note);
            clearInpuFields();
        }

    }

    private void clearInpuFields() {
        EditText tittleEditText = findViewById(R.id.tittleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        tittleEditText.getText().clear();
        contentEditText.getText().clear();

    }

    private  void createNoteView(final Notes note){
        View noteView = getLayoutInflater().inflate(R.layout.note_item,null);
        TextView tittleTextView = noteView.findViewById(R.id.tittleTextView);
        TextView contentTextView = noteView.findViewById(R.id.contentEditText);

        tittleTextView.setText(note.getTittle());
        contentTextView.setText(note.getContent());

        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                showDeleteDialogue(note);
                return true;
            }
        });

        notesContainer.addView(noteView);
    }

    private void showDeleteDialogue(final Notes note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteNotesAndRefresh(note);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteNotesAndRefresh(Notes note) {
        notesList.remove(note);
        saveNoteToPrefrence();
        refreshNoteView();
    }

    private void refreshNoteView() {
        notesContainer.removeAllViews();
        displayNotes();
    }

    private void saveNoteToPrefrence(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT, notesList.size());

        for(int i = 0; i< notesList.size(); i++){
            Notes note = notesList.get(i);
            editor.putString("note_tittle_" + i, note.getTittle());
            editor.putString("note_content_" +i, note.getContent());
        }
        editor.apply();
    }

}