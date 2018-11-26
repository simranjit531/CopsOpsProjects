package com.copsopsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;
import com.copsopsapp.utils.AppSession;
import com.copsopsapp.utils.Internet;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Login extends Fragment implements View.OnClickListener {

    private TextView Tvforgot, Tvregister;
    private RelativeLayout RLlogin;
    private EditText ETemail, ETpassword;
    private AppSession msession;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login, container, false);


        msession = AppSession.getInstance(getActivity());

        ((MainActivity) getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVlogout.setVisibility(View.GONE);

        Tvforgot = (TextView) view.findViewById(R.id.Tvforgot);
        Tvregister = (TextView) view.findViewById(R.id.Tvregister);

        RLlogin = (RelativeLayout) view.findViewById(R.id.RLlogin);

        ETemail = (EditText) view.findViewById(R.id.ETemail);
        ETpassword = (EditText) view.findViewById(R.id.ETpassword);

        Tvforgot.setOnClickListener(this);
        Tvregister.setOnClickListener(this);
        RLlogin.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Tvforgot:
                ((MainActivity) getActivity()).displayScreen(7, null);
                break;

            case R.id.Tvregister:
                if (msession.getusertype().equals("cops")) {
                    ((MainActivity) getActivity()).popBackStack();
                    ((MainActivity) getActivity()).displayScreen(5, null);
                } else if (msession.getusertype().equals("citizen")) {
                    ((MainActivity) getActivity()).popBackStack();
                    ((MainActivity) getActivity()).displayScreen(6, null);

                }
                break;

            case R.id.RLlogin:
                if (Internet.getInstance().internetConnectivity(getActivity())) {

                    if (ETemail.getText().toString().length() == 0 && ETpassword.getText().toString().length() == 0) {
                        Toast.makeText(getActivity(), "Please fill the whole details", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(ETemail.getText().toString()).matches()) {
                        Toast.makeText(getActivity(), "Please fill the correct email", Toast.LENGTH_SHORT).show();
                    } else if (ETemail.getText().toString().equals("testuser@gmail.com") && ETpassword.getText().toString().equals("123456")) {
                        msession.setlogin("true");
                    } else if (ETemail.getText().toString().equals("testcops@gmail.com") && ETpassword.getText().toString().equals("123456")) {
                        msession.setlogin("true");
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
