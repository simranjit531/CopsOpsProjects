package copops.com.copopsApp.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.llcitizenhome)
    LinearLayout llcitizenhome;

    @BindView(R.id.llcopshome)
    LinearLayout llcopshome;

    Fragment fragment;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        onClick();
        return view;
    }

    private void onClick() {
        llcitizenhome.setOnClickListener(this);
        llcopshome.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llcitizenhome:
                Utils.fragmentCall(new LoginFragment("citizen"), getFragmentManager());
                break;


            case R.id.llcopshome:
                Utils.fragmentCall(new LoginFragment("Cops"), getFragmentManager());
                break;
        }
    }
}
