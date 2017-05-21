package com.example.kanokkornthepburi.newhcvvoice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kanokkornthepburi on 5/21/2017 AD.
 */

public class DeviceSettingAdapter extends RecyclerView.Adapter {
    private List<Device> devices;
    private onClickListener mListener;

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void setListener(onClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_setting, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DeviceViewHolder) {
            ((DeviceViewHolder) holder).llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onClickItem(devices.get(position));
                    }
                }
            });
            ((DeviceViewHolder) holder).tvName.setText(devices.get(position).getNameEng() + " (" + devices.get(position).getNameThai() + ")");
        }
    }

    @Override
    public int getItemCount() {
        return devices == null ? 0 : devices.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.llRoot)
        LinearLayout llRoot;

        DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onClickListener {
        void onClickItem(Device device);
    }
}
