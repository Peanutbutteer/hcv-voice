package com.example.kanokkornthepburi.newhcvvoice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class DeviceAdapter extends RecyclerView.Adapter {
    private List<Device> devices;
    private OnChangeStatusDeviceListener mListener;
    private boolean disconnected = false;

    public void setListener(OnChangeStatusDeviceListener mListener) {
        this.mListener = mListener;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }


    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DeviceHolder) {
            DeviceHolder deviceHolder = (DeviceHolder) holder;
            deviceHolder.swStatus.setOnCheckedChangeListener(null);
            deviceHolder.name.setText(devices.get(position).getNameEng());
            if (!disconnected) {
                deviceHolder.swStatus.setClickable(true);
                deviceHolder.swStatus.setChecked(devices.get(position).getStatus());
                deviceHolder.swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (mListener != null) {
                            mListener.onChangeStatus(devices.get(position), b);
                        }
                    }
                });
            } else {
                deviceHolder.swStatus.setClickable(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return devices == null ? 0 : devices.size();
    }


    class DeviceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.sw_status)
        Switch swStatus;

        public DeviceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
