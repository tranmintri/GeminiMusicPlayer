package com.tranminhtri.test.geminimusicplayer.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.PlaylistDAO;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.User;

import java.util.Date;

public class Fragment_BottomDialog_AddPlaylist  extends BottomSheetDialogFragment {
    private Activity_Home activity_home;
    private TextView tv_close;
    private Button btn_createPlaylist;
    private EditText edtPlayListName;
    private OnDataChangeListener mDataChangeListener;
    private Playlist playlist;
    private View view;
    public static Fragment_BottomDialog_AddPlaylist newInstance() {
        Fragment_BottomDialog_AddPlaylist myBottomSheetDialog = new Fragment_BottomDialog_AddPlaylist();


        return myBottomSheetDialog;
    }


    public void setOnDataChangeListener(OnDataChangeListener listener) {
        mDataChangeListener = listener;
    }
    public void updateData() {
        // Do something to update data
        if (mDataChangeListener != null) {
            mDataChangeListener.onDataChanged(playlist);

        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
         view = LayoutInflater.from(getContext()).inflate(R.layout.fragment__bottom_dialog__add_playlist, null);
        activity_home = (Activity_Home) getActivity();
        bottomSheetDialog.setContentView(view);
        initView(view);

        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        btn_createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf( edtPlayListName.getText().toString().trim());
                String id = String.valueOf(System.currentTimeMillis());
                Date date = new Date(System.currentTimeMillis());
                User user = activity_home.getUser();
                 playlist = new Playlist(id,name,date,user);
                 mDataChangeListener.onDataChanged(playlist);
                PlaylistDAO playlistDAO = new PlaylistDAO();

                Playlist playlist1 = playlistDAO.save(playlist);
//                updateData();
                bottomSheetDialog.dismiss();
            }
        });

        return bottomSheetDialog;
    }
    public interface OnDataChangeListener {
        void onDataChanged(Playlist playlist);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    private void initView(View view) {
        tv_close = view.findViewById(R.id.tv_close_create_pl);
        btn_createPlaylist = view.findViewById(R.id.btn_createPlaylist);
        edtPlayListName = view.findViewById(R.id.edtPlayListName);
    }

}