package com.example.kanokkornthepburi.newhcvvoice.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class PromptUtils {
    public static void promptSpeechInput(Activity activity) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "th-TH");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "say something");
        try {
            activity.startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(activity, "sorry your device no support", Toast.LENGTH_LONG).show();
        }
    }
}