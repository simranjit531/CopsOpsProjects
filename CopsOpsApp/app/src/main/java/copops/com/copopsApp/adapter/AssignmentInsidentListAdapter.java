package copops.com.copopsApp.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import copops.com.copopsApp.R;
import copops.com.copopsApp.interfaceview.IncedentInterface;
import copops.com.copopsApp.pojo.AssignmentListPojo;

public class AssignmentInsidentListAdapter extends RecyclerView.Adapter<AssignmentInsidentListAdapter.ViewHolder> {
    //   private ArrayList<IncidentTypePojo.Data> incidentType;

    private Context context;

    AssignmentListPojo assignmentListPojo;

    IncedentInterface mIncedentInterface;
    String userId;

    public AssignmentInsidentListAdapter(Context context, AssignmentListPojo assignmentListPojo, IncedentInterface mIncedentInterface) {
        this.context = context;
        this.assignmentListPojo = assignmentListPojo;
        this.mIncedentInterface = mIncedentInterface;

    }
    @Override
    public AssignmentInsidentListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.assingment_item, viewGroup, false);
        return new AssignmentInsidentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssignmentInsidentListAdapter.ViewHolder viewHolder,final int i) {

        viewHolder.dateTv.setText(assignmentListPojo.getData().get(i).getCreated_at());
        viewHolder.objectId.setText(assignmentListPojo.getData().get(i).getSub_category_name());

        if (assignmentListPojo.getData().get(i).getIsAssigned().equalsIgnoreCase("wait")) {
            viewHolder.stateId.setText(R.string.onwait);
            viewHolder.stateId.setTextColor(context.getResources().getColor(R.color.orange));
        } else if (assignmentListPojo.getData().get(i).getIsAssigned().equalsIgnoreCase("pending")) {
            viewHolder.stateId.setText(R.string.pending);
            viewHolder.stateId.setTextColor(context.getResources().getColor(R.color.btntextcolort));
        } else if(assignmentListPojo.getData().get(i).getIsAssigned().equalsIgnoreCase("finished")) {
            viewHolder.stateId.setTextColor(context.getResources().getColor(R.color.green));
            viewHolder.stateId.setText(R.string.finished);
        } else {
            viewHolder.stateId.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.stateId.setText(R.string.Assigned);
        }


      /*  viewHolder.stateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncedentInterface.clickPosition(i);
            }
        });*/

        viewHolder.Ivarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncedentInterface.clickPosition(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignmentListPojo.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTv, objectId, stateId;
        private ImageView Ivarrow;


        public ViewHolder(View view) {
            super(view);
            dateTv = (TextView) view.findViewById(R.id.dateTv);
            objectId = (TextView) view.findViewById(R.id.objectId);
            stateId = (TextView) view.findViewById(R.id.stateId);
            Ivarrow = (ImageView) view.findViewById(R.id.Ivarrow);
        }
    }


}