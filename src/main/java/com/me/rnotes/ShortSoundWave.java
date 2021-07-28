package com.me.rnotes;

public class ShortSoundWave {

    private byte[] audio;

    public ShortSoundWave(byte[] audio) {
        this.audio = audio;
    }

    public byte[] GetAudio() {
        return audio;
    }
}
