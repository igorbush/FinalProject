package com.example.finalproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.finalproject.R;
import com.example.finalproject.common.AudioHelper;
import com.example.finalproject.common.AudioNotesViewAdapter;
import com.example.finalproject.common.ToastNotify;

import java.io.File;
import java.util.ArrayList;


public class AudioNotesFragment extends Fragment implements AudioNotesViewAdapter.CustomAdapterCallback {
    private FloatingActionButton recordingButton;
    private RecyclerView audioNotesListView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private AudioHelper audioHelper;
    private File pathToSave;
    private ImageButton previousTrackButton;
    private ArrayList<File> files;
    private final int REQUEST_PERMISSION_CODE = 1000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_notes, container, false);
        audioNotesListView = view.findViewById(R.id.audio_notes_list);
        audioNotesListView.setHasFixedSize(true);
        audioNotesListView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(view.getContext());
        recordingButton = view.findViewById(R.id.recording_button);

        if (!checkPermissionFromDevise()) requestPermission();
        audioHelper = new AudioHelper();
        pathToSave = getContext().getExternalFilesDir(null);
        pathToSave = new File(pathToSave.getAbsolutePath() + "/Audio Notes/");
        if (!pathToSave.exists()) {
            pathToSave.mkdirs();
        }
        files = new ArrayList<>();
        for (File file : pathToSave.listFiles()) {
            if (file.isFile()) files.add(file);
        }

        adapter = new AudioNotesViewAdapter(files, this);
        audioNotesListView.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                deleteAudioNote(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(audioNotesListView);

        audioNotesListView.setAdapter(adapter);
        recordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recordingButton.getTag().toString().equals("rec")) {
                    if (checkPermissionFromDevise()) {
                        recordingButton.setTag("stop");
                        recordingButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_mic_button));
                        audioHelper.startRecording(pathToSave.toString() + File.separator);
                        ToastNotify.showShortMsg(getContext(), getString(R.string.recording));
                    } else {
                        requestPermission();
                    }
                } else {
                    recordingButton.setTag("rec");
                    recordingButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_button));
                    audioHelper.stopRecording();
                    files = new ArrayList<>();
                    for (File file : pathToSave.listFiles()) {
                        if (file.isFile()) files.add(file);
                    }
                    ((AudioNotesViewAdapter) adapter).setFiles(files);
                    ToastNotify.showShortMsg(getContext(), getString(R.string.stop_recording));
                }
            }
        });


        return view;
    }


    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ToastNotify.showShortMsg(getContext(), "PERMISSION_GRANTED");
                } else {
                    ToastNotify.showShortMsg(getContext(), "PERMISSION_DENIED");
                }
                break;
            }
        }
    }

    public boolean checkPermissionFromDevise() {
        int writeExternalStorageResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordAudioInput = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
        return writeExternalStorageResult == PackageManager.PERMISSION_GRANTED && recordAudioInput == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onItemClick(int pos, View view) {
        ImageButton imageButton = (ImageButton) view;
        String tag = imageButton.getTag().toString();
        if (tag.equals("play")) {
            if (previousTrackButton != null) {
                previousTrackButton.setTag("play");
                previousTrackButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_button));
            }

            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_button));
            imageButton.setTag("stop");
            audioHelper.playPlayer(files.get(pos).toString());
            previousTrackButton = imageButton;
        } else {
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_button));
            imageButton.setTag("play");
            audioHelper.stopPlayer();
        }
    }

    public void deleteAudioNote(int i) {
        File fileToDelete = files.get(i);
        if (fileToDelete.exists()) {
            boolean isDelete = fileToDelete.delete();
            if (isDelete) {
                files = new ArrayList<>();
                for (File file : pathToSave.listFiles()) {
                    if (file.isFile()) files.add(file);
                }
                ((AudioNotesViewAdapter) adapter).setFiles(files);
            }
        }
    }
}