package com.tranminhtri.test.geminimusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.tranminhtri.test.geminimusicplayer.connection.SqlConnection;
import com.tranminhtri.test.geminimusicplayer.dao.ArtistsDAO;
import com.tranminhtri.test.geminimusicplayer.dao.UserDAO;
import com.tranminhtri.test.geminimusicplayer.dao.User_FollowDAO;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.Adapter.ChooseArtistAdapter;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.models.User;
import com.tranminhtri.test.geminimusicplayer.models.User_Follows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Activity_Choose_Artists extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChooseArtistAdapter artistAdapter;
    private Button btnDone;
    private TextView tv_back;
    private List<Artist> listArtistNew = new ArrayList<>();
    private List<Artist> artistList = new ArrayList<>();
    private User user;
    private int height = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_artists);
        User userPut = getIntent().getParcelableExtra("user");
        UserDAO userDAO = new UserDAO();
        this.user = userDAO.findByID(userPut.getUsername());
        System.out.println(user.getUsername());
        recyclerView = findViewById(R.id.rcv_artist);
        btnDone = findViewById(R.id.btnDone);

        artistList = getListArtist();
        artistAdapter = new ChooseArtistAdapter(this, artistList, new ChooseArtistAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, boolean isChecked) {
                if(isChecked){
                    listArtistNew.add(artistList.get(position));
                }
                else{
                    listArtistNew.remove(artistList.get(position));
                }
            }
        });

        tv_back = findViewById(R.id.tv_back_chooseArtist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(artistAdapter);

        for (int i = 0; i < getListArtist().size(); i++) {
            height+= 200;
        }
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = height;
        recyclerView.setLayoutParams(params);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listArtistNew.size() == 0)
                    return;
                for (Artist artist: listArtistNew) {
                    User_FollowDAO user_followDAO = new User_FollowDAO();
                    User_Follows user_follows = new User_Follows(artist,user);
                    user_followDAO.save(user_follows);
                }
                Intent intent = new Intent(Activity_Choose_Artists.this, Activity_Home.class);
                User user1 = new User(user.getUsername());
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                // Lưu trạng thái đăng nhập
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("username", user.getUsername());
                editor.apply();
                intent.putExtra("user",user1);
                startActivity(intent);
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Choose_Artists.this, Activity_SignIn_Free.class);
                startActivity(intent);
            }
        });
    }


    private List<Artist> getListArtist() {
        List<Artist> list = new ArrayList<>();

        ArtistsDAO artistsDAO = new ArtistsDAO();

        return artistsDAO.findAll();
    }


}