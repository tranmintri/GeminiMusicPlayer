package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.models.Artist;

import java.util.List;

public class ArtistAdapterLibrary extends RecyclerView.Adapter<ArtistAdapterLibrary.ArtistLibraryViewHolder>{
    private Context context;
    private List<Artist> mListArtist;
    private OnItemClickListener mListener;

    public ArtistAdapterLibrary(Context context,List<Artist> list,OnItemClickListener listener) {
        this.context = context;
        this.mListArtist = list;
        mListener = listener;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @NonNull
    @Override
    public ArtistLibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_library,parent,false);
        return new ArtistLibraryViewHolder(view);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    @Override
    public void onBindViewHolder(@NonNull ArtistLibraryViewHolder holder, int position) {
        Artist artist = mListArtist.get(position);
        if (artist == null)
            return;
        String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(artist.getImage(), "drawable",context.getPackageName());
        Uri uri = Uri.parse(path);
        holder.imgArtist.setImageURI(uri);
        holder.tvName.setText(artist.getName());




    }

    @Override
    public int getItemCount() {
        if(mListArtist != null){

            return mListArtist.size();
        }
        return 0;
    }
    public class ArtistLibraryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgArtist;
        private TextView tvName;
        private TextView tvType;
        public ArtistLibraryViewHolder(@NonNull View itemView) {
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
        }
    }
}
