package com.example.kanokkornthepburi.newhcvvoice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kanokkornthepburi.newhcvvoice.Event.DisableDeviceEvent;
import com.example.kanokkornthepburi.newhcvvoice.Event.EnableDeviceEvent;
import com.example.kanokkornthepburi.newhcvvoice.Event.RefreshDeviceEvent;
import com.example.kanokkornthepburi.newhcvvoice.Service.Client;
import com.example.kanokkornthepburi.newhcvvoice.Service.DevicesResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class VoiceControlsFragment extends Fragment implements OnChangeStatusDeviceListener {

    @BindView(R.id.rv_devices)
    RecyclerView rvDevices;
    DeviceAdapter deviceAdapter;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;


    Call<DevicesResponse> devicesResponseCall;
    private OnChangeStatusDeviceListener onChangeStatusDeviceListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice_controls, container, false);
        ButterKnife.bind(this, view);
        initInstance();
        return view;
    }

    private void initInstance() {
        deviceAdapter = new DeviceAdapter();
        deviceAdapter.setListener(this);
        rvDevices.setAdapter(deviceAdapter);
        rvDevices.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDevicesList();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        refreshDevicesList();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void refreshDevice(RefreshDeviceEvent event) {
        refreshDevicesList();
    }

    @Subscribe
    public void disableDevice(DisableDeviceEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceAdapter.setDisconnected(true);
                deviceAdapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe
    public void enableDevice(EnableDeviceEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceAdapter.setDisconnected(false);
                deviceAdapter.notifyDataSetChanged();
            }
        });
    }

    public void refreshDevicesList() {
        devicesResponseCall = Client.getInstance().getService().devices(UserData.getInstance().getActiveController());
        devicesResponseCall.enqueue(new Callback<DevicesResponse>() {
            @Override
            public void onResponse(Call<DevicesResponse> call, Response<DevicesResponse> response) {
                if (response.isSuccessful()) {
                    deviceAdapter.setDevices(response.body().getDevices());
                    UserData.getInstance().setDevices(response.body().getDevices());
                    deviceAdapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DevicesResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (devicesResponseCall != null) {
            devicesResponseCall.cancel();
        }
    }


    public static VoiceControlsFragment newInstance() {

        Bundle args = new Bundle();

        VoiceControlsFragment fragment = new VoiceControlsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChangeStatusDeviceListener) {
            onChangeStatusDeviceListener = (OnChangeStatusDeviceListener) context;
        }
    }

    @Override
    public void onChangeStatus(Device device, boolean status) {
        if (onChangeStatusDeviceListener != null) {
            onChangeStatusDeviceListener.onChangeStatus(device, status);
        }
    }
}


