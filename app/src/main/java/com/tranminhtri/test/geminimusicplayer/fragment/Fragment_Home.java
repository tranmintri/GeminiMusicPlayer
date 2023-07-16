package com.tranminhtri.test.geminimusicplayer.fragment;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tranminhtri.test.geminimusicplayer.Adapter.AlbumAdapter;
import com.tranminhtri.test.geminimusicplayer.Adapter.ArtistAdapter;
import com.tranminhtri.test.geminimusicplayer.Adapter.PlayListAdapter;
import com.tranminhtri.test.geminimusicplayer.Adapter.RecentSongAdapter;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.AlbumDAO;
import com.tranminhtri.test.geminimusicplayer.dao.AlbumDetailsDAO;
import com.tranminhtri.test.geminimusicplayer.dao.ArtistsDAO;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDAO;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDetailsDAO;
import com.tranminhtri.test.geminimusicplayer.dao.Recent_SongDAO;
import com.tranminhtri.test.geminimusicplayer.dao.SongDAO;
import com.tranminhtri.test.geminimusicplayer.dao.User_FollowDAO;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.AlbumDetails;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.PlaylistDetail;
import com.tranminhtri.test.geminimusicplayer.models.RecentSong;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;
import com.tranminhtri.test.geminimusicplayer.models.User_Follows;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;


public class Fragment_Home extends Fragment {
    private RecyclerView rcvRecentSong;
    private RecyclerView rcvPlaylist;
    private RecyclerView rcvLoveArtist;
    private RecyclerView rcvMusicRank;
    private RecyclerView rcvAlbumPopular;
    private Activity_Home activity_home;
    private View view;
    private TextView tvSetting, tvTitle, tvPlayList, tvRecentSong;
    private AlbumDAO albumDAO;
    private MediaPlayer mediaPlayer;
    private boolean isPlay = false;
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity_home = (Activity_Home) getActivity();
        user = activity_home.getUser();
        tvSetting = view.findViewById(R.id.iconSetting);
        tvTitle = view.findViewById(R.id.tv_title_home);
        tvPlayList = view.findViewById(R.id.tvPlayList);
        tvRecentSong = view.findViewById(R.id.tvRecentSong);
        setTitle();
        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = activity_home.getSupportFragmentManager();
                Fragment_Setting fragment_setting = new Fragment_Setting();
                manager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_setting).addToBackStack(fragment_setting.getTag()).commit();
            }
        });


        rcvRecentSong = view.findViewById(R.id.rcv_recent_song);
        rcvPlaylist = view.findViewById(R.id.rcv_playlist_home);
        rcvLoveArtist = view.findViewById(R.id.rcv_love_artist);
        rcvMusicRank = view.findViewById(R.id.rcv_rank);
        rcvAlbumPopular = view.findViewById(R.id.rcv_album_popular);

        //Bài hát gần đây
        if (getRecentSong().size() == 0) {
            tvRecentSong.setVisibility(View.GONE);
        }
        RecentSongAdapter adapter_RecentSong = new RecentSongAdapter(activity_home, getRecentSong(), new RecentSongAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Song song) {
                mediaPlayer = activity_home.getMediaPlayer();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                activity_home.setMediaPlayer(mediaPlayer);
                activity_home.changeMusic(song);
                activity_home.setPause(true);
                activity_home.setStatusPause();

            }
        });
        LinearLayoutManager layout_RecentSong = new LinearLayoutManager(activity_home, RecyclerView.HORIZONTAL, false);
        rcvRecentSong.setLayoutManager(layout_RecentSong);
        rcvRecentSong.setFocusable(false);
        rcvRecentSong.setNestedScrollingEnabled(false);
        rcvRecentSong.setAdapter(adapter_RecentSong);

        //Danh sách phát
        if (getDataPlaylist().size() == 0) {

            tvPlayList.setVisibility(View.GONE);

        }
        PlayListAdapter adapter_PlayList = new PlayListAdapter(activity_home, getDataPlaylist(), new PlayListAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Playlist playlist) {
                List<Song> songs = new ArrayList<>();
                PlaylistDetailsDAO playlistDetailsDAO = new PlaylistDetailsDAO();
                List<PlaylistDetail> playlistDetailList = playlistDetailsDAO.findByID(playlist.getId());

                for (PlaylistDetail playListDetail : playlistDetailList) {
                    songs.add(playListDetail.getSong());
                }

                activity_home.goToListMusicPlayList(songs, "musicicon", playlist);

            }
        });
        LinearLayoutManager layout_Playlist = new LinearLayoutManager(activity_home, RecyclerView.HORIZONTAL, false);
        rcvPlaylist.setLayoutManager(layout_Playlist);
        rcvPlaylist.setFocusable(false);
        rcvPlaylist.setNestedScrollingEnabled(false);
        rcvPlaylist.setAdapter(adapter_PlayList);


        //Album phổ biến
        AlbumAdapter adapter_Album = new AlbumAdapter(activity_home, getDataAlbum(), new AlbumAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Album album) {
                List<Song> songs = new ArrayList<>();
                AlbumDetailsDAO albumDetailsDAO = new AlbumDetailsDAO();
                List<AlbumDetails> albumDetailsList = albumDetailsDAO.findByID(album.getId());

                for (AlbumDetails albumDetails : albumDetailsList) {
                    songs.add(albumDetails.getSong());
                }

                activity_home.goToListMusic(songs, album.getImage());

            }
        });
        LinearLayoutManager layout_RadioHint = new LinearLayoutManager(activity_home, RecyclerView.HORIZONTAL, false);
        rcvAlbumPopular.setLayoutManager(layout_RadioHint);
        rcvAlbumPopular.setFocusable(false);
        rcvAlbumPopular.setNestedScrollingEnabled(false);
        rcvAlbumPopular.setAdapter(adapter_Album);

        //Nghệ sĩ yêu thích
        ArtistAdapter adapter_LoveArtist = new ArtistAdapter(activity_home, getDataLoveArtist(), new ArtistAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Artist artist) {
                List<Song> songs = new ArrayList<>();
                SongDAO songDAO = new SongDAO();
                songs = songDAO.findByIDArtistID(artist.getId());
                activity_home.goToListMusic(songs, artist.getImage());

            }
        });
        LinearLayoutManager layout_LoveArtist = new LinearLayoutManager(activity_home, RecyclerView.HORIZONTAL, false);
        rcvLoveArtist.setLayoutManager(layout_LoveArtist);
        rcvLoveArtist.setFocusable(false);
        rcvLoveArtist.setNestedScrollingEnabled(false);
        rcvLoveArtist.setAdapter(adapter_LoveArtist);

//Bảng xếp hạng
        AlbumAdapter adapter_AlbumRank = new AlbumAdapter(activity_home, getDataAblumRank(), new AlbumAdapter.IClickItemListener() {
            @Override
            public void onClickItem(Album album) {

                List<Song> songsVN = new ArrayList<>();
                List<Song> songsNN = new ArrayList<>();

                SongDAO songDAO = new SongDAO();
                List<Song> songList = songDAO.findAllOrderByNumListen();

                ArtistsDAO artistsDAO = new ArtistsDAO();
                List<Artist> artistVNs = artistsDAO.findByCountry("Việt Nam");
                List<Artist> artistNNs = artistsDAO.findByCountry("Việt Nam");

                for (Song song : songList) {
                    if (song.getArtist().getCountry().equalsIgnoreCase(("Việt Nam"))) {
                        songsVN.add(song);
                    } else {
                        songsNN.add(song);
                    }
                }
                if (album.getId().equalsIgnoreCase("album12")) {
                    activity_home.goToListMusic(songsVN, album.getImage());
                } else {
                    activity_home.goToListMusic(songsNN, album.getImage());
                }
            }
        });
        LinearLayoutManager layout_Rank = new LinearLayoutManager(activity_home, RecyclerView.HORIZONTAL, false);
        rcvMusicRank.setLayoutManager(layout_Rank);
        rcvMusicRank.setFocusable(false);
        rcvMusicRank.setNestedScrollingEnabled(false);
        rcvMusicRank.setAdapter(adapter_AlbumRank);

        return view;
    }

    private List<Album> getDataAlbum() {
        albumDAO = new AlbumDAO();
        List<Album> albums = albumDAO.findAll();
        List<Album> listNew = new ArrayList<>();

        for (Album album : albums) {
            if (album.getArtist() != null)
                listNew.add(album);
        }
        return listNew;
    }

    private List<Playlist> getDataPlaylist() {
        PlaylistDAO playlistDAO = new PlaylistDAO();

        return playlistDAO.findByUserID(user.getUsername());
    }

    private List<Album> getDataAblumRank() {


        albumDAO = new AlbumDAO();
        List<Album> albums = albumDAO.findAll();
        List<Album> listNew = new ArrayList<>();

        for (Album album : albums) {
            if (album.getArtist() == null)
                listNew.add(album);
        }
        return listNew;
    }

    private List<Artist> getDataLoveArtist() {
        User_FollowDAO user_followDAO = new User_FollowDAO();
        List<Artist> artists = new ArrayList<>();

        List<User_Follows> user_followsList = user_followDAO.findByID(user.getUsername());
        for (User_Follows user_follows : user_followsList) {
            artists.add(user_follows.getArtist());
        }
        return artists;
    }

    private List<Song> getRecentSong() {
        List<RecentSong> recentSongs = null;
        List<Song> songList = new ArrayList<>();
        try {
            Recent_SongDAO recent_songDAO = new Recent_SongDAO();
            recentSongs = recent_songDAO.getTop5RecentSongByDateDesc(user.getUsername());

            for (RecentSong recentSong : recentSongs) {
                songList.add(recentSong.getSong());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return songList;
    }

    public void setTitle() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (hour > 12) {
            tvTitle.setText("Chào buổi chiều");
        } else if (hour == 12) {
            if (minute > 0) {
                tvTitle.setText("Chào buổi chiều");
            }
        } else {
            tvTitle.setText("Chào buổi sáng");
        }
    }

    public static String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

}