package com.tranminhtri.test.geminimusicplayer.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.tranminhtri.test.geminimusicplayer.Adapter.SongAdapter;
import com.tranminhtri.test.geminimusicplayer.Adapter.SongAdapterSearch;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.SongDAO;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search extends Fragment {
    private View view;
    private AutoCompleteTextView edtSearch;
    private Activity_Home activity_home;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        edtSearch = view.findViewById(R.id.edtSearch);
        activity_home = (Activity_Home) getActivity();
        mediaPlayer = activity_home.getMediaPlayer();
        SongDAO songDAO = new SongDAO();
        List<Song> listSong = new ArrayList<>();
        listSong = songDAO.findAll();

        SongAdapterSearch songAdapter = new SongAdapterSearch(activity_home,listSong);
        edtSearch.setAdapter(songAdapter);

        edtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = (Song) parent.getItemAtPosition(position);
                mediaPlayer = activity_home.getMediaPlayer();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    activity_home.setPause(true);
                }
                activity_home.setMediaPlayer(mediaPlayer);
                activity_home.changeMusic(song);
                activity_home.setCurrentMusicPlay(song);
                activity_home.setStatusPause();
                activity_home.setPause(false);
                InputMethodManager imm = (InputMethodManager) activity_home.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                edtSearch.setText("");
                closeKeyboard();
            }
        });

        return view;


    }
    public void closeKeyboard(){
        View view =this.getActivity().getCurrentFocus();
        if(view!=null){
            InputMethodManager imm = (InputMethodManager) activity_home.getSystemService(activity_home.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}