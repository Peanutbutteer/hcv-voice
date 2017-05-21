package com.example.kanokkornthepburi.newhcvvoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.kanokkornthepburi.newhcvvoice.Service.ActionResponse;
import com.example.kanokkornthepburi.newhcvvoice.Service.Client;
import com.example.kanokkornthepburi.newhcvvoice.Service.MicroResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MicroControllerActivity extends AppCompatActivity implements MicroControllerAdapter.onClickListener {

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.rv_micro_controller)
    RecyclerView rvMicroController;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    MicroControllerAdapter microControllerAdapter;
    private MaterialDialog materialDialog;


    Call<MicroResponse> microResponseCall;
    Call<ActionResponse> actionResponseCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MicroController List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        microControllerAdapter = new MicroControllerAdapter();
        microControllerAdapter.setmListener(this);
        rvMicroController.setLayoutManager(new LinearLayoutManager(this));
        rvMicroController.setAdapter(microControllerAdapter);
        refreshList();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog = new MaterialDialog.Builder(MicroControllerActivity.this)
                        .title("Add MicroController")
                        .customView(R.layout.view_devices_edit, true)
                        .positiveText("Add")
                        .positiveColor(getResources().getColor(R.color.colorPrimary, getTheme()))
                        .negativeText("Cancel")
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
                                    AddMicroController(nameEng, nameThai);
                                }
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
    }

    private void AddMicroController(String nameEng, String nameThai) {
        actionResponseCall = Client.getInstance().getService().addMicroller(nameEng);
        actionResponseCall.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    if (status.equals("complete")) {
                        refreshList();
                    }
                    if (status.equals("not complete")) {
                    }
                    if (status.equals("duplicate value")) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {

            }
        });
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
        microResponseCall = Client.getInstance().getService().microllers();
        microResponseCall.enqueue(new Callback<MicroResponse>() {
            @Override
            public void onResponse(Call<MicroResponse> call, Response<MicroResponse> response) {
                if (response.isSuccessful()) {
                    microControllerAdapter.setMicroControllers(response.body().getMicroControllers());
                    microControllerAdapter.notifyDataSetChanged();
                    UserData.getInstance().setMicroControllers(response.body().getMicroControllers());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MicroResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MicroControllerActivity.class);
        return intent;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (microResponseCall != null) {
            microResponseCall.cancel();
        }
        if (actionResponseCall != null) {
            actionResponseCall.cancel();
        }
    }

    @Override
    public void onClickItem(final MicroController microController) {

        materialDialog = new MaterialDialog.Builder(MicroControllerActivity.this)
                .title("Edit MicroController")
                .customView(R.layout.view_devices_edit, true)
                .positiveText("Save")
                .negativeText("Cancel")
                .neutralText("Delete")
                .neutralColor(getResources().getColor(android.R.color.holo_red_light, getTheme()))
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
                            //Edit Change
                        }

                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        deleteMicroController(microController);
                    }
                })
                .build();

        if (materialDialog.getCustomView() != null) {
            View view = materialDialog.getCustomView();
            if (view.findViewById(R.id.et_name_en) != null) {
                ((EditText) view.findViewById(R.id.et_name_en)).setText(microController.getName());
            }
            if (view.findViewById(R.id.et_name_th) != null) {
                ((EditText) view.findViewById(R.id.et_name_th)).setText(microController.getName());
            }
        }

        materialDialog.show();


    }

    private void deleteMicroController(final MicroController microController) {
        new MaterialDialog.Builder(this)
                .content("This will delete Microcontroller " + microController.getName())
                .positiveText("Delete")
                .negativeText("Cancel")
                .positiveColor(getResources().getColor(android.R.color.holo_red_light, getTheme()))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        actionResponseCall = Client.getInstance().getService().deleteMicroller(microController.getId());
                        actionResponseCall.enqueue(new Callback<ActionResponse>() {
                            @Override
                            public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                                String status = response.body().getStatus();
                                if (status.equals("complete")) {
                                    refreshList();
                                }
                                if (status.equals("not complete")) {

                                }
                            }

                            @Override
                            public void onFailure(Call<ActionResponse> call, Throwable t) {

                            }
                        });
                    }
                })
                .show();

    }
}