package com.example.finalproject.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finalproject.R;

import java.io.File;
import java.util.List;

public class AudioNotesViewAdapter extends RecyclerView.Adapter<AudioNotesViewAdapter.AudioNotesViewHolder> {
    private List<File> arrayList;
    private final CustomAdapterCallback listener;

    public AudioNotesViewAdapter(List<File> arrayList, CustomAdapterCallback listener) {
        this.listener = listener;
        this.arrayList = arrayList;
    }

    public void setFiles(List<File> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public static class AudioNotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CustomAdapterCallback listener;
        TextView audioNoteTitle;
        ImageButton playStopButton;

        public AudioNotesViewHolder(@NonNull View itemView, CustomAdapterCallback listener) {
            super(itemView);
            this.listener = listener;
            playStopButton = itemView.findViewById(R.id.audio_play_stop_button);
            audioNoteTitle = itemView.findViewById(R.id.audio_note_title);
            this.playStopButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition(), view);
        }
    }

    public interface CustomAdapterCallback {
        void onItemClick(int pos, View view);
    }

    @NonNull
    @Override
    public AudioNotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layout = R.layout.audio_notes_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, viewGroup, false);
        AudioNotesViewAdapter.AudioNotesViewHolder audioNotesViewHolder = new AudioNotesViewHolder(view, listener);
        return audioNotesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioNotesViewHolder audioNotesViewHolder, int i) {
        File track = arrayList.get(i);
        audioNotesViewHolder.audioNoteTitle.setText(track.getName());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
