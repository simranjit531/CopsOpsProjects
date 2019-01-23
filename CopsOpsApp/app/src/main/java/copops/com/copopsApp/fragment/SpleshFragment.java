package copops.com.copopsApp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import copops.com.copopsApp.R;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.TrackingServices;
import copops.com.copopsApp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpleshFragment extends Fragment {

    private static int SPLASH_TIME_OUT = 1000;

    AppSession mAppSession;
    public SpleshFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_splesh, container, false);
        mAppSession = mAppSession.getInstance(getActivity());

        if(mAppSession.getData("Login")!=null){

        }else{
            mAppSession.saveData("Login","0");
        }

       refreshUI();
        return v;
    }
    private void refreshUI() {
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {


                                          if (mAppSession.getData("Login").equalsIgnoreCase("1")) {


                                              if(mAppSession.getData("userType").equalsIgnoreCase("Citizen")) {
                                                  Utils.fragmentCall(new CitizenFragment(), getFragmentManager());

                                              }else{
                                                  Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
                                              }

                                              getActivity().startService(new Intent(getActivity(),TrackingServices.class));
                                          } else {
                                              Utils.fragmentCall(new HomeFragment(), getFragmentManager());
                                          }
                                      }


                                  },
                SPLASH_TIME_OUT);
    }

}
