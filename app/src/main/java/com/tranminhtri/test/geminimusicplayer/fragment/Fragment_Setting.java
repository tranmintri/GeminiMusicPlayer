package com.tranminhtri.test.geminimusicplayer.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Login;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDetailsDAO;
import com.tranminhtri.test.geminimusicplayer.models.PlaylistDetail;
import com.tranminhtri.test.geminimusicplayer.models.User;


public class Fragment_Setting extends Fragment {

    private View view;
    private TextView tv_back;
    private TextView tv_Username;
    private ImageView iv_ImageUser;
    private LinearLayout linearLayout;
    private Activity_Home activity_home;
    private Button btnLogout, btnAccount, btnIntroduct;
    private User user;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_setting, container, false);
        activity_home = (Activity_Home) getActivity();
        tv_back = view.findViewById(R.id.tv_back_setting);
        tv_Username = view.findViewById(R.id.tv_Username);
        iv_ImageUser = view.findViewById(R.id.iv_ImageUser);
        linearLayout = view.findViewById(R.id.profile);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAccount = view.findViewById(R.id.btn_taiKhoan);
        btnIntroduct = view.findViewById(R.id.btn_introduct);

        user = activity_home.getUser();
        tv_Username.setText(user.getFullname());

        Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
        iv_ImageUser.setImageBitmap(bitmap);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer = activity_home.getMediaPlayer();
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        SharedPreferences sharedPreferences = activity_home.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                        // Lưu trạng thái đăng nhập
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.apply();
                        Intent intent = new Intent(activity_home, Activity_Login.class);
                        startActivity(intent);

                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = activity_home.getSupportFragmentManager();
                Fragment_Profile fragment_profile = new Fragment_Profile();
                manager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_profile).addToBackStack(fragment_profile.getTag()).commit();

            }
        });
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = activity_home.getSupportFragmentManager();
                Fragment_Account fragment_account = new Fragment_Account();
                manager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_account).addToBackStack(fragment_account.getTag()).commit();

            }
        });
        btnIntroduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = activity_home.getSupportFragmentManager();
                Fragment_Introduct fragment_introduct = new Fragment_Introduct();
                manager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right).replace(R.id.container, fragment_introduct).addToBackStack(fragment_introduct.getTag()).commit();

            }
        });


        tv_back.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {

                if (getFragmentManager() != null)

                    getFragmentManager().popBackStack();
            }
        });

        return view;
    }
}