package com.tranminhtri.test.geminimusicplayer.dao;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.AlbumDetails;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AlbumDetailsDAO {

    private Connection connection = null;
    private AlbumDAO albumDAO;
    private SongDAO songDAO;

    public AlbumDetailsDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AlbumDetails> findAll() {
        List<AlbumDetails> albumDetails = new ArrayList<>();
        try {
            String sql = "select * from album_details";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String songID = resultSet.getString(1);
                String albumID = resultSet.getString(2);
                AlbumDetails albumDetail = new AlbumDetails(new Song(songID), new Album(albumID));
                albumDetails.add(albumDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albumDetails;
    }

    public List<AlbumDetails> findByID(String albumID) {
        List<AlbumDetails> albumDetails = new ArrayList<>();
        try {
            String sql = "select * from album_details where album_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,albumID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String song_ID = resultSet.getString(1);
                String album_ID = resultSet.getString(2);
                albumDAO = new AlbumDAO();
                Album album = albumDAO.findByID(album_ID);

                songDAO = new SongDAO();
                Song song = songDAO.findByID(song_ID);
                AlbumDetails albumDetail = new AlbumDetails(song, album);
                albumDetails.add(albumDetail);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albumDetails;
    }
}
