package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;

import java.util.List;

public class PlayListLibraryAdapter extends RecyclerView.Adapter<PlayListLibraryAdapter.PlayListtLibraryViewHolder>{
    private Context context;
    private List<Playlist> playlists;
    private OnItemClickListener mListener;

    public PlayListLibraryAdapter(Context context, List<Playlist> playlists, OnItemClickListener listener) {
        this.context = context;
        this.playlists = playlists;
        mListener = listener;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }


    @NonNull
    @Override
    public PlayListtLibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_library,parent,false);
        return new PlayListtLibraryViewHolder(view);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    @Override
    public void onBindViewHolder(@NonNull PlayListtLibraryViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        if (playlist == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier("musicicon", "drawable",context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imgArtist.setImageURI(uri);
        holder.tvName.setText(playlist.getName());
        holder.tvType.setText("Danh sách phát * "+ playlist.getUser().getFullname());

    }

    @Override
    public int getItemCount() {
        if(playlists != null){

            return playlists.size();
        }
        return 0;
    }
    public class PlayListtLibraryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgArtist;
        private TextView tvName;
        private TextView tvType;
        public PlayListtLibraryViewHolder(@NonNull View itemView) {
            super(itemView);

            imgArtist = itemView.findViewById(R.id.civ_ImageArtist);
            tvName = itemView.findViewById(R.id.tv_NameArtist);
            tvType = itemView.findViewById(R.id.tv_TypeArtist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemLongClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
}
