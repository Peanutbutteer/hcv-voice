package com.example.kanokkornthepburi.newhcvvoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.kanokkornthepburi.newhcvvoice.Event.RefreshDeviceEvent;
import com.example.kanokkornthepburi.newhcvvoice.Service.ActionResponse;
import com.example.kanokkornthepburi.newhcvvoice.Service.Client;
import com.example.kanokkornthepburi.newhcvvoice.Service.DevicesResponse;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevicesActivity extends AppCompatActivity implements DeviceSettingAdapter.onClickListener {

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.rv_devices)
    RecyclerView rvDevices;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private DeviceSettingAdapter deviceAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.clRoot)
    CoordinatorLayout clRoot;

    private MaterialDialog materialDialog;
    Call<DevicesResponse> devicesResponseCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        ButterKnife.bind(this);
        deviceAdapter = new DeviceSettingAdapter();
        deviceAdapter.setListener(this);
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        rvDevices.setAdapter(deviceAdapter);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Device List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog.Builder(DevicesActivity.this)
                        .title("Add Device")
                        .customView(R.layout.view_devices_edit, true)
                        .positiveText("Add")
                        .negativeText("Cancel")
                        .positiveColor(getResources().getColor(R.color.colorPrimary, getTheme()))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String nameEng = "";
                                String nameThai = "";
                                if (materialDialog.getCustomView() != null) {
                                    View view = materialDialog.getCustomView();
                                    if (view.findViewById(R.id.et_name_en) != null) {
                                        nameEng = ((EditText) view.findViewById(R.id.et_name_en)).getText().toString();
                                    }
                                    if (view.findViewById(R.id.et_name_th) != null) {
                                        nameThai = ((EditText) view.findViewById(R.id.et_name_th)).getText().toString();
                                    }
                                }

                                Client.getInstance().getService().addDevice(UserData.getInstance().getActiveController(), nameEng, nameThai)
                                        .enqueue(new Callback<ActionResponse>() {
                                            @Override
                                            public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                                                String status = response.body().getStatus();
                                                checkStatus(status);
                                            }

                                            @Override
                                            public void onFailure(Call<ActionResponse> call, Throwable t) {

                                            }
                                        });
                            }
                        }).build();
                materialDialog.show();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        refreshList();
    }

    public void showSnackBar(String message) {
        Snackbar.make(clRoot, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshList() {
        devicesResponseCall = Client.getInstance().getService().devices(UserData.getInstance().getActiveController());
        devicesResponseCall.enqueue(new Callback<DevicesResponse>() {
            @Override
            public void onResponse(Call<DevicesResponse> call, Response<DevicesResponse> response) {
                if (response.isSuccessful()) {
                    deviceAdapter.setDevices(response.body().getDevices());
                    deviceAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DevicesResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, DevicesActivity.class);
        return intent;
    }

    @Override
    public void onClickItem(final Device device) {
        materialDialog = new MaterialDialog.Builder(DevicesActivity.this)
                .title("Edit Device")
                .customView(R.layout.view_devices_edit, true)
                .positiveText("Save")
                .negativeText("Cancel")
                .positiveColor(getResources().getColor(R.color.colorPrimary, getTheme()))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String nameEng = "";
                        String nameThai = "";
                        if (materialDialog.getCustomView() != null) {
                            View view = materialDialog.getCustomView();
                            if (view.findViewById(R.id.et_name_en) != null) {
                                nameEng = ((EditText) view.findViewById(R.id.et_name_en)).getText().toString();
                            }
                            if (view.findViewById(R.id.et_name_th) != null) {
                                nameThai = ((EditText) view.findViewById(R.id.et_name_th)).getText().toString();
                            }
                        }

                        // Save

                        Client.getInstance().getService().editDevice(device.getId(), nameEng, nameThai)
                                .enqueue(new Callback<ActionResponse>() {
                                    @Override
                                    public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                                        String status = response.body().getStatus();
                                        checkStatus(status);
                                    }

                                    @Override
                                    public void onFailure(Call<ActionResponse> call, Throwable t) {

                                    }
                                });
                    }
                }).build();
        if (materialDialog.getCustomView() != null) {
            View view = materialDialog.getCustomView();
            if (view.findViewById(R.id.et_name_en) != null) {
                ((EditText) view.findViewById(R.id.et_name_en)).setText(device.getNameEng());
            }
            if (view.findViewById(R.id.et_name_th) != null) {
                ((EditText) view.findViewById(R.id.et_name_th)).setText(device.getNameThai());
            }
        }
        materialDialog.show();
    }

    private void checkStatus(String status) {
        if (status.equals("complete")) {
            refreshList();
            EventBus.getDefault().post(new RefreshDeviceEvent());
            showSnackBar("Completed");
        }
        if (status.equals("not complete")) {
            showSnackBar("Can not Add");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (devicesResponseCall != null) {
            devicesResponseCall.cancel();
        }
    }
}
