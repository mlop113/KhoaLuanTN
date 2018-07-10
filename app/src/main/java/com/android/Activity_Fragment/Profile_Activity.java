package com.android.Activity_Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.API.APIFunction;
import com.android.Global.AppPreferences;
import com.android.Models.User;
import com.android.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ngoc Vu on 1/2/2018.
 */

public class Profile_Activity extends AppCompatActivity {
    AppPreferences appPreferences;
    TextView txt_Logout, txtname, txtaddress, txtphone;
    ImageView user_profile_photo;
    private String u;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile_user);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
        }
        txt_Logout = findViewById(R.id.profile_logout);
        txtname = findViewById(R.id.user_profile_name);
        txtaddress = findViewById(R.id.user_profile_address);
        txtphone = findViewById(R.id.information_phone);
        user_profile_photo = findViewById(R.id.user_profile_photo);
        txt_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(Profile_Activity.this);
                builder.setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất tài khoản?")
                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                logout();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(user_profile_photo);
        txtname.setText(firebaseUser.getDisplayName());
        txtaddress.setText(firebaseUser.getEmail());
        APIFunction apiFunction = APIFunction.getInstance();
        User user = apiFunction.getUser_byID(firebaseUser.getUid());
        int level = Integer.parseInt(user.getUser_LevelID());
        String levelName = "";
        if (level == 1) {
            levelName = "Thành viên";
        } else if (level == 2) {
            levelName = "Thành viên Vip";
        }
        txtphone.setText(levelName);
    }

    private void logout() {
        firebaseAuth.signOut();
        firebaseUser = null;
        finish();
    }

}
