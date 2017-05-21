package com.example.kanokkornthepburi.newhcvvoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;

import com.example.kanokkornthepburi.newhcvvoice.Service.Client;
import com.example.kanokkornthepburi.newhcvvoice.Service.LoginResponse;
import com.example.kanokkornthepburi.newhcvvoice.Utils.PrefUtils;
import com.example.kanokkornthepburi.newhcvvoice.Utils.PromptUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends PromptActivity implements View.OnClickListener {

    @BindView(R.id.et_email)
    EditText etUsername;
    @BindView(R.id.imageButton2)
    ImageButton voiceLogin;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.sw_img)
    ImageSwitcher swMic;
    @BindView(R.id.btn_login)
    Button btnLogin;

    Call<LoginResponse> responseCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefUtils.init(getApplicationContext());
        if (PrefUtils.getInstance().isLogin()) {
            UserData.getInstance().setUsername(PrefUtils.getInstance().getUsername());
            startActivity(MainActivity.getStartIntent(this));
            this.finish();
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        voiceLogin.setOnClickListener(this);

        swMic.setOnClickListener(this);
        UserData.getInstance().setMicStatus(PrefUtils.getInstance().isOpenMic());
        if (UserData.getInstance().isOpenMic()) {
            swMic.showNext();
            enableAutoPrompt(7000);
        }
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == voiceLogin) {
            PromptUtils.promptSpeechInput(LoginActivity.this);
        }
        if (view == swMic) {
            swMic.showNext();
            UserData.getInstance().setMicStatus(!UserData.getInstance().isOpenMic());
            PrefUtils.getInstance().setMic(UserData.getInstance().isOpenMic());
            if (UserData.getInstance().isOpenMic()) {
                enableAutoPrompt(7000);
            } else {
                disableAutoPrompt();
            }
        }
        if (view == btnLogin) {
            responseCall = Client.getInstance().getService().login("text", etUsername.getText().toString(), etPassword.getText().toString(), "");
            login();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (responseCall != null) {
            responseCall.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disableAutoPrompt();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String sentence = result.get(0);
            responseCall = Client.getInstance().getService().login("voice", etUsername.getText().toString(), etPassword.getText().toString(), sentence);
            login();
        }
    }

    private void login() {
        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult()) {
                        //Change Page
                        startActivity(MainActivity.getStartIntent(LoginActivity.this));
                        UserData.getInstance().setUsername(response.body().getUsername());
                        PrefUtils.getInstance().setUsername(response.body().getUsername());
                        LoginActivity.this.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}
