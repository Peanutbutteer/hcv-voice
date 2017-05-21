package com.example.kanokkornthepburi.newhcvvoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.kanokkornthepburi.newhcvvoice.Event.DisableDeviceEvent;
import com.example.kanokkornthepburi.newhcvvoice.Event.EnableDeviceEvent;
import com.example.kanokkornthepburi.newhcvvoice.Event.RefreshDeviceEvent;
import com.example.kanokkornthepburi.newhcvvoice.Service.Client;
import com.example.kanokkornthepburi.newhcvvoice.Service.Config;
import com.example.kanokkornthepburi.newhcvvoice.Service.MicroGearDevice;
import com.example.kanokkornthepburi.newhcvvoice.Service.MicroResponse;
import com.example.kanokkornthepburi.newhcvvoice.Utils.PrefUtils;
import com.example.kanokkornthepburi.newhcvvoice.Utils.PromptUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends PromptActivity implements MicrogearEventListener, OnChangeStatusDeviceListener {

    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.fab_mic)
    FloatingActionButton fabMic;
    @BindView(R.id.vp_main)
    ViewPager vpMain;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.clRoot)
    CoordinatorLayout clRoot;
    private Microgear microgear;
    private List<MicroGearDevice> microGearDeviceList = new ArrayList<>();

    Call<MicroResponse> microResponseCall;


    private void connectMicrogear() {
        if (microgear == null) {
            microgear = new Microgear(this);
            microgear.setCallback(this);
            microgear.disconnect();
            microgear.connect(Config.appid, Config.key, Config.secret, Config.alias);
            microgear.subscribe(Config.topicRead);
        }
    }

    private void refreshDeviceList() {
        microResponseCall = Client.getInstance().getService().microllers();
        microResponseCall.enqueue(new Callback<MicroResponse>() {
            @Override
            public void onResponse(Call<MicroResponse> call, Response<MicroResponse> response) {
                if (response.isSuccessful()) {
                    UserData.getInstance().setMicroControllers(response.body().getMicroControllers());
                }
            }

            @Override
            public void onFailure(Call<MicroResponse> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectMicrogear();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        refreshDeviceList();

        MainPageAdapter mainPageAdapter = new MainPageAdapter(getSupportFragmentManager());
        vpMain.setAdapter(mainPageAdapter);
        vpMain.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(vpMain);

        setSupportActionBar(toolbar);


        UserData.getInstance().setMicStatus(PrefUtils.getInstance().isOpenMic());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("HCvVoice");
            if (UserData.getInstance().getActiveController().isEmpty()) {
                getSupportActionBar().setSubtitle("Home: -");
            } else {
                getSupportActionBar().setSubtitle("Home: " + UserData.getInstance().getActiveController());
            }
        }

        fabMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptUtils.promptSpeechInput(MainActivity.this);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String sentence = result.get(0);
            showSnackBar(sentence);
            if (sentence.contains("รายงาน")) {
                vpMain.setCurrentItem(1);
            }
            if (sentence.contains("สั่งงาน")) {
                vpMain.setCurrentItem(0);
            }
            if (UserData.getInstance().getDevices() != null) {
                for (Device device : UserData.getInstance().getDevices()) {
                    if (sentence.contains(device.getNameThai())) {
                        boolean status = false;
                        if (sentence.contains("ปิด")) {
                            status = false;
                        }
                        if (sentence.contains("เปิด")) {
                            status = true;
                        }
                        Fragment fragment = getActiveFragment(vpMain, 0);
                        if (fragment instanceof VoiceControlsFragment) {
                            onChangeStatus(device, status);
                        }
                    }
                }
            }
            if (UserData.getInstance().getMicroControllers() != null) {
                for (MicroController microController : UserData.getInstance().getMicroControllers()) {
                    if (sentence.contains(microController.getNameThai())) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setSubtitle("Home: " + microController.getNameEng());
                            UserData.getInstance().setActiveController(microController.getNameEng());
                            refreshDevices();
                        }
                    }
                }
            }
        }
    }

    private void refreshDevices() {
        EventBus.getDefault().post(new RefreshDeviceEvent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (menu != null) {
            if (UserData.getInstance().isOpenMic()) {
                enableAutoPrompt(7000);
                menu.getItem(3).setIcon(getResources().getDrawable(R.drawable.ic_mic_black_24dp, getTheme()));
            } else {
                menu.getItem(3).setIcon(getResources().getDrawable(R.drawable.ic_mic_off_black_24dp, getTheme()));
                disableAutoPrompt();
            }
        }
        return true;
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return getSupportFragmentManager().findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_arduino:
                startActivity(MicroControllerActivity.getStartIntent(this));
                break;
            case R.id.action_device:
                startActivity(DevicesActivity.getStartIntent(this));
                break;
            case R.id.action_mic:
                UserData.getInstance().setMicStatus(!UserData.getInstance().isOpenMic());
                PrefUtils.getInstance().setMic(UserData.getInstance().isOpenMic());
                if (UserData.getInstance().isOpenMic()) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_mic_black_24dp, getTheme()));
                    enableAutoPrompt(7000);
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_mic_off_black_24dp, getTheme()));
                    disableAutoPrompt();
                }
                break;
            case R.id.action_change_home:
                if (UserData.getInstance().getMicroControllers() != null) {
                    item.getSubMenu().clear();
                    for (MicroController microController : UserData.getInstance().getMicroControllers()) {
                        item.getSubMenu().add(microController.getNameEng());
                    }
                }
                break;
            case R.id.action_logout:
                PrefUtils.getInstance().setLogout();
                startActivity(LoginActivity.getStartIntent(this));
                this.finish();
                break;
            default:
                for (MicroController microController : UserData.getInstance().getMicroControllers()) {
                    if (item.getTitle().equals(microController.getNameEng())) {
                        UserData.getInstance().setActiveController(microController.getNameEng());
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setSubtitle("Home: " + microController.getNameEng());
                        }
                        refreshDevices();
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        disableAutoPrompt();
        if (microResponseCall != null) {
            microResponseCall.cancel();
        }
    }


    @Override
    public void onConnect() {
        Log.e("onConnect", "");

    }

    @Override
    public void onMessage(String topic, String message) {
        Log.e("onMessage:message", message);
        Log.e("onMessage:topic", topic);
        if (message.contains(UserData.getInstance().getActiveController())) {
            EventBus.getDefault().post(new RefreshDeviceEvent());
            if (message.contains(UserData.getInstance().getUsername())) {
                int index = message.indexOf(' ', message.indexOf(' ') + 1);
                showSnackBar(message.substring(index, message.length() - 1));
            }
        }
    }

    @Override
    public void onPresent(String token) {
        Log.e("onPresent", token);
        convertMicroGearDevice(token);
    }

    @Override
    public void onAbsent(String token) {
        Log.e("onAbsent", token);
        convertMicroGearDevice(token);
    }

    private void convertMicroGearDevice(String token) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        MicroGearDevice device = gson.fromJson(token, MicroGearDevice.class);
        boolean isHas = true;
        for (int i = 0; i < microGearDeviceList.size(); i++) {
            if (microGearDeviceList.get(i).getAlias().equals(device.getAlias())) {
                microGearDeviceList.get(i).setType(device.getType());
                isHas = false;
            }
        }
        if (isHas) {
            microGearDeviceList.add(device);
        }

        for (int i = 0; i < microGearDeviceList.size(); i++) {
            if (microGearDeviceList.get(i).getAlias().equals(UserData.getInstance().getActiveController())) {
                if (microGearDeviceList.get(i).getType().equals("online")) {
                    EventBus.getDefault().post(new EnableDeviceEvent());
                } else {
                    EventBus.getDefault().post(new DisableDeviceEvent());
                }
            }
        }


    }

    @Override
    public void onDisconnect() {
        Log.e("onDisconnect", "");
        EventBus.getDefault().post(new DisableDeviceEvent());
    }

    @Override
    public void onError(String error) {
        Log.e("exception", "Exception : " + error);
        if (error.equals("connection Lost") || error.equals("service disconnect")) {
            EventBus.getDefault().post(new DisableDeviceEvent());

        } else {
            // Show Error
            showSnackBar(error);
        }
    }

    @Override
    public void onInfo(String info) {
        Log.e("exception", "Exception : " + info);
    }


    public void showSnackBar(String message) {
        Snackbar.make(clRoot, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onChangeStatus(Device device, boolean status) {
        if (!UserData.getInstance().getActiveController().isEmpty()) {
            microgear.chat(UserData.getInstance().getActiveController(), UserData.getInstance().getUsername() + " one " + device.getChannel() + (status ? " ON" : " OFF"));
        } else {
            // Show Error
            showSnackBar("Please select MicroController before make order.");
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
