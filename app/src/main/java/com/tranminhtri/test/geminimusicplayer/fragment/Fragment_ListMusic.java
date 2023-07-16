package com.tranminhtri.test.geminimusicplayer.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tranminhtri.test.geminimusicplayer.Adapter.SongAdapter;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Fragment_ListMusic extends Fragment{

    public static final String TAG = Fragment_ListMusic.class.getName();
    private ImageView iv_detailPlaylist;
    private TextView tv_back;
    private TextView tv_play;
    private Activity_Home activity_home;
    private boolean isPlay = false;
    private RecyclerView recyclerView;
    private MediaPlayer mediaPlayer;
    private List<Song> songs = new ArrayList<>();
    private View view;
    public static Fragment_ListMusic newInstance() {
      Fragment_ListMusic fragment_listMusic = new Fragment_ListMusic();
        return fragment_listMusic;
    }

    public Fragment_ListMusic() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_music, container, false);
        activity_home = (Activity_Home) getActivity();

        iv_detailPlaylist = view.findViewById(R.id.iv_detail_playList);
        tv_back = view.findViewById(R.id.tv_back);
        tv_play = view.findViewById(R.id.tv_play_list_music);

        Bundle bundle = getArguments();
        Parcelable[] songList =  bundle.getParcelableArray("list_song");
        if(songList != null)
        {
            for (Parcelable parcelable: songList) {
                Song song = (Song) parcelable;
                songs.add(song);

            }
        }
        String imagePath = bundle.getString("image");

        String path = "android.resource://" + activity_home.getPackageName() + "/" + activity_home.getResources().getIdentifier(imagePath, "drawable",activity_home.getPackageName());
        Uri uri = Uri.parse(path);
        iv_detailPlaylist.setImageURI(uri);
        isPlay = activity_home.isPause();
        setStatusPlay();

        tv_back.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {

                if (getFragmentManager() != null) ;

                getFragmentManager().popBackStack();
            }
        });
        tv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity_home.getLinearLayout().getVisibility() == View.GONE) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(songs.size());

                    activity_home.setCurrentMusicPlay(songs.get(randomNumber));
                }
                setStatusPlay();

            }
        });
        SongAdapter songAdapter = new SongAdapter(activity_home,songs, new SongAdapter.IClickItemListener() {


            @Override
            public void onClickItem(int position) {
                mediaPlayer = activity_home.getMediaPlayer();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                isPlay = false;
                activity_home.setMediaPlayer(mediaPlayer);
                activity_home.changeMusic(songs.get(position));
                setStatusPlay();
            }

        },
                new SongAdapter.OnclickItem(){

                    @Override
                    public void onClick(int position,View view) {
                        showPopupMenu(view,songs.get(position));
                    }
                });

        recyclerView = view.findViewById(R.id.rcv_detail_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity_home, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(songAdapter);


        return view;
    }



    private void showPopupMenu(View view, Song song) {
        // Tạo PopupMenu object
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        Song song1  = song;
        // Inflate menu
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());


        // Thêm Listener cho item trong menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addToPlaylist:
                        // Code xử lý khi người dùng chọn menu item 1
                        clickOpenBottomSheetDialogFragment(song1);
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

    public void setStatusPlay() {
        if (isPlay != true) {
            tv_play.setBackgroundResource(R.drawable.pause_circle_green);
            isPlay = true;
            activity_home.setPause(isPlay);
            activity_home.setStatusPause();
        } else {
            tv_play.setBackgroundResource(R.drawable.play_circle_green);
            isPlay = false;
            activity_home.setPause(isPlay);
            activity_home.setStatusPause();
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    public void setPlay(boolean isPlayed) {
        isPlay = isPlayed;
        setStatusPlay();
    }

//    @Override
//    public void onClick(boolean isClick) {
//        if (isClick != true) {
//            tv_play.setBackgroundResource(R.drawable.pause_circle_green);
//            isPlay = isClick;
//            activity_home.setPause(isPlay);
//            activity_home.setStatusPause();
//        } else {
//            tv_play.setBackgroundResource(R.drawable.play_circle_green);
//            isPlay = isClick;
//            activity_home.setPause(isPlay);
//            activity_home.setStatusPause();
//        }
//    }
}