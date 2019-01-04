package copops.com.copopsApp.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import copops.com.copopsApp.R;
import copops.com.copopsApp.interfaceview.IncedentInterface;
import copops.com.copopsApp.pojo.IncidentTypePojo;


public class IncidentTypeAdapter extends RecyclerView.Adapter<IncidentTypeAdapter.ViewHolder> {
    private ArrayList<IncidentTypePojo.Data> incidentType;
    private Context context;
    IncedentInterface mIncedentInterface;
    String userId;
    public IncidentTypeAdapter(Context context, ArrayList<IncidentTypePojo.Data> incidentType,IncedentInterface mIncedentInterface) {
        this.incidentType = incidentType;
        this.context = context;
        this.mIncedentInterface = mIncedentInterface;
    }

    @Override
    public IncidentTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.frag_report_an_incidents_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IncidentTypeAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.icident_type_name.setText(incidentType.get(i).getIncident_name());
        viewHolder.icident_type_desc.setText(incidentType.get(i).getIncident_description());
        if(incidentType.get(i).getIncident_img_url()!=null){
//            Glide.with(context)
//                    .load(incidentType.get(i).getIncident_img_url())
//                    .apply(new RequestOptions().override(150, 150))
//                    .into(viewHolder.icident_type_img);
            Glide.with(context).load(incidentType.get(i).getIncident_img_url()).into(viewHolder.icident_type_img);
        }else{
            Glide.with(context).load(R.mipmap.img_police).into(viewHolder.icident_type_img);
        }

        viewHolder.Rlpoliceincident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncedentInterface.clickPosition(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return incidentType.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView icident_type_name,icident_type_desc;
        private ImageView icident_type_img;
        RelativeLayout Rlpoliceincident;
        public ViewHolder(View view) {
            super(view);

            icident_type_name = (TextView)view.findViewById(R.id.mType);
            Rlpoliceincident = (RelativeLayout)view.findViewById(R.id.Rlpoliceincident);

            icident_type_desc = (TextView)view.findViewById(R.id.mTypeDescription);

            icident_type_img = (ImageView) view.findViewById(R.id.IVpolicelogo);
        }
    }

}