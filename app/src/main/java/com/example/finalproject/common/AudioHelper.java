package com.example.finalproject.common;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioHelper {
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    public AudioHelper() {
    }

    public void setupMediaRecorder(String pathSave) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(pathSave);
    }

    public void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playPlayer(String pathSave) {
        stopPlayer();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(pathSave);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
        }
    }

    public void startRecording(String pathSave) {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String path = pathSave + formatForDateNow.format(dateNow) + ".mp3";
        this.setupMediaRecorder(path);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
