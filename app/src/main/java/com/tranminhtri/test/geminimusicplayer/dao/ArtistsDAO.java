package com.tranminhtri.test.geminimusicplayer.dao;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArtistsDAO {
    Connection connection = null;

    public ArtistsDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();
        try {

            String sql = "Select * from artists";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String image = resultSet.getString(3);
                String country = resultSet.getString(4);
                Artist artist = new Artist(id, name, image,country);
                artists.add(artist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artists;
    }
    public Artist findByID(String artistID) {
        Artist artist = null;
        try {
            String sql = "select * from artists where artist_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,artistID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String image = resultSet.getString(3);
                String country = resultSet.getString(4);
                 artist = new Artist(id, name, image,country);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return artist;
    }
    public List<Artist> findByCountry(String country) {
        List<Artist> artists = new ArrayList<>();
        try {
            String sql = "select * from artists where country = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,country);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String image = resultSet.getString(3);
                String country1 = resultSet.getString(4);
                Artist artist = new Artist(id, name, image,country);
                artists.add(artist);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return artists;
    }
}
