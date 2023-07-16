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

public class UserDAO {
    Connection connection = null;

    public UserDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "select * from users";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String username = resultSet.getString(1);
                String fullname = resultSet.getString(2);
                String passoword = resultSet.getString(3);
                String email = resultSet.getString(4);
                String address = resultSet.getString(6);
                byte[] image = resultSet.getBytes(5);
                User user = new User(username, fullname,passoword, email, address, image);


                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public User findByID(String userName) {
        User user = null;
        try {
            String sql = "select * from users where username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            // Lấy dữ liệu từ các cột của bảng
            if (resultSet.next()) {
                String username = resultSet.getString(1);
                String fullname = resultSet.getString(2);
                String passoword = resultSet.getString(3);
                String email = resultSet.getString(4);
                String address = resultSet.getString(6);
                byte[] image = resultSet.getBytes(5);
                user = new User(username, fullname,passoword, email, address, image);


            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public User save(User user) {
        try {
            String sql = "insert into users([username],[fullname],[user_pass],[email],[address],[image]) \n" +
                    "values(?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFullname());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getAddress());
            statement.setBytes(6, user.getImage());

            int rowsInserted = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public User update(User user) {
        try {
            String sql = "update users set image = ?, fullname = ?, user_pass = ? where username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBytes(1, user.getImage());
            statement.setString(2, user.getFullname());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUsername());

            int rowsInserted = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }
}
