package com.copsopsapp.fragment.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Reporting_Police extends Fragment implements View.OnClickListener {

    private LinearLayout llviolence,lltheft,llburglary,llsexualviolence,llbodyincident,llothers;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_reporting_police,container,false);

        ((MainActivity)getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).IVlogout.setVisibility(View.GONE);

        llothers=(LinearLayout)view.findViewById(R.id.llothers);
        llburglary=(LinearLayout)view.findViewById(R.id.llburglary);
        llburglary.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.llburglary:
                ((MainActivity)getActivity()).displayScreen(9,null);
                break;


        }



    }
}
