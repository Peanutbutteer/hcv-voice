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

public class MicroControllerAdapter extends RecyclerView.Adapter {

    private List<MicroController> microControllers;
    private onClickListener mListener;

    public void setMicroControllers(List<MicroController> microControllers) {
        this.microControllers = microControllers;
    }

    public void setmListener(onClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_micro_controller, parent, false);
        return new MicroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MicroViewHolder) holder).tvName.setText(microControllers.get(position).getNameEng());
        ((MicroViewHolder) holder).llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(microControllers.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return microControllers == null ? 0 : microControllers.size();
    }

    class MicroViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.llRoot)
        LinearLayout llRoot;

        public MicroViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onClickListener {
        void onClickItem(MicroController microController);
    }
}
