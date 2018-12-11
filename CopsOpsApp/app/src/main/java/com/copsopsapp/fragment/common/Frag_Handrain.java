package com.copsopsapp.fragment.common;

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

public class Frag_Handrain extends Fragment  implements View.OnClickListener{

    private RelativeLayout Rlnext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_handrail,container,false);

        ((MainActivity)getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).IVlogout.setVisibility(View.GONE);

        Rlnext=(RelativeLayout)view.findViewById(R.id.Rlnext);
        Rlnext.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Rlnext:
                ((MainActivity)getActivity()).displayScreen(16,null);
                break;



        }


    }
}
