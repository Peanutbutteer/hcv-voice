package com.example.kanokkornthepburi.newhcvvoice;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.kanokkornthepburi.newhcvvoice.Event.RefreshGraphEvent;
import com.example.kanokkornthepburi.newhcvvoice.Service.Client;
import com.example.kanokkornthepburi.newhcvvoice.Service.DeviceHistory;
import com.example.kanokkornthepburi.newhcvvoice.Service.History;
import com.example.kanokkornthepburi.newhcvvoice.Service.HistoryResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class GraphFragment extends Fragment {

    int[] colors = {
            Color.rgb(255, 128, 0),
            Color.GREEN,
            Color.RED,
            Color.CYAN
    };

    public static GraphFragment newInstance() {

        Bundle args = new Bundle();

        GraphFragment fragment = new GraphFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Call<HistoryResponse> call;

    @BindView(R.id.graphSpiner)
    Spinner graphSpiner;
    @BindView(R.id.graph)
    BarChart chart;
    String[] options;
    String selected = "day";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, view);
        setGraphSpiner();
        fetchHistoryData();
        return view;

    }

    public void setGraphSpiner() {
        options = getResources().getStringArray(R.array.selectGraph);
        ArrayAdapter<String> adapterGraph = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, options);
        adapterGraph.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        graphSpiner.setAdapter(adapterGraph);
        graphSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = options[position].toLowerCase();
                fetchHistoryData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fetchHistoryData() {
//        ArrayList<History> historis = new ArrayList<>();
//        List<DeviceHistory> dev = new ArrayList<>();
//        dev.add(new DeviceHistory("Fan", 4));
//        dev.add(new DeviceHistory("NoteBook", 3));
//        dev.add(new DeviceHistory("Phone", 2));
//        dev.add(new DeviceHistory("She", 1));
//        historis.add(new History("May 16", dev));
//        historis.add(new History("May 17", dev));
//        historis.add(new History("May 18", dev));
//        historis.add(new History("May 19", dev));
//        drawChart(historis);


        call = Client.getInstance().getService().getHistory(selected, UserData.getInstance().getActiveController());
        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if (response.isSuccessful()) {
                    drawChart(response.body().getHistories());
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null) {
            call.cancel();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe
    public void refreshGraphEvent(RefreshGraphEvent event) {
        fetchHistoryData();
    }

    public void drawChart(final ArrayList<History> histories) {
        if (histories == null || histories.size() == 0 || histories.get(0).getDeviceHistories() == null)
            return;

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        BarData data; // Dataset มารวมกันจะได้กราฟทั้งหมด
        Description des;
        XAxis xAxis;
        YAxis rightAxis;

        for (int i = 0; i < histories.get(0).getDeviceHistories().size(); i++) {
            List<BarEntry> entries = new ArrayList<>();
            for (History history : histories) {
                if (history.getDeviceHistories().get(i) != null) {
                    entries.add(new BarEntry(i, history.getDeviceHistories().get(i).getValue()));
                }
            }
            BarDataSet set = new BarDataSet(entries, histories.get(0).getDeviceHistories().get(i).getName());
            set.setValueTextSize(8);
            set.setColor(colors[i]);
            dataSets.add(set);
        }

        for (int i = 0; i < dataSets.size(); i++) {
            dataSets.get(i).setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    double hour = Math.floor(value / 3600);
                    double l_hour = value % 3600;
                    double minute = Math.floor(l_hour / 60);
                    double second = l_hour % 60;

                    if (hour == 0 && minute == 0 && second == 0)
                        return "";
                    else if (hour == 0 && minute == 0 && second != 0)
                        return (int) second + "s";
                    else if (hour == 0 && minute != 0)
                        return (int) minute + "m " + (int) second + "s";
                    else
                        return (int) hour + "histories " + (int) minute + "m ";
                }
            });
        }

        data = new BarData(dataSets);

        float groupSpace = 0.06f;
        float barSpace = 0f;
        float barWidth = 0.1f;

        data.setBarWidth(barWidth); // set the width of each bar
        chart.setData(data);
        chart.groupBars(0f, groupSpace, barSpace); // perform the "explicit" grouping
        chart.invalidate();
        chart.setVisibleXRangeMaximum((barWidth * dataSets.size() + groupSpace) * histories.size());
        chart.moveViewToX((barWidth * dataSets.size() + groupSpace) * histories.size() - (barWidth * dataSets.size() + groupSpace));

        des = new Description();
        des.setText("");
        chart.setDescription(des);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);

        xAxis = chart.getXAxis();
        xAxis.setTextSize(12);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(barWidth * dataSets.size() + groupSpace);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum((barWidth * dataSets.size() + groupSpace) * histories.size());
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            float groupSpace = 0.06f;
            float barWidth = 0.1f;
            float position = barWidth * 4 + groupSpace;

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if ((int) (value / position) >= 0 && (int) (value / position) < histories.size())
                    return histories.get((int) (value / position)).getDate();
                else
                    return "";
            }
        });

        rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                double hour = Math.floor(value / 3600);
                double l_hour = value % 3600;
                double minute = Math.floor(l_hour / 60);
                double second = l_hour % 60;

                if (hour == 0 && minute == 0 && second != 0)
                    return (int) second + "s";
                else if (hour == 0 && minute != 0)
                    return (int) minute + "m ";
                else if (hour != 0)
                    return (int) hour + "histories " + (int) minute + "m ";
                else
                    return "";
            }
        });


    }
}
