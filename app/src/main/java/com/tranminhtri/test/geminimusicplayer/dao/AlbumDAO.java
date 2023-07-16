package com.tranminhtri.test.geminimusicplayer.dao;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

    Connection connection = null;

    public AlbumDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Album> findAll() {
        List<Album> albums = new ArrayList<>();
        try {
            String sql = "Select * from albums";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String image = resultSet.getString(3);
                int year = resultSet.getInt(4);

                ArtistsDAO artistsDAO = new ArtistsDAO();

                Artist artist = artistsDAO.findByID(resultSet.getString(5));

                Album album = new Album(id, name, image, year, artist);
                albums.add(album);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albums;
    }

    public Album findByID(String albumID) {
        Album album= null;
        try {
            String sql = "select * from albums where album_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, albumID);
            ResultSet resultSet = statement.executeQuery();
            // Lấy dữ liệu từ các cột của bảng
            if (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String image = resultSet.getString(3);
                int year = resultSet.getInt(4);
                Artist artist = new Artist(resultSet.getString(5));

                album = new Album(id, name, image, year, artist);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return album;
    }
}
