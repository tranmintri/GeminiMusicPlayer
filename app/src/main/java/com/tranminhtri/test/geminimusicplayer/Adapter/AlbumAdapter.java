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
import com.tranminhtri.test.geminimusicplayer.models.Artist;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<Album> albums;
    private IClickItemListener iClickItemListener;
    private Context context;

    public interface  IClickItemListener{
        void onClickItem(Album album);
    }

    public AlbumAdapter(Context context,List<Album> albums, IClickItemListener listener) {
        this.albums = albums;
        this.context = context;
        this.iClickItemListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_home,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        int i = position;
        if (album == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(album.getImage(), "drawable",context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imageView.setImageURI(uri);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iClickItemListener.onClickItem(album);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(albums != null)
            return albums.size();
        return 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_radio_popular);

        }
    }
}
