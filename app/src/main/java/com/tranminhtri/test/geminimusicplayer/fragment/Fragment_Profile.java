package com.tranminhtri.test.geminimusicplayer.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tranminhtri.test.geminimusicplayer.Adapter.PlayListLibraryAdapter;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDAO;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDetailsDAO;
import com.tranminhtri.test.geminimusicplayer.dao.User_FollowDAO;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.PlaylistDetail;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;
import com.tranminhtri.test.geminimusicplayer.models.User_Follows;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Profile extends Fragment implements Fragment_Edit_Profile.OnDataChangeListener{
    private View view;
    private TextView tv_back;
    private ImageView imageProfile;
    private TextView profileName;
    private Button btn_edit;
    private Activity_Home activity_home;
    private RecyclerView rcv_PlayList;
    private PlayListLibraryAdapter playListAdapterLibrary;
    private List<Playlist> playlists;
    private TextView followCount, artistCount;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        activity_home = (Activity_Home) getActivity();
        tv_back = view.findViewById(R.id.tv_back_profile);
        btn_edit = view.findViewById(R.id.btn_edit);
        rcv_PlayList = view.findViewById(R.id.rcv_playlist);
        artistCount = view.findViewById(R.id.artistCount);
        imageProfile = view.findViewById(R.id.image_profile);
        profileName = view.findViewById(R.id.profile_name);

        activity_home = (Activity_Home) getActivity();
        user = activity_home.getUser();
        profileName.setText(user.getFullname());
        Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);

        imageProfile.setImageBitmap(bitmap);
        profileName.setText(user.getFullname());
        artistCount.setText(String.valueOf(getDataLoveArtist().size()));

        tv_back.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {

                if (getFragmentManager() != null) ;

                getFragmentManager().popBackStack();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenBottomSheetDialogFragment();
            }
        });

        playlists = getDataPlaylist();

        playListAdapterLibrary = new PlayListLibraryAdapter(getContext(), playlists, new PlayListLibraryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<Song> songs = new ArrayList<>();
                PlaylistDetailsDAO playlistDetailsDAO = new PlaylistDetailsDAO();
                List<PlaylistDetail> playlistDetailList = playlistDetailsDAO.findByID(playlists.get(position).getId());

                for (PlaylistDetail playListDetail : playlistDetailList) {
                    songs.add(playListDetail.getSong());
                }

                activity_home.goToListMusicPlayList(songs, "musicicon", playlists.get(position));
            }

            @Override
            public void onItemLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn muốn xoá?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PlaylistDetailsDAO playlistDetailsDAO = new PlaylistDetailsDAO();

                        int row = playlistDetailsDAO.deleteByPlayListID(playlists.get(position).getId());
                        int row1 = -1;
                        if(row != -1){
                            PlaylistDAO playlistDAO  = new PlaylistDAO();
                            row1 = playlistDAO.deleteByObject(playlists.get(position));
                        }
                        if(row1 != -1){
                            playlists.remove(position);
                            playListAdapterLibrary.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Đã xóa thành công", Toast.LENGTH_SHORT).show();
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
        });

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_PlayList.setLayoutManager(linearLayoutManager2);
        rcv_PlayList.setFocusable(false);
        rcv_PlayList.setNestedScrollingEnabled(false);
        rcv_PlayList.setAdapter(playListAdapterLibrary);

        return view;
    }

    private void clickOpenBottomSheetDialogFragment() {

        Fragment_Edit_Profile myBottomSheetDialog = Fragment_Edit_Profile.newInstance();
        myBottomSheetDialog.setOnDataChangeListener(this);
        myBottomSheetDialog.show(activity_home.getSupportFragmentManager(), myBottomSheetDialog.getTag());
    }

    private List<Playlist> getDataPlaylist() {
        PlaylistDAO playlistDAO = new PlaylistDAO();

        return playlistDAO.findByUserID(user.getUsername());
    }
    private List<Artist> getDataLoveArtist() {
        User_FollowDAO user_followDAO = new User_FollowDAO();
        List<Artist> artists = new ArrayList<>();

        List<User_Follows> user_followsList = user_followDAO.findByID(user.getUsername());
        for (User_Follows user_follows: user_followsList) {
            artists.add(user_follows.getArtist());
        }
        return artists;
    }

    @Override
    public void onDataChanged(User user) {

        this.user = user;
        Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
        imageProfile.setImageBitmap(bitmap);
        profileName.setText(user.getFullname());
    }

}