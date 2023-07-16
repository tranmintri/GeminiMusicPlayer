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
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {
    private List<Playlist> playlists;
    private IClickItemListener iClickItemListener;
    private Context context;

    public interface  IClickItemListener{
        void onClickItem(Playlist playlist);
    }

    public PlayListAdapter(Context context, List<Playlist> playlists, IClickItemListener listener) {
        this.playlists = playlists;
        this.context = context;
        this.iClickItemListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_home,parent,false);
        return new PlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        int i = position;
        if (playlist == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier("musicicon", "drawable",context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imageView.setImageURI(uri);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iClickItemListener.onClickItem(playlist);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(playlists != null)
            return playlists.size();
        return 0;
    }

    public class PlayListViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public PlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_radio_popular);

        }
    }
}
