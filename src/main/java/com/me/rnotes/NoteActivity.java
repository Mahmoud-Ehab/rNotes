package com.me.rnotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Timer;
import java.util.TimerTask;

public class NoteActivity extends AppCompatActivity {

    private RecordedNote recordedNote;
    private int rnIndex;
    private AudioPlayer audioPlayer;

    private EditText editText;

    private byte[] audioData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Get ExtraData from intent
        Intent intent = getIntent();
        rnIndex = intent.getExtras().getInt("index");
        recordedNote = MainActivity.RecordedNotes.get(rnIndex);

        // Set ExtraData in the views
        editText = (EditText) findViewById(R.id.note_title);
        editText.setText(recordedNote.getTitle());

        TextView textView = (TextView) findViewById(R.id.progress_tv);
        textView.setText(recordedNote.getDuration() + " seconds");

        // Load audioData
        File file = new File(recordedNote.getPath());
        if (file.exists()) {
            audioData = new byte[(int) file.length()];
        }
        else {
            audioData = null;
        }

        try {

            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(audioData, 0, (int) file.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PlayNoteAudio(View view) {
        final ImageButton imageButton = (ImageButton) view;

        if (audioData == null) {
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (audioPlayer == null) {
            audioPlayer = new AudioPlayer(audioData);
            audioPlayer.Play();
            imageButton.setImageResource(R.drawable.media_pause);
        }
        else {
            StopNoteAudio(imageButton);
        }
    }

    private void StopNoteAudio(ImageButton imageButton) {
        audioPlayer.Stop();
        audioPlayer = null;
        imageButton.setImageResource(R.drawable.media_play);
    }

    public void Save(View view) throws IOException, JSONException {
        String temp = editText.getText().toString();
        recordedNote.setTitle(temp);

        Toast.makeText(this, "Title saved.", Toast.LENGTH_SHORT).show();
        MainActivity.NotifyDataChanged();
    }

    public void Delete(View view) throws IOException, JSONException {
        MainActivity.RecordedNotes.remove(rnIndex);

        File file = new File(recordedNote.getPath());
        file.delete();

        MainActivity.NotifyDataChanged();
        finish();
    }
}