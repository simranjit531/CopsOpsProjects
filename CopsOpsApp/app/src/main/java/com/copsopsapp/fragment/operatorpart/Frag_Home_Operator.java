package com.copsopsapp.fragment.operatorpart;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Home_Operator extends Fragment implements View.OnClickListener {

    private RelativeLayout RLpostiionofintervention, RLreportsnofincidents, RLnavigation;
    private LinearLayout llintervention;
    private RelativeLayout rlchat;
    private TextView TVname;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_operator_new, container, false);

        ((MainActivity) getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVback.setVisibility(View.GONE);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVlogout.setVisibility(View.VISIBLE);

        RLpostiionofintervention = (RelativeLayout) view.findViewById(R.id.RLpostiionofintervention);
        RLreportsnofincidents = (RelativeLayout) view.findViewById(R.id.RLreportanincident);
        RLnavigation = (RelativeLayout) view.findViewById(R.id.RLnavigation);

        llintervention = (LinearLayout) view.findViewById(R.id.llintervention);
        rlchat = (RelativeLayout) view.findViewById(R.id.rlchat);

        TVname = (TextView) view.findViewById(R.id.TVname);

        RLpostiionofintervention.setOnClickListener(this);
        RLreportsnofincidents.setOnClickListener(this);
        RLnavigation.setOnClickListener(this);
        llintervention.setOnClickListener(this);
        rlchat.setOnClickListener(this);
        TVname.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.RLnavigation:
                opendialogcustomdialog();
                break;

            case R.id.TVname:
                //   ((MainActivity) getActivity()).displayScreen(18, null);
                break;

            case R.id.RLreportanincident:
                ((MainActivity) getActivity()).displayScreen(13, null);
                break;

            case R.id.RLpostiionofintervention:
                ((MainActivity) getActivity()).displayScreen(21, null);
                break;


        }


    }

    public void opendialogcustomdialog() {

        final Dialog dialog1 = new Dialog(getActivity(), R.style.DialogFragmentTheme);
        dialog1.setContentView(R.layout.custom_dialog_navigation_home_citoyen);
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);

        TextView TVrefuse = (TextView) dialog1.findViewById(R.id.TVrefuse);
        TextView TVallow = (TextView) dialog1.findViewById(R.id.TVallow);

        TVallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                ((MainActivity) getActivity()).displayScreen(19, null);

            }
        });

        TVrefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }
}
