package com.tranminhtri.test.geminimusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayOutputStream;

public class Activity_SignIn_Free extends AppCompatActivity {

    private Button btnContinue;
    private TextView tv_back;
    private TextInputEditText edtUserName, edtPass,edtEmail;
    private TextInputLayout layoutUserName, layoutEmail, layoutPass;
    private boolean validEmail = false;
    private boolean validPassWord = false;
    private boolean validUser = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_free);



        btnContinue = findViewById(R.id.continue_free_screen);
        tv_back = findViewById(R.id.tv_back_signin);
        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPass);
        edtEmail = findViewById(R.id.edtEmail);

        layoutEmail = findViewById(R.id.emailContainer);
        layoutUserName = findViewById(R.id.userNameContainer);
        layoutPass = findViewById(R.id.passwordContainer);



        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = String.valueOf(edtUserName.getText().toString().trim());

                if(username.equals("")){
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
        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = String.valueOf(edtPass.getText().toString().trim());

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
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = String.valueOf(edtEmail.getText().toString().trim());
                if(email.equals("")){
                    layoutEmail.setHelperText("Không được bỏ trống");
                }
                else if(!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@gmail.com$")){

                    layoutEmail.setHelperText("Email không đúng định dạng (gmail)");
                }
                else{
                    layoutEmail.setHelperText("Bắt buộc");
                    validEmail = true;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = String.valueOf(edtUserName.getText().toString().trim());
                String password = String.valueOf(edtPass.getText().toString().trim());
                String email = String.valueOf(edtEmail.getText().toString().trim());
                String fullname = username;
            
                byte[] image = convertImage();

                if(!(validUser && validPassWord && validEmail))
                    return;

                User user = new User(username,fullname,password,email,"Việt Name",image);
                UserDAO userDAO = new UserDAO();
                User user1 = userDAO.save(user);

                if(user1 != null){
                    Intent intent = new Intent(Activity_SignIn_Free.this, Activity_Choose_Artists.class);
                    User user2 = new User(user1.getUsername());
                    intent.putExtra("user", user2);
                    Toast.makeText(Activity_SignIn_Free.this,"Tạo tài khoản thành công",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Activity_SignIn_Free.this,"Tạo tài khoản thất bại",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_SignIn_Free.this, Activity_Login.class);
                startActivity(intent);
            }
        });
    }
    public byte[] convertImage(){
        Drawable drawable = getResources().getDrawable(R.drawable.avtuser);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}