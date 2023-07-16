package com.tranminhtri.test.geminimusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.dao.UserDAO;
import com.tranminhtri.test.geminimusicplayer.models.User;

public class Activity_Signup extends AppCompatActivity {
    private Button btnLogin;
    private TextView tv_back;
    private User user;
    private TextInputEditText edt_username, edt_password;
    private TextInputLayout layoutUserName, layoutPass;
    private boolean validUser = false, validPassWord = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnLogin = findViewById(R.id.btnLogin);
        tv_back = findViewById(R.id.tv_back_signup);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_pass);

        layoutPass = findViewById(R.id.passwordSignUpContainer);
        layoutUserName = findViewById(R.id.userSignUpContainer);

        edt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = String.valueOf(edt_username.getText().toString().trim());

                if(userName.equals("")){
                    layoutUserName.setHelperText("Không được bỏ trống");
                }
                else{
                    layoutUserName.setHelperText("Bắt buộc");
                    validUser = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = String.valueOf(edt_password.getText().toString().trim());

                if(password.equals("")){

                    layoutPass.setHelperText("Không được bỏ trống");
                }
                else{
                    if(password.length() > 16){

                        layoutPass.setHelperText("Mật khẩu quá dài");
                    }
                    else{
                        layoutPass.setHelperText("Bắt buộc");
                        validPassWord = true;
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(validUser && validPassWord)) {
                    return;
                }
                UserDAO userDAO = new UserDAO();
                user = userDAO.findByID(String.valueOf(edt_username.getText().toString().trim()));

                if (user != null) {
                      if (user.getPassword().equalsIgnoreCase(String.valueOf(edt_password.getText().toString().trim()))) {

                        User user1 = new User(user.getUsername());

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                        // Lưu trạng thái đăng nhập
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", user.getUsername());
                        editor.apply();
                        Intent intent = new Intent(Activity_Signup.this, Activity_Home.class);
                        intent.putExtra("user", user1);
                        startActivity(intent);
                    } else {
                       layoutPass.setHelperText("Sai mật khẩu");
                    }
                } else {
                   layoutUserName.setHelperText("Sai tài khoản");
                }


            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Signup.this, Activity_Login.class);
                startActivity(intent);
            }
        });
    }
}