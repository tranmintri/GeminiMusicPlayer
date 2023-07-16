package com.tranminhtri.test.geminimusicplayer.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.Manifest;
import com.tranminhtri.test.geminimusicplayer.R;
import com.tranminhtri.test.geminimusicplayer.activity.Activity_Home;
import com.tranminhtri.test.geminimusicplayer.dao.UserDAO;
import com.tranminhtri.test.geminimusicplayer.models.Playlist;
import com.tranminhtri.test.geminimusicplayer.models.Song;
import com.tranminhtri.test.geminimusicplayer.models.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Fragment_Edit_Profile extends BottomSheetDialogFragment  {
    private Activity_Home activity_home;
    private TextView tv_cancle, tv_save;
    private EditText edtName;
    private ImageView image_detail_profile;
    private TextView tv_updateImage;
    private byte[] image;
    private User user;
    private Fragment_Edit_Profile.OnDataChangeListener mDataChangeListener;
    private static final int PICK_IMAGE_REQUEST = 1;
    private final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private final int PERMISSION_CODE = 2;
    private Bitmap bitmap;

    // Khai báo biến của ActivityResultLauncher để sử dụng trong quá trình xin quyền truy cập bộ sưu tập ảnh
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    public static Fragment_Edit_Profile newInstance() {
        Fragment_Edit_Profile myBottomSheetDialog = new Fragment_Edit_Profile();


        return myBottomSheetDialog;
    }
    public void setOnDataChangeListener(Fragment_Edit_Profile.OnDataChangeListener listener) {
        mDataChangeListener = listener;
    }
    public void updateData() {
        // Do something to update data
        if (mDataChangeListener != null) {
            mDataChangeListener.onDataChanged(user);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment__edit__profile, null);
        activity_home = (Activity_Home) getActivity();
        user = activity_home.getUser();



        bottomSheetDialog.setContentView(view);
        initView(view);
        edtName.setText(String.valueOf(user.getFullname().trim()));

        bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);

        image_detail_profile.setImageBitmap(bitmap);
        image = user.getImage();

        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        tv_updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();

            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname = String.valueOf(edtName.getText().toString().trim());
                user.setFullname(fullname);
                user.setImage(image);

                UserDAO userDAO = new UserDAO();
                userDAO.update(user);
                updateData();
                bottomSheetDialog.dismiss();
            }
        });


        return bottomSheetDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Nếu đã được cấp quyền truy cập, thực hiện hành động cần thiết (ở đây là mở bộ sưu tập ảnh)
                pickImageFromGallery();
            }
        });
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Nếu lấy ảnh thành công, lấy đường dẫn của ảnh và hiển thị lên ImageView
                Uri uri = result.getData().getData();

                InputStream inputStream = null;
                try {
                    inputStream = getContext().getContentResolver().openInputStream(uri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                    }
                     image = byteArrayOutputStream.toByteArray();
                    if(image.length == 0){
                        image = user.getImage();
                    }


                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(image, 0, image.length);
                    if(bitmap1 == null){
                        bitmap1 = bitmap;
                    }
                    image_detail_profile.setImageBitmap(bitmap1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

// Convert input stream to byte array

            } else {
                // Nếu không lấy được ảnh, hiển thị thông báo cho người dùng
                Toast.makeText(getContext(), "Failed to pick image", Toast.LENGTH_SHORT).show();
            }
        });

        // Kiểm tra quyền truy cập bộ sưu tập ảnh
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Nếu đã được cấp quyền truy cập, thực hiện hành động cần thiết (ở đây là mở bộ sưu tập ảnh)
            pickImageFromGallery();
        } else {
            // Nếu chưa được cấp quyền truy cập, yêu cầu cấp quyền
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    // Hàm để mở bộ sưu tập ảnh
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }


    private void initView(View view) {
        tv_cancle = view.findViewById(R.id.tv_cancle);
        tv_save = view.findViewById(R.id.tv_save);
        edtName = view.findViewById(R.id.edt_name_profile);
        tv_updateImage = view.findViewById(R.id.tv_updateImage);
        image_detail_profile = view.findViewById(R.id.image_detail_profile);
    }
    public interface OnDataChangeListener {
        void onDataChanged(User user);
    }

}