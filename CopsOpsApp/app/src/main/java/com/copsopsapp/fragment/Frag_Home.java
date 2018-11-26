package com.copsopsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;
import com.copsopsapp.utils.AppSession;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Home extends Fragment implements View.OnClickListener {

    private LinearLayout llcopshome, llcitizenhome;
    private AppSession msession;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        msession = AppSession.getInstance(getActivity());


        ((MainActivity) getActivity()).Rltoolbar.setVisibility(View.GONE);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        ((MainActivity) getActivity()).IVback.setVisibility(View.GONE);
        ((MainActivity) getActivity()).IVlogout.setVisibility(View.GONE);

        llcitizenhome = (LinearLayout) view.findViewById(R.id.llcitizenhome);
        llcopshome = (LinearLayout) view.findViewById(R.id.llcopshome);

        llcopshome.setOnClickListener(this);
        llcitizenhome.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.llcitizenhome:
                if (msession.getusertype().equals("citizen")) {
                    if (msession.getlogin().equals("")) {
                        msession.setusertype("citizen");
                        ((MainActivity) getActivity()).displayScreen(4, null);
                    } else {
                        ((MainActivity) getActivity()).displayScreen(2, null);
                    }
                } else {
                    msession.setusertype("citizen");
                    ((MainActivity) getActivity()).displayScreen(4, null);
                }
                break;

            case R.id.llcopshome:
                if (msession.getusertype().equals("cops")) {
                    if (msession.getlogin().equals("")) {
                        msession.setusertype("cops");
                        ((MainActivity) getActivity()).displayScreen(4, null);
                    } else {
                        ((MainActivity) getActivity()).displayScreen(3, null);
                    }
                } else {
                    msession.setusertype("cops");
                    ((MainActivity) getActivity()).displayScreen(4, null);
                }

                break;
        }

    }
}
