package com.tranminhtri.test.geminimusicplayer.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tranminhtri.test.geminimusicplayer.Adapter.SongAdapter;
import com.tranminhtri.test.geminimusicplayer.Adapter.SongAdapterPlaylist;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDetailsDAO;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.PlaylistDetail;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Fragment_ListMusicPlayList extends Fragment {

    public static final String TAG = Fragment_ListMusicPlayList.class.getName();
    private ImageView iv_detailPlaylist;
    private TextView tv_back,tv_titleLíst;
    private TextView tv_play;
    private Activity_Home activity_home;
    private boolean isPlay = false;
    private RecyclerView recyclerView;
    private MediaPlayer mediaPlayer;
    private List<Song> songs = new ArrayList<>();
    private SongAdapterPlaylist songAdapter;
    private Playlist playlist;

    public static Fragment_ListMusicPlayList newInstance() {
        Fragment_ListMusicPlayList fragment_listMusic = new Fragment_ListMusicPlayList();
        return fragment_listMusic;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_music, container, false);
        activity_home = (Activity_Home) getActivity();
        iv_detailPlaylist = view.findViewById(R.id.iv_detail_playList);
        tv_back = view.findViewById(R.id.tv_back);
        tv_play = view.findViewById(R.id.tv_play_list_music);
        tv_titleLíst = view.findViewById(R.id.tv_titleLíst);

        tv_titleLíst.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        Parcelable[] songList = bundle.getParcelableArray("list_song");
        if (songList != null) {
            for (Parcelable parcelable : songList) {
                Song song = (Song) parcelable;
                songs.add(song);

            }
        }
        String imagePath = bundle.getString("image");

        String path = "android.resource://" + activity_home.getPackageName() + "/" + activity_home.getResources().getIdentifier(imagePath, "drawable", activity_home.getPackageName());
        Uri uri = Uri.parse(path);
        iv_detailPlaylist.setImageURI(uri);

        playlist = (Playlist) bundle.getSerializable("playlist");
        tv_titleLíst.setText(String.valueOf(playlist.getName()));

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

                    if (songs.size() != 0){
                        Random random = new Random();
                        int randomNumber = random.nextInt(songs.size());

                        activity_home.setCurrentMusicPlay(songs.get(randomNumber));
                        setStatusPlay();
                    }
                }
                else {
                    setStatusPlay();
                }
            }
        });
        songAdapter = new SongAdapterPlaylist(activity_home, songs, new SongAdapterPlaylist.IClickItemListener() {

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

            @Override
            public void onLongClickItem(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn muốn xoá?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PlaylistDetailsDAO playlistDetailsDAO = new PlaylistDetailsDAO();
                        PlaylistDetail playlistDetail = new PlaylistDetail(songs.get(position),playlist);
                        int row = playlistDetailsDAO.deleteByObject(playlistDetail);
                        if(row != -1){
                            songs.remove(position);
                            songAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(getContext(),"Đã xóa thành công",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }

        );

        recyclerView = view.findViewById(R.id.rcv_detail_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity_home, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(songAdapter);


        return view;
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

    public void setPlay(boolean isPlayed) {

        isPlay = isPlayed;
        setStatusPlay();
    }
}