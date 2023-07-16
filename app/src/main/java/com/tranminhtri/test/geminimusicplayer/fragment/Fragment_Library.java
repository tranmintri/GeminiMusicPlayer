package com.tranminhtri.test.geminimusicplayer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tranminhtri.test.geminimusicplayer.Adapter.ArtistAdapterLibrary;
import com.tranminhtri.test.geminimusicplayer.Adapter.PlayListLibraryAdapter;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDAO;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDetailsDAO;
import com.tranminhtri.test.geminimusicplayer.dao.SongDAO;
import com.tranminhtri.test.geminimusicplayer.dao.UserDAO;
import com.tranminhtri.test.geminimusicplayer.dao.User_FollowDAO;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.PlaylistDetail;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;
import com.tranminhtri.test.geminimusicplayer.models.User_Follows;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Library extends Fragment implements Fragment_BottomDialog_AddPlaylist.OnDataChangeListener {
    private RecyclerView rcv_ArtistLibrary;
    private RecyclerView rcv_PlayListLibrary;
    private Activity_Home activity_home;
    private TextView tv_add_playlist;
    private ImageView img_avt;
    private Fragment_BottomDialog_AddPlaylist myBottomSheetDialog;
    private PlayListLibraryAdapter playListAdapterLibrary;
    private List<Playlist> playlists;
    private LinearLayout layoutAddArtist;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library,container,false);

        activity_home = (Activity_Home) getActivity();
        user = activity_home.getUser();
        rcv_ArtistLibrary = view.findViewById(R.id.rcv_artist_library);
        rcv_PlayListLibrary = view.findViewById(R.id.rcv_playlist_library);
        img_avt = view.findViewById(R.id.circleImageView2);
        layoutAddArtist = view.findViewById(R.id.addArtist);
        UserDAO userDAO = new UserDAO();
        User user2 = userDAO.findByID(user.getUsername());

        Bitmap bitmap = BitmapFactory.decodeByteArray(user2.getImage(), 0, user2.getImage().length);
        user = user2;


        img_avt.setImageBitmap(bitmap);

        img_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = activity_home.getSupportFragmentManager();
                Fragment_Setting fragment_setting = new Fragment_Setting();
                manager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_setting).addToBackStack(fragment_setting.getTag()).commit();
            }
        });
        layoutAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = activity_home.getSupportFragmentManager();
                Fragment_AddArtist fragment_addArtist = new Fragment_AddArtist();
                manager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_addArtist).addToBackStack(fragment_addArtist.getTag()).commit();
            }
        });


        ArtistAdapterLibrary artistAdapterLibrary = new ArtistAdapterLibrary(getContext(), getListData(), new ArtistAdapterLibrary.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Artist artist = getListData().get(position);
                List<Song> songs = new ArrayList<>();
                SongDAO songDAO = new SongDAO();
                songs = songDAO.findByIDArtistID(artist.getId());
                activity_home.goToListMusic(songs, artist.getImage());
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        rcv_ArtistLibrary.setLayoutManager(linearLayoutManager);
        rcv_ArtistLibrary.setFocusable(false);
        rcv_ArtistLibrary.setNestedScrollingEnabled(false);
        rcv_ArtistLibrary.setAdapter(artistAdapterLibrary);

        tv_add_playlist = view.findViewById(R.id.tv_add_playlist);
        tv_add_playlist.setOnClickListener(new View.OnClickListener() {
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
                List<PlaylistDetail> playlistDetailList = playlistDetailsDAO.findByID(getDataPlaylist().get(position).getId());

                for (PlaylistDetail playListDetail: playlistDetailList) {
                    songs.add(playListDetail.getSong());
                }

                activity_home.goToListMusicPlayList(songs,"musicicon",playlists.get(position));
            }

             @Override
             public void onItemLongClick(int position) {
             }
         });

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        rcv_PlayListLibrary.setLayoutManager(linearLayoutManager2);
        rcv_PlayListLibrary.setFocusable(false);
        rcv_PlayListLibrary.setNestedScrollingEnabled(false);
        rcv_PlayListLibrary.setAdapter(playListAdapterLibrary);


        return view;
    }
    private void clickOpenBottomSheetDialogFragment() {

        myBottomSheetDialog = Fragment_BottomDialog_AddPlaylist.newInstance();
        myBottomSheetDialog.setOnDataChangeListener(this);
        myBottomSheetDialog.show(activity_home.getSupportFragmentManager(), myBottomSheetDialog.getTag());
    }

    private List<Artist> getListData() {

        User_FollowDAO user_followDAO = new User_FollowDAO();
        List<Artist> artists = new ArrayList<>();

        List<User_Follows> user_followsList = user_followDAO.findByID(user.getUsername());
        for (User_Follows user_follows: user_followsList) {
            artists.add(user_follows.getArtist());
        }
        return artists;

    }
    private List<Playlist> getDataPlaylist() {
        PlaylistDAO playlistDAO = new PlaylistDAO();
        return playlistDAO.findByUserID(user.getUsername());
    }

    @Override
    public void onDataChanged(Playlist playlist) {

        playlists.add(playlist  );
        playListAdapterLibrary.notifyItemInserted(playlists.size() - 1);

    }
}