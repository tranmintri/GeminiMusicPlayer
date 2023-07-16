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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private List<Artist> artists;
    private IClickItemListener iClickItemListener;
    private Context context;

    public interface  IClickItemListener{
        void onClickItem(Artist artist);
    }

    public ArtistAdapter(Context context,List<Artist> artists, IClickItemListener listener) {
        this.artists = artists;
        this.context = context;
        this.iClickItemListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_home,parent,false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);
        int i = position;
        if (artist == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(artist.getImage(), "drawable",context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imageView.setImageURI(uri);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iClickItemListener.onClickItem(artist);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(artists != null)
            return artists.size();
        return 0;
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_radio_popular);

        }
    }
}
