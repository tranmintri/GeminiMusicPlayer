package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;

import java.util.List;

public class ChooseArtistAdapter extends  RecyclerView.Adapter<ChooseArtistAdapter.ArtistViewHolder>{
    private Context mContext;
    private List<Artist> mListArtist;


    private OnItemClickListener mListener;


    public ChooseArtistAdapter(Context mContext, List<Artist> newArtist, OnItemClickListener listener) {
        this.mListener = listener;
        this.mContext = mContext;
        this.mListArtist = newArtist;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(int position,boolean isChecked);
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist,parent,false);
        return new ArtistViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = mListArtist.get(position);
        String path = "android.resource://" + mContext.getPackageName() + "/" + mContext.getResources().getIdentifier(artist.getImage(), "drawable", mContext.getPackageName());
        if (artist == null)
            return;
        Uri uri = Uri.parse(path);
        holder.imgArtist.setImageURI(uri);

    }

    @Override
    public int getItemCount() {
        if(mListArtist != null){
            return mListArtist.size();
        }
        return 0;
    }
    public class ArtistViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgArtist;
        public CheckBox checkBox;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            imgArtist = itemView.findViewById(R.id.img_artist);
            checkBox = itemView.findViewById(R.id.checkbox_artist);
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
