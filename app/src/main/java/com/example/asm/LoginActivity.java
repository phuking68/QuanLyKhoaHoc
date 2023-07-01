package com.example.asm;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asm.LoginWithEmail.LoginWithEmailActivity;
import com.example.asm.Service.LoginService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    IntentFilter intentFilter;
    CheckBox chkRemember;
    String user;
    String pass;
    LoginButton btnLoginFB;
    CallbackManager callbackManager;
    SignInButton btnLoginGoogle;

    GoogleSignInClient mGoogleSignInClient;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private boolean showOneTapUI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginFB = findViewById(R.id.btnLoginFB);
        EditText edtUser = findViewById(R.id.edtUser);
        EditText edtPass = findViewById(R.id.edtPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        chkRemember = findViewById(R.id.chkRemember);
        TextView tvLoginEmail = findViewById(R.id.tvLoginEmail);
        TextView tvLoginPhone = findViewById(R.id.tvLoginPhone);

        //login với phone number
        tvLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginWithPhoneActivity.class));
            }
        });

        //login với email
        tvLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginWithEmailActivity.class));
            }
        });

        //login với google
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();


        //login với facebook
        callbackManager = CallbackManager.Factory.create();
        //check người dùng đã đăng nhập trước đó hay chưa?
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            getUserProfile(accessToken);
            Toast.makeText(this, "Đã đăng nhập", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }

        //nút đăng nhập/logout facebook
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Hủy đăng nhập", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Lỗi trong quá trình đăng nhập", Toast.LENGTH_SHORT).show();
            }
        });


        //nút đăng nhập google
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult beginSignInResult) {
                                try {
                                    IntentSenderRequest intentSenderRequest =
                                            new IntentSenderRequest.Builder(beginSignInResult.getPendingIntent().getIntentSender()).build();
                                    startActivityForResult.launch(intentSenderRequest);
                                } catch (Exception e) {
                                    Log.e(TAG, "" + e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: ", e);
                            }
                        });
            }
        });


        intentFilter = new IntentFilter();
        intentFilter.addAction("kiemTraDangNhap");

        //check giá trị isRemember đã lưu
        SharedPreferences sharedPreferences = getSharedPreferences("dataUser", MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("isRemember", false);
        if (check) {
            String user = sharedPreferences.getString("user", "");
            String pass = sharedPreferences.getString("pass", "");
            chkRemember.setChecked(true);
            edtUser.setText(user);
            edtPass.setText(pass);
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = edtUser.getText().toString();
                pass = edtPass.getText().toString();
                boolean isRemember = chkRemember.isChecked();

                Intent intent = new Intent(LoginActivity.this, LoginService.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", user);
                bundle.putString("pass", pass);
                bundle.putBoolean("isRemember", isRemember);
                intent.putExtras(bundle);
                startService(intent);

                if (user.length() > 0 && pass.length() > 0) {
                    //lấy giá trị của checkbox
                    SharedPreferences sharedPreferences = getSharedPreferences("dataUser", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isRemember", isRemember);
                    editor.putString("user", user);
                    editor.putString("pass", pass);
                    editor.apply();
                } else {
                    Toast.makeText(LoginActivity.this, "Nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myBroadcas, intentFilter);
    }

    //tạo BroadcasReceiver
    public BroadcastReceiver myBroadcas = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //nhận data
            switch (intent.getAction()) {
                case "kiemTraDangNhap":
                    Bundle bundle = intent.getExtras();
                    boolean check = bundle.getBoolean("check");
                    if (check) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(context, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //google

    ActivityResultLauncher<IntentSenderRequest> startActivityForResult = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override

                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {

                            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                            String idToken = credential.getGoogleIdToken();
                            if (idToken != null) {
                                //Thông tin email
                                String email = credential.getId();
                                //Password
                                String name = credential.getDisplayName();
                                //Chúng ta có thể lẩy ảnh của người dùng, và dùng thư viện glide để show ảnh lên UI
                                Uri avatar = credential.getProfilePictureUri();

                                Toast.makeText(LoginActivity.this, "email: " + email + "\n" + "name: " + name, Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onFailure: ", e);
                        }
                    } else {

                    }
                }
            });


    //chuyển kết quả đăng nhập vào LoginManager qua callbackManager.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static PendingIntent createPendingIntentGetActivity(Context context, int id, Intent intent, int flag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_MUTABLE | flag);
        } else {
            return PendingIntent.getActivity(context, id, intent, flag);
        }
    }

    //Lấy thông tin người dùng khi đăng nhập thành công
    private void getUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("id");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    Toast.makeText(LoginActivity.this, name + " - " + email + " - " + image, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }
}