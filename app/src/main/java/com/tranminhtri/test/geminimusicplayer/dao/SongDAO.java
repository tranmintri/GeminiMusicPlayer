package com.tranminhtri.test.geminimusicplayer.dao;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.models.Album;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.RecentSong;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {

    Connection connection = null;

    public SongDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Song> findAll() {
        List<Song> songs = new ArrayList<>();
        try {
            String sql = "Select * from songs";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                 String id = resultSet.getString(1);
                 String name = resultSet.getString(2);
                 String image =resultSet.getString(3);
                 int duration = resultSet.getInt(4);
                 String filePath = resultSet.getString(5);
                 String lyric = resultSet.getString(6);
                 int numListen = resultSet.getInt(7);

                ArtistsDAO artistsDAO = new ArtistsDAO();

                Artist artist = artistsDAO.findByID(resultSet.getString(8));

                Song song = new Song(id,name,image,artist,duration,numListen,filePath,lyric);
                songs.add(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }
    public List<Song> findAllOrderByNumListen() {
        List<Song> songs = new ArrayList<>();
        try {
            String sql = "select * from songs order by numListen desc";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String image =resultSet.getString(3);
                int duration = resultSet.getInt(4);
                String filePath = resultSet.getString(5);
                String lyric = resultSet.getString(6);
                int numListen = resultSet.getInt(7);

                ArtistsDAO artistsDAO = new ArtistsDAO();

                Artist artist = artistsDAO.findByID(resultSet.getString(8));

                Song song = new Song(id,name,image,artist,duration,numListen,filePath,lyric);
                songs.add(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public Song findByID(String songID) {
        Song song = null;
        try {
            String sql = "select * from songs where song_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,songID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Lấy dữ liệu từ các cột của bảng
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String image = resultSet.getString(3);
                int duration = resultSet.getInt(4);
                String filePath = resultSet.getString(5);
                String lyric = resultSet.getString(6);
                int numListen = resultSet.getInt(7);

                ArtistsDAO artistsDAO = new ArtistsDAO();

                Artist artist = artistsDAO.findByID(resultSet.getString(8));


                song = new Song(id, name, image, artist, duration, numListen, filePath, lyric);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return song;
    }
    public List<Song> findByIDArtistID(String artistID) {
        List<Song> songs = new ArrayList<>();
        try {
            String sql = "Select * from songs where artist_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,artistID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String image =resultSet.getString(3);
                int duration = resultSet.getInt(4);
                String filePath = resultSet.getString(5);
                String lyric = resultSet.getString(6);
                int numListen = resultSet.getInt(7);

                ArtistsDAO artistsDAO = new ArtistsDAO();
                Artist artist = artistsDAO.findByID( resultSet.getString(8));

                Song song = new Song(id,name,image,artist,duration,numListen,filePath,lyric);

                songs.add(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }
    public Song update(Song song) {
        try {
            String sql = "update songs set numListen = ? where song_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, song.getNumListen());
            statement.setString(2, song.getId());

            int rowsInserted = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return song;
    }

}
