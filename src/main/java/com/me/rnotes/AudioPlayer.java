package com.me.rnotes;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioPlayer {

    private byte[] audio;
    private AudioTrack audioTrack;

    public AudioPlayer(byte[] audio) {
        this.audio = audio;

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        AudioFormat format = new AudioFormat.Builder()
                .setSampleRate(44100)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build();


        audioTrack = new AudioTrack(attributes, format, audio.length, AudioTrack.MODE_STATIC, AudioManager.AUDIO_SESSION_ID_GENERATE);
        audioTrack.write(audio, 0, audio.length);
    }

    public void Play() {
        audioTrack.play();
    }
    public void Stop() { audioTrack.stop(); audioTrack.release(); }
}
