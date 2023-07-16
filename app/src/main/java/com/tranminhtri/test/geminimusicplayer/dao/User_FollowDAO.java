package com.tranminhtri.test.geminimusicplayer.dao;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.AlbumDetails;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.PlaylistDetail;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;
import com.tranminhtri.test.geminimusicplayer.models.User_Follows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class User_FollowDAO {
    private Connection connection = null;

    public User_FollowDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User_Follows> findByID(String username) {
        List<User_Follows> user_followsList = new ArrayList<>();
        try {
            String sql = "select * from user_follows where username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String artist_ID = resultSet.getString(1);
                String user_name = resultSet.getString(2);

                ArtistsDAO artistsDAO = new ArtistsDAO();
                Artist artist = artistsDAO.findByID(artist_ID);

                UserDAO userDAO = new UserDAO();
                User user = userDAO.findByID(username);

                User_Follows user_follows = new User_Follows(artist,user);
                user_followsList.add(user_follows);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user_followsList;
    }
    public List<User_Follows> findNotByID(String username) {
        List<User_Follows> user_followsList = new ArrayList<>();
        try {
            String sql = "select * from user_follows where username <> ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String artist_ID = resultSet.getString(1);
                String user_name = resultSet.getString(2);

                ArtistsDAO artistsDAO = new ArtistsDAO();
                Artist artist = artistsDAO.findByID(artist_ID);

                UserDAO userDAO = new UserDAO();
                User user = userDAO.findByID(username);

                User_Follows user_follows = new User_Follows(artist,user);
                user_followsList.add(user_follows);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user_followsList;
    }
    public User_Follows save(User_Follows user_follows){
        try {
            String sql = "insert into user_follows(artist_id,username) \n" +
                    "values(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,user_follows.getArtist().getId());
            statement.setString(2,user_follows.getUser().getUsername());

            int rowsInserted = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user_follows;
    }
}
