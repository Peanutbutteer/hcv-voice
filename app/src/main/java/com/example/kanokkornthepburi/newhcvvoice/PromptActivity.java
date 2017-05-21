package com.example.kanokkornthepburi.newhcvvoice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.kanokkornthepburi.newhcvvoice.Utils.PrefUtils;
import com.example.kanokkornthepburi.newhcvvoice.Utils.PromptUtils;

/**
 * Created by satjawatpanakarn on 5/21/2017 AD.
 */

public class PromptActivity extends AppCompatActivity {
    Handler handler = new Handler();
    private long time;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            PromptUtils.promptSpeechInput(PromptActivity.this);
        }
    };

    public void enableAutoPrompt(long time) {
        this.time = time;
        handler.postDelayed(runnable, time);
    }

    public void disableAutoPrompt() {
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (PrefUtils.getInstance().isOpenMic()) {
                enableAutoPrompt(7000);
            }
        }
    }
}
