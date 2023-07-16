package com.tranminhtri.test.geminimusicplayer.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.dao.SongDAO;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;


public class SongAdapterSearch extends ArrayAdapter<Song> {

    private List<Song> songs;


    private Context context;

    public SongAdapterSearch(@NonNull Context context, @NonNull List<Song> songs) {
        super(context, 0, songs);
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return  songFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Song song = songs.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song_playlist,parent,false);

        }
         ImageView imgAlbum = convertView.findViewById(R.id.iv_imgsongPlayList);
         TextView  albumDetailMusicName = convertView.findViewById(R.id.tv_songPlayListName);
         TextView albumArtistName = convertView.findViewById(R.id.tv_songPlayListArtist);

         if(song != null){
             String path = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier(song.getImage(), "drawable", context.getPackageName());
             Uri uri = Uri.parse(path);
             imgAlbum.setImageURI(uri);
             albumDetailMusicName.setText(song.getName());

             albumArtistName.setText(song.getArtist().getName());
         }


        return convertView;
    }

    private  Filter songFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            SongDAO songDAO = new SongDAO();

            List<Song> songList  = songDAO.findAll();
            List<Song> suggestions = new ArrayList<>();

            if (constraint == null || constraint.toString().trim().length() == 0) {
                // Nếu không có tiêu chí lọc, trả về toàn bộ danh sách ban đầu.
                suggestions.addAll(songs);
            } else {
                // Nếu có tiêu chí lọc, thực hiện lọc dữ liệu theo tiêu chí đó.
                String filterPattern = removeDiacritics( constraint.toString().toLowerCase().trim());
                for (Song song : songList) {
                    if (removeDiacritics(song.getName().toLowerCase().trim()).contains(filterPattern) || song.getArtist().getName().toLowerCase().trim().contains(filterPattern)) {

                        suggestions.add(song);
                    }
                }

            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll( (List<Song>)results.values);
            notifyDataSetChanged();
        }


    };
    public static String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

}
