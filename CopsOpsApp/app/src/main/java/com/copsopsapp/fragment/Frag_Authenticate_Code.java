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
import com.copsopsapp.utils.AppSession;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Authenticate_Code extends Fragment implements View.OnClickListener {

    private RelativeLayout RLSend;
    private AppSession msession;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_authenticate_device,container,false);

        msession=AppSession.getInstance(getActivity());

        ((MainActivity)getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).IVlogout.setVisibility(View.GONE);

        RLSend=(RelativeLayout)view.findViewById(R.id.RLSend);
        RLSend.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.RLSend:
                if (msession.getusertype().equals("cops")) {
                    ((MainActivity) getActivity()).popBackStack();
                    ((MainActivity) getActivity()).displayScreen(3, null);
                } else if (msession.getusertype().equals("citizen")) {
                    ((MainActivity) getActivity()).popBackStack();
                    ((MainActivity) getActivity()).displayScreen(2, null);

                }
                break;

        }

    }
}
