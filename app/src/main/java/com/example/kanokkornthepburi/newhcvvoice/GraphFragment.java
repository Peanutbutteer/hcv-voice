package com.example.kanokkornthepburi.newhcvvoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class GraphFragment extends Fragment {

    public static GraphFragment newInstance() {
        
        Bundle args = new Bundle();
        
        GraphFragment fragment = new GraphFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
