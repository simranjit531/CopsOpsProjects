package com.copsopsapp.fragment.citizen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Public_Subscription extends Fragment implements View.OnClickListener {

    private RelativeLayout RLnext;
    private ImageView IVgreenwoman,IVgreenmam;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_public_subsription, container, false);

        ((MainActivity) getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVlogout.setVisibility(View.GONE);

        RLnext = (RelativeLayout) view.findViewById(R.id.RLnext);
               RLnext.setOnClickListener(this);

        IVgreenmam = (ImageView) view.findViewById(R.id.IVgreenmam);
        IVgreenwoman = (ImageView) view.findViewById(R.id.IVgreenwoman);
        IVgreenmam.setOnClickListener(this);
        IVgreenwoman.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.RLnext:
                ((MainActivity) getActivity()).displayScreen(1, null);
                break;


            case R.id.IVgreenmam:
               IVgreenmam.setImageResource(R.mipmap.img_green_dot);
               IVgreenwoman.setImageResource(R.mipmap.img_white_dot);
                break;

            case R.id.IVgreenwoman:
                IVgreenwoman.setImageResource(R.mipmap.img_green_dot);
                IVgreenmam.setImageResource(R.mipmap.img_white_dot);
                break;


        }

    }
}
