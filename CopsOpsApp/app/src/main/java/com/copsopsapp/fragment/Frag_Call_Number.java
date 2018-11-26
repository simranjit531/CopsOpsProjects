package com.copsopsapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Call_Number extends Fragment implements View.OnClickListener {

    private ImageView IVnothanks,IVcall;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_call_number,container,false);

        ((MainActivity)getActivity()).Rltoolbar.setVisibility(View.GONE);
        ((MainActivity)getActivity()).IVback.setVisibility(View.GONE);
        ((MainActivity)getActivity()).toolbar.setVisibility(View.GONE);
        ((MainActivity)getActivity()).IVlogout.setVisibility(View.GONE);

        IVnothanks=(ImageView)view.findViewById(R.id.IVnothanks);
        IVcall=(ImageView)view.findViewById(R.id.IVcall);
        IVcall.setOnClickListener(this);
        IVnothanks.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.IVnothanks:
                ((MainActivity)getActivity()).popBackStack();
                break;

            case R.id.IVcall:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                startActivity(callIntent);
                break;

        }

    }
}
