package copops.com.copopsApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quickblox.users.model.QBUser;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.AssignmentListPojo;

public class ChatAAdapter extends RecyclerView.Adapter<ChatAAdapter.ViewHolder> {
    //   private ArrayList<IncidentTypePojo.Data> incidentType;

    private Context context;

    AssignmentListPojo assignmentListPojo;

    ChatInterFcee mIncedentInterface;

    List<QBUser> users;
    public ChatAAdapter(Context context, List<QBUser> users, ChatInterFcee mIncedentInterface) {
        this.context = context;
        this.users = users;
        this.mIncedentInterface = mIncedentInterface;


    }

    public interface  ChatInterFcee{
        public void onClick(int Pos);
    }


    @Override
    public ChatAAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatitem, viewGroup, false);
        return new ChatAAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatAAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.nameTv.setText(users.get(i).getFullName());
       viewHolder.chatId.setText(String.valueOf(users.get(i).getLastRequestAt()));
       // viewHolder.objectId.setText(assignmentListPojo.getData().get(i).getSub_category_name());
//
//        if (assignmentListPojo.getData().get(i).getStatus().equalsIgnoreCase("1")) {
//            viewHolder.stateId.setText("On-wait");
//            viewHolder.stateId.setTextColor(context.getResources().getColor(R.color.black));
//        } else if (assignmentListPojo.getData().get(i).getStatus().equalsIgnoreCase("2")) {
//            viewHolder.stateId.setText("Pending");
//            viewHolder.stateId.setTextColor(context.getResources().getColor(R.color.btntextcolort));
//        } else {
//            viewHolder.stateId.setTextColor(context.getResources().getColor(R.color.black));
//            viewHolder.stateId.setText("Finished");
//        }
//
//
        viewHolder.chatclickLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncedentInterface.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView chatId, nameTv;
        LinearLayout chatclickLL;


        public ViewHolder(View view) {
            super(view);
            nameTv = (TextView) view.findViewById(R.id.nameTv);
            chatId = (TextView) view.findViewById(R.id.chatId);
           chatclickLL = (LinearLayout) view.findViewById(R.id.chatclickLL);

        }
    }


}
