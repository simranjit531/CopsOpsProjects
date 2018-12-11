package com.copsopsapp.fragment.operatorpart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Assigning_an_Interventions extends Fragment implements View.OnClickListener {




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_assigning_an_intervention, container, false);

        ((MainActivity) getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVlogout.setVisibility(View.GONE);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        
        }


    }
}
