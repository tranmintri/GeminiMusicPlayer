package com.tranminhtri.test.geminimusicplayer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tranminhtri.test.geminimusicplayer.Adapter.LyricAdapter;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.SongDAO;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Fragment_Detail_Music extends BottomSheetDialogFragment {

    private Song song;
    private TextView musicName, artistName;
    private ImageView musicImg;
    private TextView timeStart, timeEnd,tv_more;
    private TextView tv_DownPage, skipNext, skipPrevious, tv_shuffle, tv_repeat;
    private TextView tv_Pause;
    private SeekBar seekBar;
    private RecyclerView listView;
    private Activity_Home activity_home;
    private boolean isPause = false;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<Song> songs = new ArrayList<>();
    int i = 0;
    boolean isShuffle = false;
    boolean isRepeat = false;
    private View view;

    public static Fragment_Detail_Music newInstance(Song song) {
        Fragment_Detail_Music myBottomSheetDialog = new Fragment_Detail_Music();

        Bundle bundle = new Bundle();
        bundle.putParcelable("song", song);
        myBottomSheetDialog.setArguments(bundle);
        return myBottomSheetDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
         view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_music, null);
        activity_home = (Activity_Home) getActivity();
        bottomSheetDialog.setContentView(view);
        initView(view);

        setDataSong(song);



        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        tv_DownPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        tv_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusPlay();
                activity_home.setStatusPause();
            }
        });
        tv_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRepeat){
                    tv_repeat.setBackgroundResource(R.drawable.repeat_green);
                    isRepeat = true;
                    if(isShuffle)
                    {
                        tv_shuffle.setBackgroundResource(R.drawable.shuffle_while);
                        isShuffle = false;
                    }

                }
                else{
                    tv_repeat.setBackgroundResource(R.drawable.repeat_white);
                    isRepeat = false;
                }
            }
        });
        tv_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShuffle){
                    tv_shuffle.setBackgroundResource(R.drawable.shuffle_green);
                    isShuffle = true;
                    if(isRepeat)
                    {
                        tv_repeat.setBackgroundResource(R.drawable.repeat_white);
                        isRepeat = false;
                    }

                }
                else{
                    tv_shuffle.setBackgroundResource(R.drawable.shuffle_while);
                    isShuffle = false;
                }
            }
        });
        i = getPositionOnList(songs);

        skipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i != -2) {
                    mediaPlayer = activity_home.getMediaPlayer();
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    setMediaPlayer();
                    activity_home.setMediaPlayer(mediaPlayer);

                    if (i == songs.size()) {
                        activity_home.changeMusic(songs.get(0));
                        setDataSong(songs.get(0));
//                        setLoop();
                        song = songs.get(0);
                        i=0;

                    } else {
                        activity_home.changeMusic(songs.get(i));
                        setDataSong(songs.get(i));
//                        setLoop();

                        song = songs.get(i);
                    }
                    isPause = false;
                    setStatusPlay();
                    activity_home.setPause(isPause);
                    activity_home.setStatusPause();

                }
            }
        });



        skipPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                isPause = false;
                if (i != -2) {
                    mediaPlayer = activity_home.getMediaPlayer();
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();

                    }
                    setMediaPlayer();
                    activity_home.setMediaPlayer(mediaPlayer);
                    i--;
                    if (i == -1) {

                        activity_home.changeMusic(songs.get(songs.size()-1));
                        setDataSong(songs.get(songs.size()-1));
//                        setLoop();
                        i = songs.size()-1;
                        song = songs.get(songs.size()-1);

                    } else {
                        activity_home.changeMusic(songs.get(i));
                        setDataSong(songs.get(i));
//                        setLoop();
                        song = songs.get(i);

                    }
                    isPause = false;
                    setStatusPlay();
                    activity_home.setPause(isPause);
                    activity_home.setStatusPause();


                }
            }

        });
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });



        setLoop();

        return bottomSheetDialog;

    }
    private void showPopupMenu(View view) {
        // Tạo PopupMenu object
        PopupMenu popupMenu = new PopupMenu(getContext(), view);

        // Inflate menu
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());
        // Thêm Listener cho item trong menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addToPlaylist:
                        // Code xử lý khi người dùng chọn menu item 1
                        clickOpenBottomSheetDialogFragment(song);
                        return true;

                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
            }
        });

        // Hiển thị PopupMenu
        popupMenu.show();
    }
    private void clickOpenBottomSheetDialogFragment(Song song) {

        Fragment_BottomDialog_AddSong myBottomSheetDialog = Fragment_BottomDialog_AddSong.newInstance(song);
        myBottomSheetDialog.show(activity_home.getSupportFragmentManager(), myBottomSheetDialog.getTag());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            song = (Song) bundle.getParcelable("song");

        }

    }

    public void setStatusPlay() {
        if (isPause == false) {
            tv_Pause.setBackgroundResource(R.drawable.pause_circle);
            isPause = true;
            activity_home.setPause(!isPause);
            activity_home.setStatusPause();
        } else {
            tv_Pause.setBackgroundResource(R.drawable.play_circle_white);
            isPause = false;
            activity_home.setPause(!isPause);
            activity_home.setStatusPause();
        }
    }

    private String formatTime(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String formattedTime = formatter.format(new Date(time));
        return formattedTime;
    }

    private void initView(View view) {
        artistName = view.findViewById(R.id.tv_musicDetailArtistName);
        musicName = view.findViewById(R.id.tv_musicDetailName);
        musicImg = view.findViewById(R.id.music_detail_img);
        timeEnd = view.findViewById(R.id.tv_ToEnd);
        seekBar = view.findViewById(R.id.seekbar_detailPlayList);
        timeStart = view.findViewById(R.id.tv_FromStart);
        tv_DownPage = view.findViewById(R.id.tv_downPage);
        tv_Pause = view.findViewById(R.id.tv_pause);
        skipNext = view.findViewById(R.id.tv_skip_next);
        skipPrevious = view.findViewById(R.id.tv_skip_previous);
        tv_repeat = view.findViewById(R.id.tv_repeat);
        tv_shuffle = view.findViewById(R.id.tv_shuffle);
        listView = view.findViewById(R.id.layout_lyric);

        tv_more = view.findViewById(R.id.tv_more_horiz_detail);
    }

    private void setDataSong(Song song) {
        setMediaPlayer();
        if (song == null)
            return;
        musicName.setText(song.getName());
        artistName.setText(song.getArtist().getName());

        String path = "android.resource://" + activity_home.getPackageName() + "/" + activity_home.getResources().getIdentifier(song.getImage(), "drawable", activity_home.getPackageName());
        Uri uri = Uri.parse(path);
        musicImg.setImageURI(uri);
        timeEnd.setText(String.valueOf(formatTime(mediaPlayer.getDuration())));
        SongDAO songDAO = new SongDAO();
        songs = songDAO.findByIDArtistID(song.getArtist().getId());

        String[] listLyric = song.getLyric().split("/n");
        List<String> lyrics = new ArrayList<>(Arrays.asList(listLyric));
        LyricAdapter arrayAdapter = new LyricAdapter(activity_home, lyrics);
        listView.setFocusable(false);
        listView.setNestedScrollingEnabled(false);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);


        LinearLayoutManager layout = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        listView.setLayoutManager(layout);
        listView.setFocusable(false);
        listView.setNestedScrollingEnabled(false);
        listView.setAdapter(arrayAdapter);

    }

    public int getPositionOnList(List<Song> songs) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId().equalsIgnoreCase(song.getId())) {
                return i;
            }
        }
        return -2;
    }

    public void setLoop() {
        Handler handler = new Handler();
        seekBar.setMax(mediaPlayer.getDuration());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    timeStart.setText(formatTime(currentPosition));
                    seekBar.setProgress(currentPosition);
                    if (!mediaPlayer.isPlaying()) {
                        tv_Pause.setBackgroundResource(R.drawable.play_circle_white);
                    }

                }
                handler.postDelayed(this, 0);
            }
        }, 0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }

                if(progress == mediaPlayer.getDuration()){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    int i = getPositionOnList(songs);
                    activity_home.setMediaPlayer(mediaPlayer);
                    if(!isRepeat && !isShuffle){
                        System.out.println("a");
                        //qua baif tieeps theo
                        i++;
                        if (i != -2) {

                            if (i == songs.size()) {
                                activity_home.changeMusic(songs.get(0));
                                setDataSong(songs.get(0));
//                        setLoop();
                                song = songs.get(0);
                                System.out.println(song.getName());
                                i=0;

                            } else {
                                activity_home.changeMusic(songs.get(i));
                                setDataSong(songs.get(i));
//                        setLoop();

                                song = songs.get(i);
                                System.out.println(song.getName());
                            }
                            isPause = false;
                            setStatusPlay();
                            activity_home.setPause(isPause);
                            activity_home.setStatusPause();

                        }

                    }

                    else if( isRepeat== true){
                        // phat lai
                        System.out.println("b");
                        activity_home.changeMusic(song);

                    }
                    else if(isShuffle){
                        System.out.println("c");
                        Random random = new Random();
                        int randomNumber = random.nextInt(songs.size());
                        activity_home.changeMusic(songs.get(randomNumber));
                        song = songs.get(randomNumber);
                    }
                    setMediaPlayer();
                    setDataSong(song);

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


    public void setMediaPlayer() {
        mediaPlayer = activity_home.getMediaPlayer();
    }
}
