package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;



import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.IncedentAcceptResponse;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ReportFinishFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.TVreferencenumber)
    TextView TVreferencenumber;

    @BindView(R.id.RLfinish)
    RelativeLayout RLfinish;

    @BindView(R.id.barId)
    ImageView barId;

AppSession mAppSession;
    IncedentAcceptResponse incedentAcceptResponse;
    public ReportFinishFragment(IncedentAcceptResponse incedentAcceptResponse) {
        // Required empty public constructor

        this.incedentAcceptResponse=incedentAcceptResponse;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_report_finish, container, false);

      //  mAppSession=mAppSession.getInstance(getActivity());
        ButterKnife.bind(this,view);
        initView();
        return view;
    }


    private void initView() {

        mAppSession=mAppSession.getInstance(getActivity());
        Log.e("incedentAcceptResponse",""+incedentAcceptResponse);
        if(incedentAcceptResponse.getReference()!=null) {
            TVreferencenumber.setText(incedentAcceptResponse.getReference());
        } if(incedentAcceptResponse.getQrcode_url()!=null){
            //  Glide.with(this).load(incedentAcceptResponse.getQrcode_url()).into(barId);


//            RequestOptions myOptions = new RequestOptions()
//                    .placeholder(R.drawable.ic_error_white)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .dontAnimate()
//                    .skipMemoryCache(true)
//                    ;
//            Glide
//                    .with(getActivity())
//                    .load(incedentAcceptResponse.getQrcode_url())
//                    .apply(myOptions.override(250, 250))
//                    .into(barId);
            Glide.with(getActivity())
                    .load(incedentAcceptResponse.getQrcode_url()).override(250, 250)
                    .into(barId);
        }

        RLfinish.setOnClickListener(this);
//        Rltoolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.RLfinish:

                Utils.fragmentCall(new OperatorFragment(), getFragmentManager());


                break;

        }
    }
}
