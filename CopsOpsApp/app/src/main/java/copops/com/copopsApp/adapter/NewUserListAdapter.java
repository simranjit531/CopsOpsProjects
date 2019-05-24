package copops.com.copopsApp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import copops.com.copopsApp.R;
import copops.com.copopsApp.interfaceview.IncedentInterface;
import copops.com.copopsApp.pojo.IncidentSubPojo;
import copops.com.copopsApp.pojo.UserListPojo;

public class NewUserListAdapter extends RecyclerView.Adapter<NewUserListAdapter.ViewHolder> {

    private Context context;
    UserListPojo userList;

    List<UserListPojo.Response> list;
    ArrayList<UserListPojo.Response> listTemp = new ArrayList<>();
    UserListInterFace mUserListInterFace;

    public interface UserListInterFace{
        public void onClick(int pos);
    }
    public NewUserListAdapter(Context context, List<UserListPojo.Response> list, UserListInterFace mUserListInterFace) {

        this.context = context;
        this.list = list;
        this.mUserListInterFace=mUserListInterFace;
        this.listTemp.addAll(list);
    }

    @Override
    public NewUserListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_user_list_item, viewGroup, false);
        return new NewUserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewUserListAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.nameId.setText(list.get(i).getFirst_name()+" "+list.get(i).getLast_name());

        // viewHolder.icident_cat_name.setText(mIncidentSubPojo.getData().get(i).getIncident_name());
//        if(mIncidentSubPojo.getData().get(i).getIncident_img_url()!=null){
//
//
////            Glide
////                    .with(context)
////                    .load(mIncidentSubPojo.getData().get(i).getIncident_img_url()).apply(new RequestOptions().override(100, 100))
////                    .into(viewHolder.icident_cat_img);
//            Glide.with(context)
//                    .load(mIncidentSubPojo.getData().get(i).getIncident_img_url())
//                    .override(100, 100)
//                    .into(viewHolder.icident_cat_img);
//            //   Glide.with(context).load(mIncidentSubPojo.getData().get(i).getIncident_img_url()).into(viewHolder.icident_cat_img);
//        }else{
//            Glide.with(context).load(R.mipmap.img_violence).into(viewHolder.icident_cat_img);
//        }
//
//
        viewHolder.nameId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserListInterFace.onClick(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameId;
        private ImageView icident_cat_img;
        LinearLayout llviolence;
        public ViewHolder(View view) {
            super(view);

            nameId = (TextView)view.findViewById(R.id.nameId);
//
//            icident_cat_img = (ImageView) view.findViewById(R.id.icident_cat_img);
//            llviolence = (LinearLayout) view.findViewById(R.id.llviolence);
        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        list.clear();
        if (charText.length() == 0) {
            list.addAll(listTemp);
        }
        else
        {
            for (UserListPojo.Response wp : listTemp)
            {
                if ((wp.getFirst_name().toLowerCase()+" "+wp.getLast_name().toLowerCase()).contains(charText))
                {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}