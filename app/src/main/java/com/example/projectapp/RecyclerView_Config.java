package com.example.projectapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private SongsAdapter mSongsAdapter;

    public void SetConfig(RecyclerView recyclerView, Context context,List<song> songs,List<String> Keys ){
        mContext =context;
        mSongsAdapter = new SongsAdapter(songs, Keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mSongsAdapter);
    }

    class SongItemView extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mArtiste;
        private TextView mRelease;
        private TextView mcategorie;
        private String Key;

        public SongItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.liste_songs, parent, false));
            mTitle = (TextView) itemView.findViewById(R.id.title_txtView);
            mArtiste = (TextView) itemView.findViewById(R.id.artist_txtView);
            mRelease = (TextView) itemView.findViewById(R.id.release_txtView);
            mcategorie = (TextView) itemView.findViewById(R.id.Categori_txtView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(mContext, UpdateDeleteSongActivity.class);
                    intent.putExtra("key", Key);
                    intent.putExtra("artist",mArtiste.getText().toString().trim());
                    intent.putExtra("titre",mTitle.getText().toString().trim());
                    intent.putExtra("release",mRelease.getText().toString().trim());
                    intent.putExtra("categorie",mcategorie.getText().toString().trim());

                    mContext.startActivity(intent);
                }
            });
        }

        public void bind(song song, String Key) {
            mTitle.setText(song.getTitre());
            mArtiste.setText(song.getArtiste());
            mRelease.setText(song.getRéaliser());
            mcategorie.setText(song.getCatégorie());
            this.Key =Key;
        }
    }
    class SongsAdapter extends RecyclerView.Adapter<SongItemView>{
        private List<song> mSongList;
        private List<String> mKeys;

        public SongsAdapter(List<song> mSongList, List<String> mKeys) {
            this.mSongList = mSongList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public SongItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SongItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SongItemView holder, int position) {
            holder.bind(mSongList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mSongList.size();
        }
    }
}