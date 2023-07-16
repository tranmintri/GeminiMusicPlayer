package com.tranminhtri.test.geminimusicplayer.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tranminhtri.test.geminimusicplayer.Adapter.PlayListAddSongAdapter;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDAO;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDetailsDAO;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.PlaylistDetail;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;

import java.util.ArrayList;
import java.util.List;

public class Fragment_BottomDialog_AddSong extends BottomSheetDialogFragment implements Fragment_BottomDialog_AddPlaylist.OnDataChangeListener {
    private Activity_Home activity_home;
    private Song song;
    private TextView tv_Cancle;
    private Button btnAddPlayList, btnDone;
    private EditText edtPlayListName;
    private RecyclerView rcv_playlist;
    private Playlist playlist;
    private PlayListAddSongAdapter playListAdapterAddSong;
    private  Fragment_BottomDialog_AddPlaylist myBottomSheetDialog;
    private List<Playlist> playlists;
    private User user;
    public static Fragment_BottomDialog_AddSong newInstance(Song song) {
        Fragment_BottomDialog_AddSong myBottomSheetDialog = new Fragment_BottomDialog_AddSong();

        Bundle bundle = new Bundle();
        bundle.putParcelable("song", song);
        myBottomSheetDialog.setArguments(bundle);

        return myBottomSheetDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment__bottom_dialog__add_songs, null);
        activity_home = (Activity_Home) getActivity();
        this.user = activity_home.getUser();
        bottomSheetDialog.setContentView(view);
        initView(view);

        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        tv_Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        playlists = getDataPlaylist();
        List<Playlist> playlistCheck = new ArrayList<>();
        playListAdapterAddSong = new PlayListAddSongAdapter(getContext(), playlists, new PlayListAddSongAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, boolean isChecked) {
                if(isChecked){
                    playlistCheck.add(playlists.get(position));
                }
                else{
                    playlistCheck.remove(playlists.get(position));
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Playlist playlist: playlistCheck) {
                    PlaylistDetailsDAO playlistDetailsDAO = new PlaylistDetailsDAO();
                    PlaylistDetail playlistDetail = new PlaylistDetail(song,playlist);
                    PlaylistDetail playlistDetail1= playlistDetailsDAO.save(playlistDetail);
                    if(playlistDetail1 == null){
                        Toast.makeText(getContext(),"Nhạc đã có trong " + playlist.getName().toLowerCase(),Toast.LENGTH_SHORT).show();
                    }
                }
                bottomSheetDialog.dismiss();

            }
        });
        btnAddPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenBottomSheetDialogFragment();
            }
        });





        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        rcv_playlist.setLayoutManager(linearLayoutManager2);
        rcv_playlist.setFocusable(false);
        rcv_playlist.setNestedScrollingEnabled(false);
        rcv_playlist.setAdapter(playListAdapterAddSong);

        return bottomSheetDialog;
    }
    private void clickOpenBottomSheetDialogFragment() {

        myBottomSheetDialog = Fragment_BottomDialog_AddPlaylist.newInstance();
            myBottomSheetDialog.setOnDataChangeListener(Fragment_BottomDialog_AddSong.this);
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
    private void initView(View view) {
        tv_Cancle = view.findViewById(R.id.tv_cancle_addsong);
        btnAddPlayList = view.findViewById(R.id.btnAddPlayList);
        btnDone = view.findViewById(R.id.btnDoneAddSong);
        rcv_playlist = view.findViewById(R.id.rcv_playlist_addSong);
    }

    private List<Playlist> getDataPlaylist() {
        PlaylistDAO playlistDAO = new PlaylistDAO();

        return playlistDAO.findByUserID(user.getUsername());
    }


    @Override
    public void onDataChanged(Playlist playlist) {
        playlists.add(playlist);
        playListAdapterAddSong.notifyItemInserted(playlists.size() - 1);
    }
}