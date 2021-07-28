package com.me.rnotes.ui.home;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.me.rnotes.AudioPlayer;
import com.me.rnotes.MainActivity;
import com.me.rnotes.R;
import com.me.rnotes.RecordedNote;
import com.me.rnotes.ShortSoundWave;

import org.json.JSONException;

import java.io.File;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.Channel;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private AudioRecord audioRecord;
    private ArrayList<ShortSoundWave> audioData;

    private  String path;
    private TextView time_TextView;

    private int seconds, minutes, hours;
    private int MIN_BUFFER_LEN;
    private int NoteDuration = 5;
    private int MAX_DURATION_NUM = 25;
    private Timer timer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Determine notes path
        path = getContext().getExternalCacheDir().getAbsolutePath();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        time_TextView = root.findViewById(R.id.RecordingTime_tv);
        audioData = new ArrayList<>();

        // Set Spinner items
        ArrayList<String> sItems = new ArrayList<>();
        sItems.add("5 seconds");
        sItems.add("10 seconds");
        sItems.add("15 seconds");
        sItems.add("25 seconds");

        Spinner spinner = root.findViewById(R.id.period_spinner);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, sItems);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        NoteDuration = 5;
                        break;
                    case 1:
                        NoteDuration = 10;
                        break;
                    case 2:
                        NoteDuration = 15;
                        break;
                    case 3:
                        NoteDuration = 25;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Spinner","Nothing selected!");
            }
        });

        // Set Buttons Events
        root.findViewById(R.id.record_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StartRecording();
                    ImageButton button = (ImageButton) view;
                    button.setImageResource(R.drawable.mic);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void StartRecording() throws IOException, JSONException {
        if(audioRecord == null) {
            Initialize_MediaRecorder();
            TimeCount();
            Toast.makeText(getContext(), "Recording", Toast.LENGTH_SHORT).show();
        }
        else {
            // Initialize audio_data which will be saved into the device
            int note_dur;
            byte[] audio_data;

            if (audioData.size() > NoteDuration) {
                note_dur = NoteDuration;
                audio_data = new byte[MIN_BUFFER_LEN * 2 * note_dur];

                for (int n = audioData.size() - NoteDuration; n < audioData.size(); n++) {
                    byte[] temp = audioData.get(n).GetAudio();
                    for (int m = 0; m < MIN_BUFFER_LEN * 2; m++) {
                        audio_data[(n - audioData.size() + NoteDuration) * (MIN_BUFFER_LEN * 2) + m] = temp[m];
                    }
                }
            }
            else {
                note_dur = audioData.size();
                audio_data = new byte[MIN_BUFFER_LEN * 2 * note_dur];

                for (int n = 0; n < audioData.size(); n++) {
                    byte[] temp = audioData.get(n).GetAudio();
                    for (int m = 0; m < MIN_BUFFER_LEN * 2; m++) {
                        audio_data[n * (MIN_BUFFER_LEN * 2) + m] = temp[m];
                    }
                }
            }


            // Save the data in a new file
            SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-hh-mm-ss");
            String noteTitle = String.valueOf(sdf.format(new Date().getTime()));
            File noteFile = new File(path + "/" + noteTitle);

            FileOutputStream outputStream = new FileOutputStream(noteFile);
            outputStream.write(audio_data, 0, audio_data.length);
            outputStream.close();

            RecordedNote rn = new RecordedNote(noteTitle, note_dur, noteFile.getAbsolutePath());
            MainActivity.RecordedNotes.add(rn);
            MainActivity.NotifyDataChanged();

            Toast.makeText(getContext(), "Note saved. \br" + noteTitle, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize_MediaRecorder() throws IOException {

        MIN_BUFFER_LEN = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO
                , AudioFormat.ENCODING_PCM_16BIT) * 12;

        audioRecord = new AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(44100)
                        .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                        .build())
                .setBufferSizeInBytes(MIN_BUFFER_LEN * 2)
                .build();

        audioRecord.startRecording();
    }

    private void TimeCount() {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (seconds < 59) {
                    seconds += 1;
                }
                else {
                    seconds = 0;

                    if (minutes < 59) {
                        minutes += 1;
                    }
                    else {
                        minutes = 0;
                        hours += 1;
                    }
                }

                @SuppressLint("DefaultLocale") String timeString =
                        String.format("%02d", hours) + " : " + String.format("%02d", minutes)
                                + " : " + String.format("%02d", seconds);

                time_TextView.setText(timeString);

                byte[] temp = new byte[MIN_BUFFER_LEN * 2];
                audioRecord.read(temp, 0, MIN_BUFFER_LEN * 2);

                ShortSoundWave soundWave = new ShortSoundWave(temp);
                if (audioData.size() < MAX_DURATION_NUM) {
                    audioData.add(soundWave);
                }
                else {
                    audioData.add(soundWave);
                    audioData.remove(0);
                }
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        if (timer != null) {
           timer.cancel();
           timer.purge();
           timer = null;
        }
    }
}