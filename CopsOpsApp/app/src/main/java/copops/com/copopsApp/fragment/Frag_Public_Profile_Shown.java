package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickblox.sample.core.utils.ResourceUtils;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.chatmodule.App;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.utils.AppSession;

@SuppressLint("ValidFragment")
public class Frag_Public_Profile_Shown extends Fragment implements View.OnClickListener {

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;


    @BindView(R.id.TVname)
    TextView TVname;

    @BindView(R.id.TVprogressbarnumber)
    TextView TVprogressbarnumber;

    @BindView(R.id.TVprogressbarreports)
    TextView TVprogressbarreports;

    @BindView(R.id.TVprofiledescription)
    TextView TVprofiledescription;
    @BindView(R.id.grade)
    TextView grade;

    @BindView(R.id.TVprogresspercentage)
    TextView TVprogresspercentage;
    @BindView(R.id.IVqrcode)
    ImageView IVqrcode;

    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    OperatorShowAlInfo operatorShowAlInfo;

    AppSession mAppSession;

    public Frag_Public_Profile_Shown(OperatorShowAlInfo operatorShowAlInfo) {

        this.operatorShowAlInfo = operatorShowAlInfo;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_public_profile, container, false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {

        mAppSession = mAppSession.getInstance(getActivity());
        Rltoolbar.setOnClickListener(this);
        TVname.setText(mAppSession.getData("name"));


        TVprogressbarnumber.setText(operatorShowAlInfo.getReport());

        if(mAppSession.getData("userType").equalsIgnoreCase("citizen")){
            grade.setVisibility(View.GONE);
        }else{
            grade.setVisibility(View.VISIBLE);
            grade.setText(operatorShowAlInfo.getGrade());
        }


        TVprofiledescription.setText(operatorShowAlInfo.getLevel());
        TVprogressbarreports.setText(operatorShowAlInfo.getCompleted_reports() + " Signalements");

        if (operatorShowAlInfo.getProfile_percent().equalsIgnoreCase("0")) {

        } else {
            TVprogresspercentage.setText(operatorShowAlInfo.getProfile_percent() + "%");
        }

        progressBar1.setMax(100);
        progressBar1.setProgress(Integer.valueOf(operatorShowAlInfo.getProfile_percent()));


        if (mAppSession.getData("profile_qrcode") != null) {




            Glide.with(getActivity())
                    .load(mAppSession.getData("profile_qrcode"))
                    .override(250, 250)
                    .into(IVqrcode);
//            RequestOptions myOptions = new RequestOptions()
//                    .placeholder(R.drawable.ic_error_white)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .dontAnimate()
//                    .skipMemoryCache(true)
//                    ;
//            Glide
//                    .with(getActivity())
//                    .load(mAppSession.getData("profile_qrcode"))
//                    .apply(myOptions.override(250, 250))
//                    .into(IVqrcode);


        } else {

            IVqrcode.setImageResource(R.mipmap.img_qrcode);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Rltoolbar:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                break;
        }


    }
}
