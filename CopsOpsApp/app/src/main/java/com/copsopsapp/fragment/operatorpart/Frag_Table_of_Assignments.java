package com.copsopsapp.fragment.operatorpart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Table_of_Assignments extends Fragment implements View.OnClickListener {

    private ListView lvtableofassignments;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_table_of_assignments, container, false);

        ((MainActivity) getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVlogout.setVisibility(View.GONE);

        lvtableofassignments = (ListView) view.findViewById(R.id.lvtableofassignments);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        
        }


    }
}
