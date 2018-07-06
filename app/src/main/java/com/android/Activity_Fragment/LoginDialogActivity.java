package com.android.Activity_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.API.APIFunction;
import com.android.API.Response;
import com.android.Models.User;
import com.android.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class LoginDialogActivity extends AppCompatActivity  implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener  {
    final static String TAG = LoginDialogActivity.class.getSimpleName();
    private RelativeLayout rlFacebook;
    private RelativeLayout rlGoogle;
    private ImageView quitDialog;
    //dialog wait
    private SpotsDialog progressDialog;

    private APIFunction apiFunction = new APIFunction();

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    GoogleApiClient googleApiClient;
    static final int RC_SIGN_IN = 9001;
    FirebaseUser user;
    //facebook
    CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dialog);
        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        this.setFinishOnTouchOutside(true);

        Date today = new Date(System.currentTimeMillis());
        DateFormat timeFormat = SimpleDateFormat.getDateTimeInstance();
        // xét layout cho dialog
        setContentView(R.layout.layout_login_new);
        rlFacebook = findViewById(R.id.rlFacebook);
        rlGoogle = findViewById(R.id.rlGoogle);
        quitDialog = findViewById(R.id.quitDialog);

        rlFacebook.setOnClickListener(this);
        rlGoogle.setOnClickListener(this);
        quitDialog.setOnClickListener(this);
        progressDialog = new SpotsDialog(this,R.style.CustomAlertDialog);

        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(TAG, "onAuthStateChanged:: signined_id:  "+firebaseUser.getUid());
                    updateUI(user);
                }else{
                    Log.d(TAG, "onAuthStateChanged:: sign_out");
                }
            }
        };

        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: "+connectionResult);
        Toast.makeText(this, "Google Play Service error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{
                updateUI(null);
            }
        }
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void onLogin(){
        Response response = apiFunction.loginUser(new User("123123", "asdsad", "asdasd", "asdsad", "asdasd", "1"));
        if (response != null) {
            if (response.isSuccess()) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "login: "+response.getMessage());
        } else {
            Toast.makeText(this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: "+account.getId());
        progressDialog.show();

        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                            try{
                                throw  task.getException();

                            }
                            catch (FirebaseNetworkException e){
                                Toast.makeText(LoginDialogActivity.this, "Chưa kết nối mạng", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                Toast.makeText(LoginDialogActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    private void signinWithGoogle(){
        revokeAccess();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void sigoutGoogle(){
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(null);
            }
        });
    }

    private void revokeAccess(){
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(null);
            }
        });
    }

    private void updateUI(FirebaseUser user){
        if (user != null && user.isEmailVerified()) {
            Toast.makeText(this, "login success ", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "log unsuccess ", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginDialogActivity.this, "success", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            try {
                                throw  task.getException();
                            }
                            catch (FirebaseNetworkException e){
                                Toast.makeText(LoginDialogActivity.this, "Chưa kết nối mạng", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                Toast.makeText(LoginDialogActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlFacebook:
                progressDialog.show();
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError"+ error);
                        progressDialog.dismiss();
                        if(error.getMessage().toString().contains("CONNECTION_FAILURE")){
                            Toast.makeText(LoginDialogActivity.this, "Chưa kết nối mạng", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
                break;
            case R.id.rlGoogle:
                signinWithGoogle();
                break;
            case R.id.quitDialog:
                finish();
                break;
        }
    }
}
