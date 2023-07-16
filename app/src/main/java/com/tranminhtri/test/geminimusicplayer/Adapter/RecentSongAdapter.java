package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.RecentSong;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.util.List;

public class RecentSongAdapter extends RecyclerView.Adapter<RecentSongAdapter.RecentSongViewHolder> {
    private List<Song> songs;
    private IClickItemListener iClickItemListener;
    private Context context;

    public interface  IClickItemListener{
        void onClickItem(Song song);
    }

    public RecentSongAdapter(Context context, List<Song> songs, IClickItemListener listener) {
        this.songs = songs;
        this.context = context;
        this.iClickItemListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecentSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_home,parent,false);
        return new RecentSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSongViewHolder holder, int position) {
        Song song = songs.get(position);
        int i = position;
        if (song == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(song.getImage(), "drawable",context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imageView.setImageURI(uri);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iClickItemListener.onClickItem(song);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(songs != null)
            return songs.size();
        return 0;
    }

    public class RecentSongViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public RecentSongViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_radio_popular);

        }
    }
}
