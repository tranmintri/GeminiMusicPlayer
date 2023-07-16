package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.annotation.SuppressLint;
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
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.util.List;

public class LyricAdapter extends  RecyclerView.Adapter<LyricAdapter.LyricViewHolder>{
    private View view;
    private List<String> lyrics;
    private Context context;



    public LyricAdapter(Context context, List<String> lyrics) {
        this.lyrics = lyrics;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LyricViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lyric,parent,false);
        return new LyricViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LyricViewHolder holder, @SuppressLint("RecyclerView") int position) {

       holder.tv_lyric.setText(lyrics.get(position));

    }

    @Override
    public int getItemCount() {
        if(lyrics != null)
            return lyrics.size();
        return 0;
    }

    public class LyricViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_lyric;
        public LyricViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_lyric = itemView.findViewById(R.id.tv_lyric);
        }
    }
}
