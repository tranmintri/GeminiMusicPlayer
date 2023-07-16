package com.tranminhtri.test.geminimusicplayer.dao;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.RecentSong;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Recent_SongDAO {

    Connection connection = null;

    public Recent_SongDAO() {
        try {
            connection = SqlConnection.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RecentSong> getTop5RecentSongByDateDesc(String username) throws Exception {
        ArrayList<RecentSong> list = new ArrayList<>();
        String query = "select * from recentSongs where username = ? order by dateListen desc";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if (list.size() < 5) {
                String songid = rs.getString(1);
                String user_name = rs.getString(2);
                Timestamp dateListen = rs.getTimestamp(3);

                SongDAO songDAO = new SongDAO();
                Song song = songDAO.findByID(songid);

                UserDAO userDAO = new UserDAO();
                User user = userDAO.findByID(user_name);

                RecentSong recentSong = new RecentSong(song, user, dateListen);
                boolean isAdd = false;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getSong().getId().equals(recentSong.getSong().getId())) {
                        isAdd = true;
                        break;
                    }
                }
                if (isAdd == false) {
                    list.add(recentSong);
                }
            }
        }

        return list;
    }

    public RecentSong save(RecentSong recentSong) {
        try {
            String sql = "insert into recentSongs(song_id,username,dateListen) \n" +
                    "values(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, recentSong.getSong().getId());
            statement.setString(2, recentSong.getUser().getUsername());
            statement.setTimestamp(3, recentSong.getDateListen());
            int rowsInserted = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return recentSong;
    }
}
