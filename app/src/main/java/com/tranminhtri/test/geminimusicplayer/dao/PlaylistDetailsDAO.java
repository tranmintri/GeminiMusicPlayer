package com.tranminhtri.test.geminimusicplayer.dao;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.AlbumDetails;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.PlaylistDetail;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailsDAO {
    private Connection connection = null;
    private PlaylistDAO playlistDAO;
    private SongDAO songDAO;

    public PlaylistDetailsDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PlaylistDetail> findAll() {
        List<PlaylistDetail> playlistDetails = new ArrayList<>();
        try {
            String sql = "select * from playlist_details";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String songID = resultSet.getString(1);
                String playListID = resultSet.getString(2);

                SongDAO songDAO = new SongDAO();
                PlaylistDAO playlistDAO = new PlaylistDAO();
                PlaylistDetail playlistDetail = new PlaylistDetail(songDAO.findByID(songID) ,playlistDAO.findByPlayListID(playListID));
                playlistDetails.add(playlistDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlistDetails;
    }

    public List<PlaylistDetail> findByID(String playListID) {
        List<PlaylistDetail> playlistDetails = new ArrayList<>();
        try {
            String sql = "select * from playlist_details where playlist_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,playListID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String songID = resultSet.getString(1);
                String playList_ID = resultSet.getString(2);

                SongDAO songDAO = new SongDAO();
                PlaylistDAO playlistDAO = new PlaylistDAO();
                PlaylistDetail playlistDetail = new PlaylistDetail(songDAO.findByID(songID) ,playlistDAO.findByPlayListID(playList_ID));
                playlistDetails.add(playlistDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return playlistDetails;
    }
    public PlaylistDetail save(PlaylistDetail playlistDetail){
        try {
            String sql = "insert into playlist_details([song_id],[playlist_id]) \n" +
                    "values(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,playlistDetail.getSong().getId());
            statement.setString(2,playlistDetail.getPlaylist().getId());

            int rowsInserted = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return playlistDetail;
    }
    public int deleteByObject(PlaylistDetail playlistDetail){
        int rowsDelete = 0;
        try {
            String sql ="delete playlist_details where playlist_id = ? and song_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,playlistDetail.getPlaylist().getId());
            statement.setString(2,playlistDetail.getSong().getId());
            rowsDelete = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return rowsDelete;
    }
    public int deleteByPlayListID(String id){
        int rowsDelete = 0;
        try {
            String sql ="delete playlist_details where playlist_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,id);
            rowsDelete = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return rowsDelete;
    }
}
