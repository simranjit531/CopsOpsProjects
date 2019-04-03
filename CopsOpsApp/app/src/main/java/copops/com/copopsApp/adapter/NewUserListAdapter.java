package copops.com.copopsApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import copops.com.copopsApp.R;
import copops.com.copopsApp.interfaceview.IncedentInterface;
import copops.com.copopsApp.pojo.IncidentSubPojo;

public class NewUserListAdapter  extends RecyclerView.Adapter<NewUserListAdapter.ViewHolder> {
    private IncidentSubPojo mIncidentSubPojo;
    private Context context;
    IncedentInterface mIncedentInterface;

    public NewUserListAdapter(Context context) {

        this.context = context;

    }

    @Override
    public NewUserListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_user_list_item, viewGroup, false);
        return new NewUserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewUserListAdapter.ViewHolder viewHolder, final int i) {

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
//        viewHolder.llviolence.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIncedentInterface.clickPosition(i);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 25;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView icident_cat_name;
        private ImageView icident_cat_img;
        LinearLayout llviolence;
        public ViewHolder(View view) {
            super(view);

//            icident_cat_name = (TextView)view.findViewById(R.id.icident_cat_name);
//
//            icident_cat_img = (ImageView) view.findViewById(R.id.icident_cat_img);
//            llviolence = (LinearLayout) view.findViewById(R.id.llviolence);
        }
    }

}