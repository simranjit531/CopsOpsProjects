package com.copsopsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Report_an_Incidents extends Fragment implements View.OnClickListener {

    private RelativeLayout Rlpoliceincident,Rlmedicalfiremanincident,Rlcityincident;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_report_an_incidents,container,false);

        ((MainActivity)getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).IVlogout.setVisibility(View.GONE);

        Rlpoliceincident=(RelativeLayout)view.findViewById(R.id.Rlpoliceincident);
        Rlcityincident=(RelativeLayout)view.findViewById(R.id.Rlcityincident);
        Rlmedicalfiremanincident=(RelativeLayout)view.findViewById(R.id.Rlmedicalfiremanincident);

        Rlpoliceincident.setOnClickListener(this);
        Rlcityincident.setOnClickListener(this);
        Rlmedicalfiremanincident.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Rlpoliceincident:
                ((MainActivity)getActivity()).displayScreen(14,null);
                break;

            case R.id.Rlmedicalfiremanincident:
                ((MainActivity)getActivity()).displayScreen(10,null);
                break;

            case R.id.Rlcityincident:
                ((MainActivity)getActivity()).displayScreen(15,null);
                break;

        }

    }
}
