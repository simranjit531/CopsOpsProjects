package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import copops.com.copopsApp.R;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.Utils;


/**
 * Created by Lenovo on 21-11-2018.
 */

@SuppressLint("ValidFragment")
public class Frag_Call_Number extends Fragment implements View.OnClickListener {

    private TextView TVnothanks,TVcall,TVcalltext;
    String helpline_number;
    String parentname;

    AppSession mAppSession;

    public Frag_Call_Number(String helpline_number,String parentname){
        this.helpline_number=helpline_number;
        this.parentname=parentname;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_call_number,container,false);

        mAppSession=mAppSession.getInstance(getActivity());

        TVcall=(TextView)view.findViewById(R.id.TVcall);
        TVnothanks=(TextView)view.findViewById(R.id.TVnothanks);
        TVcalltext=(TextView)view.findViewById(R.id.TVcalltext);
        TVnothanks.setOnClickListener(this);
        TVcall.setOnClickListener(this);

        if (mAppSession.getData("city").equals("police")){
            TVcalltext.setText("Souhaitez-vous être mis en relation avec le comissariat de Police ?");
            helpline_number="17";
        }else if (mAppSession.getData("city").equals("medical")){
            TVcalltext.setText("Souhaitez-vous être mis en relation avec les Sapeurs Pompiers ?");
            helpline_number="18";
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.TVnothanks:
             //   ((MainActivity)getActivity()).popBackStack();

                if(mAppSession.getData("userType").equalsIgnoreCase("Citizen")) {
                    Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
                }else{
                    Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
                }
                break;


            case R.id.TVcall:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+helpline_number));
                startActivity(callIntent);
                break;

        }

    }
}
