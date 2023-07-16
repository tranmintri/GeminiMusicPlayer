package com.tranminhtri.test.geminimusicplayer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tranminhtri.test.geminimusicplayer.dao.Recent_SongDAO;
import com.tranminhtri.test.geminimusicplayer.dao.SongDAO;
import com.tranminhtri.test.geminimusicplayer.dao.UserDAO;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_Detail_Music;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_Home;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_Introduct;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_Library;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_ListMusic;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_ListMusicPlayList;
import com.tranminhtri.test.geminimusicplayer.fragment.Fragment_Search;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.RecentSong;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Activity_Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment_Home fragment_home = new Fragment_Home();
    private Fragment_Search fragment_search = new Fragment_Search();
    private Fragment_Library fragment_library = new Fragment_Library();

    private LinearLayout linearLayout;
    private TextView tvMusicName, tvArtistName;
    private ImageView ivArtist;
    private TextView tv_pause;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isPause = true;
    private SeekBar seekBar;
    private int count = 0;
    private LinearLayout layout;
    private Song currentSong;
    private int currentPosition = 0;
    private User user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        User user = getIntent().getParcelableExtra("user");
        UserDAO userDAO = new UserDAO();

        this.user = userDAO.findByID(user.getUsername());


        bottomNavigationView = findViewById(R.id.bottom_nav);
        tvArtistName = findViewById(R.id.tv_ArtistName);
        tvMusicName = findViewById(R.id.tv_MusicName);
        ivArtist = findViewById(R.id.iv_ImageArtist);
        linearLayout = findViewById(R.id.currentStatus);
        tv_pause = findViewById(R.id.tv_pause_main);
        seekBar = findViewById(R.id.seekbar_main);
        layout = findViewById(R.id.currentStatus);


        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_home).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_home).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                        break;
                    case R.id.action_search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_search).commit();
                        break;
                    case R.id.action_library:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_right_to_left).replace(R.id.container, fragment_library).commit();
                        break;
                }
                return true;
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickOpenBottomSheetDialogFragment();
            }
        });

        setResourcesWithMusic();

        Activity_Home.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentPosition = mediaPlayer.getCurrentPosition();
                    if (mediaPlayer.isPlaying()) {
                        tv_pause.setBackgroundResource(R.drawable.pause);
                    } else
                        tv_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                }
                new Handler().postDelayed(this, 100);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
                if(progress == seekBar.getMax()){
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }



                    changeMusic(currentSong);


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void clickOpenBottomSheetDialogFragment() {

        Fragment_Detail_Music myBottomSheetDialog = Fragment_Detail_Music.newInstance(currentSong);
        myBottomSheetDialog.show(getSupportFragmentManager(), myBottomSheetDialog.getTag());
    }

    //sẽ truyền vào 1 id của artist hoặc album rồi tìm tới đố tượng bài hát của nó.
    public void goToListMusic(List<Song> songs, String imagePath) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment_ListMusic fragment_listMusic = new Fragment_ListMusic();

        Parcelable[] songsArray = new Parcelable[songs.size()];

        for (int i = 0; i < songs.size(); i++) {
            songsArray[i] = songs.get(i);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArray("list_song", songsArray);
        bundle.putString("image", imagePath);

        fragment_listMusic.setArguments(bundle);
        fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_listMusic);
        fragmentTransaction.addToBackStack(fragment_listMusic.TAG);
        fragmentTransaction.commit();
    }
        public void goToListMusicPlayList(List<Song> songs, String imagePath, Playlist playlist) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment_ListMusicPlayList fragment_listMusic = new Fragment_ListMusicPlayList();

        Parcelable[] songsArray = new Parcelable[songs.size()];

        for (int i = 0; i < songs.size(); i++) {
            songsArray[i] = songs.get(i);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArray("list_song", songsArray);
        bundle.putString("image", imagePath);
        bundle.putSerializable("playlist",playlist);

        fragment_listMusic.setArguments(bundle);
        fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_listMusic);
        fragmentTransaction.addToBackStack(fragment_listMusic.TAG);
        fragmentTransaction.commit();
    }


    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setStatusPause() {

        if (isPause == false) {
            tv_pause.setBackgroundResource(R.drawable.pause);
            mediaPlayer.pause();
            isPause = true;

        } else {
            if (count == 0)
                playMusic();
            setVisible();
            tv_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            mediaPlayer.start();
            isPause = false;
        }

    }

    public void setCurrentMusicPlay(Song song) {
        String path = "android.resource://" + getPackageName() + "/" + getResources().getIdentifier(song.getImage(), "drawable", getPackageName());
        Uri uri = Uri.parse(path);
        tvArtistName.setText(song.getArtist().getName());
        tvMusicName.setText(song.getName());
        ivArtist.setImageURI(uri);
        currentSong = song;

    }

    private void setResourcesWithMusic() {
//        tv_pause.setOnClickListener(v -> setStatusPause());
        tv_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusPause();
                setPauseInList(isPause);
            }
        });

    }

    private void playMusic() {

        String path = "android.resource://" + getPackageName() + "/" + getResources().getIdentifier(currentSong.getFilePath(), "raw", getPackageName());
        Uri uri = Uri.parse(path);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());
        count = 1;
        Recent_SongDAO recent_songDAO = new Recent_SongDAO();
        RecentSong recentSong = new RecentSong(currentSong,user,new Timestamp(System.currentTimeMillis()));
        recent_songDAO.save(recentSong);
        updateNumberList(currentSong);
    }

    public void setVisible() {
        layout.setVisibility(View.VISIBLE);
    }

    public LinearLayout getLinearLayout() {
        return layout;
    }

    public void changeMusic(Song song) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        setCurrentMusicPlay(song);
        String path = "android.resource://" + getPackageName() + "/" + getResources().getIdentifier(currentSong.getFilePath(), "raw", getPackageName());
        Uri uri = Uri.parse(path);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);


        mediaPlayer.start();
        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());
        Recent_SongDAO recent_songDAO = new Recent_SongDAO();
        RecentSong recentSong = new RecentSong(currentSong,user,new Timestamp(System.currentTimeMillis()));
        recent_songDAO.save(recentSong);
        updateNumberList(currentSong);
        count = 1;
    }
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
    public void setMediaPlayer(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }
    public User getUser(){

        return user;
    }
    public void setPauseInList(boolean isPause){
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof Fragment_ListMusic) {
                Fragment_ListMusic myFragment = (Fragment_ListMusic) fragment;
                myFragment.setPlay(isPause); // Gọi phương thức bên Fragment
                System.out.println(myFragment);
            }
            if (fragment instanceof Fragment_ListMusicPlayList) {
                Fragment_ListMusicPlayList myFragment = (Fragment_ListMusicPlayList) fragment;
                myFragment.setPlay(isPause); // Gọi phương thức bên Fragment
                System.out.println(myFragment);
            }
        }
    }
    public void updateNumberList(Song song){
        SongDAO songDAO = new SongDAO();
        song.setNumListen(song.getNumListen() + 1);
        songDAO.update(song);
    }
}
