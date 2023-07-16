package com.tranminhtri.test.geminimusicplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tranminhtri.test.geminimusicplayer.Adapter.ChooseArtistAdapter;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Choose_Artists;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_SignIn_Free;
import com.tranminhtri.test.geminimusicplayer.dao.ArtistsDAO;
import com.tranminhtri.test.geminimusicplayer.dao.User_FollowDAO;
import com.tranminhtri.test.geminimusicplayer.models.Artist;
import com.tranminhtri.test.geminimusicplayer.models.User;
import com.tranminhtri.test.geminimusicplayer.models.User_Follows;

import java.util.ArrayList;
import java.util.List;

public class Fragment_AddArtist extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ChooseArtistAdapter artistAdapter;
    private Button btnDone;
    private TextView tv_back;
    private List<Artist> listArtistNew = new ArrayList<>();
    private List<Artist> artistList = new ArrayList<>();
    private User user;
    private int height = 0;
    private Activity_Home activity_home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__add_artist, container, false);

        activity_home = (Activity_Home) getActivity();
        this.user = activity_home.getUser();
        recyclerView = view.findViewById(R.id.rcv_AddArtist);
        btnDone = view.findViewById(R.id.btnDoneAddArtist);

        artistList = getListArtist();
        artistAdapter = new ChooseArtistAdapter(activity_home, artistList, new ChooseArtistAdapter.OnItemClickListener() {

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

        tv_back = view.findViewById(R.id.tv_back_addArtist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity_home,3);
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
                for (Artist artist: listArtistNew) {
                    User_FollowDAO user_followDAO = new User_FollowDAO();
                    User_Follows user_follows = new User_Follows(artist,user);
                    user_followDAO.save(user_follows);
                }
                if (getFragmentManager() != null)

                    getFragmentManager().popBackStack();


            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null)

                    getFragmentManager().popBackStack();
            }
        });
        return view;
    }
    private List<Artist> getListArtist() {
        ArtistsDAO artistsDAO = new ArtistsDAO();


        User_FollowDAO user_followDAO = new User_FollowDAO();
        List<Artist> artists = new ArrayList<>();

        List<User_Follows> user_followsList = user_followDAO.findByID(user.getUsername());
        for (User_Follows user_follows : user_followsList) {
            artists.add(user_follows.getArtist());
        }
        List<Artist> artistList1 = artistsDAO.findAll();

        for (int i =0 ; i < artistList1.size(); i++) {
            for (Artist artist1: artists) {
                if(artistList1.get(i).getId().trim().equalsIgnoreCase(artist1.getId().trim())){
                    artistList1.remove(i);
                }
            }
        }

        return artistList1;
    }
}