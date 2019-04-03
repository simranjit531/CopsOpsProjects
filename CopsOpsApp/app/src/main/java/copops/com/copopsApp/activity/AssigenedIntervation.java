package copops.com.copopsApp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;

import copops.com.copopsApp.pojo.AssignmentListPojo;

import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.shortcut.ShortcutViewService_Citizen;


public class AssigenedIntervation extends AppCompatActivity {
    private Context mContext;
    int pos;
    AssignmentListPojo assignmentListPojo;
    @BindView(R.id.Tvdate)
    TextView Tvdate;
    @BindView(R.id.Tvtime)
    TextView Tvtime;
    @BindView(R.id.Tvstate)
    TextView Tvstate;
    @BindView(R.id.desHeaderTv)
    TextView desHeaderTv;
    @BindView(R.id.TVreferencenumber)
    TextView TVreferencenumber;
    @BindView(R.id.tvid)
    TextView tvid;
    @BindView(R.id.otherHeaderTv)
    TextView otherHeaderTv;

    @BindView(R.id.etAddressId)
    EditText etAddressId;
    @BindView(R.id.objectId)
    EditText objectId;
    @BindView(R.id.otherdescId)
    EditText otherdescId;
    @BindView(R.id.descId)
    EditText descId;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;
    @BindView(R.id.Rlintervenue)
    RelativeLayout Rlintervenue;

    private String intervationId="";
    private String intervationDateTime="";
    private String intervationAddress="";
    private String intervationObject="";
    private String intervationDescp="";
    private String intervationOthDescp="";
    private String intervationStatus="";
    private String intervationRef="";
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_intervation);
        mContext=AssigenedIntervation.this;
        ButterKnife.bind(this);

        mainThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopService(new Intent(mContext, ShortcutViewService.class));

                stopService(new Intent(mContext, ShortcutViewService_Citizen.class));
            }
        }, 1000);
        Intent intent=getIntent();
        String value=intent.getStringExtra("remMsg");
        setReomteMsg(value);
    }
    protected void setReomteMsg(String value)
    {



try
{
       JSONObject jsonObject=new JSONObject(value);
       // JSONArray jsonArray=jsonObject.getJSONArray("incidentDetails");
       intervationId      =jsonObject.getString("id");
       intervationDateTime=jsonObject.getString("created_at");
       intervationAddress =jsonObject.getString("address");
       intervationObject  =jsonObject.getString("sub_category_name");
       intervationDescp   =jsonObject.getString("incident_description");
       intervationOthDescp =jsonObject.getString("other_description");
       intervationStatus=jsonObject.getString("status");
       intervationRef=jsonObject.getString("reference");



    otherdescId.setText(""+intervationOthDescp);
    TVreferencenumber.setText(""+intervationRef);
    etAddressId.setText(""+intervationAddress);
    objectId.setText(""+intervationObject);
    descId.setText(""+intervationDescp);

    String[] parts = intervationDateTime.split(" ");
    String date = parts[0]; // 004
    String time = parts[1]; // 034556
    Tvdate.setText(""+date);
    Tvtime.setText(""+time);

    if (intervationStatus.equals("1")) {
        Tvstate.setText(R.string.onwait);
        Tvstate.setTextColor(getResources().getColor(R.color.orange));
    }
    else if (intervationStatus.equals("2")) {
        Tvstate.setText(R.string.pending);
        Tvstate.setTextColor(getResources().getColor(R.color.btntextcolort));
    }else if (intervationStatus.equals("3")) {
        Tvstate.setText(R.string.Assigned);
        Tvstate.setTextColor(getResources().getColor(R.color.black));
    }
    else {
        Tvstate.setText(R.string.finished);
        //  Tvstate.setText("Finished");
        Tvstate.setTextColor(getResources().getColor(R.color.green));
        //  Rlintervenue.setVisibility(View.INVISIBLE);
        //  Tvstate.setText("Pending");
        //  Tvstate.setTextColor(getResources().getColor(R.color.black));
    }
}catch (JSONException e){
    e.printStackTrace();
}
    }
    public void clickBack(View v)
    {

        //startActivity(new Intent(mContext,DashboardActivity.class));
        finish();
    }

}
