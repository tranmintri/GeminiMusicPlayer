package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.util.List;

public class SongAdapterPlaylist extends  RecyclerView.Adapter<SongAdapterPlaylist.SongPlayListViewHolder>{
    private View view;
    private List<Song> songs;
    private Context context;


    private IClickItemListener iClickItemListener;


    public SongAdapterPlaylist(Context context, List<Song> songs, IClickItemListener iClickItemListener) {
        this.songs = songs;
        this.context = context;
        this.iClickItemListener = iClickItemListener;

        notifyDataSetChanged();
    }
    public interface  IClickItemListener{
        void onClickItem(int position);
        void onLongClickItem(int position);
    }


    @NonNull
    @Override
    public SongPlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_playlist,parent,false);
        return new SongPlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongPlayListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Song song = songs.get(position);
        if (song == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(song.getImage(), "drawable", context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imgAlbum.setImageURI(uri);
        holder.albumDetailMusicName.setText(song.getName());
        holder.albumArtistName.setText(song.getArtist().getName());


    }


    @Override
    public int getItemCount() {
        if(songs != null)
            return songs.size();
        return 0;
    }

    public class SongPlayListViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAlbum;
        private TextView albumDetailMusicName;
        private TextView albumArtistName;

        public SongPlayListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAlbum = itemView.findViewById(R.id.iv_imgsongPlayList);
            albumArtistName = itemView.findViewById(R.id.tv_songPlayListName);
            albumDetailMusicName = itemView.findViewById(R.id.tv_songPlayListArtist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iClickItemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            iClickItemListener.onClickItem(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (iClickItemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            iClickItemListener.onLongClickItem(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
}
