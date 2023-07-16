package com.tranminhtri.test.geminimusicplayer.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.UserDAO;
import com.tranminhtri.test.geminimusicplayer.models.User;

public class Fragment_Account extends Fragment {

    private View view;
    private TextView tv_back;
    private Activity_Home activity_home;
    private User user;
    private TextView userName, email;
    private Button btnChangePass;
    private boolean validPass = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__account, container, false);
        tv_back = view.findViewById(R.id.tv_back_account);
        userName = view.findViewById(R.id.tv_username_account);
        btnChangePass = view.findViewById(R.id.btn_changepass);
        email = view.findViewById(R.id.tv_email_account);
        activity_home = (Activity_Home) getActivity();
        this.user = activity_home.getUser();

        userName.setText(user.getUsername());
        email.setText(user.getEmail());
        tv_back.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {

                if (getFragmentManager() != null) ;

                getFragmentManager().popBackStack();
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });


        return view;
    }

    private void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.login_dialog, null);
        final Button btnChange, btnCancle;

        TextInputLayout layoutUserName = alertLayout.findViewById(R.id.userSignUpContainerDialog);
        TextInputLayout layoutPass = alertLayout.findViewById(R.id.passwordSignUpContainerDialog);
        TextInputEditText edtUserName = alertLayout.findViewById(R.id.edt_usernameDialog);
        TextInputEditText edtPassword = alertLayout.findViewById(R.id.edt_passDialog);

        btnChange = alertLayout.findViewById(R.id.btnChangPassDialog);
        btnCancle = alertLayout.findViewById(R.id.btnCancleDialog);

        AlertDialog.Builder alert = new AlertDialog.Builder(activity_home);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        edtUserName.setText(user.getUsername());
        dialog.show();
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = String.valueOf(edtPassword.getText().toString().trim());

                if(password.equals("")){

                    layoutPass.setHelperText("Không được bỏ trống");
                }
                else{
                    if(password.length() > 16){

                        layoutPass.setHelperText("Mật khẩu quá dài");
                    }
                    else{
                        layoutPass.setHelperText("Bắt buộc");
                        validPass = true;
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validPass){
                    String password = String.valueOf(edtPassword.getText().toString().trim());
                    UserDAO userDAO = new UserDAO();
                    user.setPassword(password);
                    User user1 = userDAO.update(user);
                    if(user1 != null){
                        Toast.makeText(activity_home,"Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}