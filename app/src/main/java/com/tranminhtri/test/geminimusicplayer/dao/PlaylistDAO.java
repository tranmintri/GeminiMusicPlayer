package com.tranminhtri.test.geminimusicplayer.dao;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaylistDAO {
    Connection connection = null;

    public PlaylistDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Playlist save(Playlist playlist){
        try {
            String sql = "insert into playlists([playlist_id],[playlist_name],[username],[createDate]) \n" +
                    "values(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,playlist.getId());
            statement.setString(2,playlist.getName());
            statement.setString(3,playlist.getUser().getUsername());
            statement.setDate(4,new java.sql.Date(playlist.getCreateDate().getTime()));
            int rowsInserted = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return playlist;
    }

    public List<Playlist> findAll() {
        List<Playlist> playlists = new ArrayList<>();
        try {
            String sql = "select * from playlists";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String username = resultSet.getString(3);
                Date date = resultSet.getDate(4);

               UserDAO userDAO = new UserDAO();

                User user = userDAO.findByID(username);
                Playlist playlist  = new Playlist(id,name,date,user);
                playlists.add(playlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return playlists;
    }

    public List<Playlist> findByUserID(String username) {
        List<Playlist> playlists = new ArrayList<>();
        try {
            String sql = "select * from playlists where username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            // Lấy dữ liệu từ các cột của bảng

            while(resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String userName = resultSet.getString(3);
                Date date = resultSet.getDate(4);

                UserDAO userDAO = new UserDAO();

                User user = userDAO.findByID(userName);


                Playlist playlist  = new Playlist(id,name,date,user);

                playlists.add(playlist);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return playlists;
    }
    public Playlist findByPlayListID(String playListID) {
        Playlist playlist =null;
        try {
            String sql = "select * from playlists where playlist_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playListID);
            ResultSet resultSet = statement.executeQuery();
            // Lấy dữ liệu từ các cột của bảng

            if(resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String username = resultSet.getString(3);
                Date date = resultSet.getDate(4);

                UserDAO userDAO = new UserDAO();

                User user = userDAO.findByID(username);


                 playlist  = new Playlist(id,name,date,user);


            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return playlist;
    }
    public int deleteByObject(Playlist playlist){
        int rowsDelete = 0;
        try {
            String sql ="delete playlists where playlist_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,playlist.getId());
            rowsDelete = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return rowsDelete;
    }
}
