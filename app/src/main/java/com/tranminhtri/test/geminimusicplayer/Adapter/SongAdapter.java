package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_BottomDialog_AddSong;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_Edit_Profile;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.util.List;

public class SongAdapter extends  RecyclerView.Adapter<SongAdapter.SongViewHolder>{
    private View view;
    private List<Song> songs;
    private Context context;


    private IClickItemListener iClickItemListener;
    private OnclickItem onclickItem;

    public SongAdapter(Context context,List<Song> songs, IClickItemListener iClickItemListener, OnclickItem onclickItem) {
        this.songs = songs;
        this.context = context;
        this.onclickItem = onclickItem;
        this.iClickItemListener = iClickItemListener;
        notifyDataSetChanged();
    }
    public interface  IClickItemListener{
        void onClickItem(int position);
    }
    public interface  OnclickItem{
        void onClick(int position,View view);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Song song = songs.get(position);
        if (song == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(song.getImage(), "drawable", context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imgAlbum.setImageURI(uri);
        holder.albumDetailMusicName.setText(song.getName());
        holder.albumArtistName.setText(song.getArtist().getName());
//        holder.tv_more_horiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(v);
//            }
//        });



    }


    @Override
    public int getItemCount() {
        if(songs != null)
            return songs.size();
        return 0;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAlbum;
        private TextView albumDetailMusicName;
        private TextView albumArtistName;
        private  TextView tv_more_horiz;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAlbum = itemView.findViewById(R.id.iv_ImageDetailAlbum);
            albumArtistName = itemView.findViewById(R.id.tv_AlbumArtistName);
            albumDetailMusicName = itemView.findViewById(R.id.tv_AlbumDetailMusicName);
            tv_more_horiz = itemView.findViewById(R.id.tv_more_horiz);
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
            tv_more_horiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclickItem != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onclickItem.onClick(position,v);
                        }
                    }
                }
            });

        }
    }
}
