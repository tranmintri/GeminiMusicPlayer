package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;

import java.util.List;

public class PlayListAddSongAdapter extends RecyclerView.Adapter<PlayListAddSongAdapter.PlayListAddSongLibraryViewHolder>{
    private Context context;
    private List<Playlist> playlists;
    private OnItemClickListener mListener;

    public PlayListAddSongAdapter(Context context, List<Playlist> playlists, OnItemClickListener listener) {
        this.context = context;
        this.playlists = playlists;
        mListener = listener;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(int position,boolean isChecked);
    }


    @NonNull
    @Override
    public PlayListAddSongLibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        return new PlayListAddSongLibraryViewHolder(view);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    @Override
    public void onBindViewHolder(@NonNull PlayListAddSongLibraryViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        if (playlist == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier("musicicon", "drawable",context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imgArtist.setImageURI(uri);
        holder.tvName.setText(playlist.getName());
        holder.tvType.setText("Danh sách phát * "+ playlist.getUser().getUsername());




    }

    @Override
    public int getItemCount() {
        if(playlists != null){

            return playlists.size();
        }
        return 0;
    }
    public class PlayListAddSongLibraryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgArtist;
        private TextView tvName;
        private TextView tvType;
        public CheckBox checkBox;
        public PlayListAddSongLibraryViewHolder(@NonNull View itemView) {
            super(itemView);

            imgArtist = itemView.findViewById(R.id.iv_ImagePlaylist);
            tvName = itemView.findViewById(R.id.tv_playlistName);
            tvType = itemView.findViewById(R.id.tv_playlistTitle);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(position, isChecked);
                    }
                }
            });

        }
    }
}
