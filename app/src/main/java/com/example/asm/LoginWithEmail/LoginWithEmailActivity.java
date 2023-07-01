package com.example.asm.LoginWithEmail;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.asm.HomeActivity;
import com.example.asm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginWithEmailActivity extends AppCompatActivity {

    CheckBox chkRemember;
    String email;
    String password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        TextInputEditText edtUser = findViewById(R.id.edtUser);
        TextInputEditText edtPass = findViewById(R.id.edtPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnbtnToRegis = findViewById(R.id.btnToRegis);
        chkRemember = findViewById(R.id.chkRemember);

        mAuth = FirebaseAuth.getInstance();

        //check giá trị isRemember đã lưu
        SharedPreferences sharedPreferences = getSharedPreferences("dataEmail", MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("isRemember", false);
        if (check) {
            String email = sharedPreferences.getString("email", "");
            String pass = sharedPreferences.getString("passemail", "");
            chkRemember.setChecked(true);
            edtUser.setText(email);
            edtPass.setText(pass);
        }

        btnbtnToRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithEmailActivity.this, RegisterEmailActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtUser.getText().toString();
                password = edtPass.getText().toString();
                boolean isRemember = chkRemember.isChecked();

                if (email.length() > 0 && email.length() > 0) {
                    //lấy giá trị của checkbox
                    SharedPreferences sharedPreferences = getSharedPreferences("dataEmail", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isRemember", isRemember);
                    editor.putString("email", email);
                    editor.putString("passemail", password);
                    editor.apply();
                } else {
                    Toast.makeText(LoginWithEmailActivity.this, "Nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginWithEmailActivity.this, "Vui lòng nhập Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginWithEmailActivity.this, "Vui lòng nhập Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //lấy thông tin tài khoản mới vừa đăng ký
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginWithEmailActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginWithEmailActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginWithEmailActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


    }
}