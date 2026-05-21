package com.example.on_thi_1_2pdf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    public interface OnSongClickListener {
        void onSongClick(Song song);
    }

    private final List<Song> songs = new ArrayList<>();
    private final OnSongClickListener onSongClickListener;

    public SongAdapter(OnSongClickListener onSongClickListener) {
        this.onSongClickListener = onSongClickListener;
    }

    public void setSongs(List<Song> songList) {
        songs.clear();
        songs.addAll(songList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.tvSongName.setText(song.getName());
        holder.tvSinger.setText(song.getSinger());
        holder.tvRating.setText(String.format(Locale.getDefault(), "%.2f", song.getRating()));

        holder.itemView.setOnClickListener(v -> onSongClickListener.onSongClick(song));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        final TextView tvSongName;
        final TextView tvSinger;
        final TextView tvRating;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvSinger = itemView.findViewById(R.id.tvSinger);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}

