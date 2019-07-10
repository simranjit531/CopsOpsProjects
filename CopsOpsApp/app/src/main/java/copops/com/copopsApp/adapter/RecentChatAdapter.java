package copops.com.copopsApp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import copops.com.copopsApp.R;
import copops.com.copopsApp.fragment.ChatRecentFragment;
import copops.com.copopsApp.interfaceview.IncedentInterface;
import copops.com.copopsApp.pojo.IncidentSubPojo;
import copops.com.copopsApp.pojo.RecentChatHolder;
import copops.com.copopsApp.utils.Utils;
/**
 * Created by Ranjan Gupta
 */
public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.ViewHolder> {
    private IncidentSubPojo mIncidentSubPojo;
    private Context context;
 ChatItmeInterface mChatItmeInterface;
    ArrayList<RecentChatHolder> recentChatHolders;

 public interface ChatItmeInterface{
     public void clickPosition(int pos);
 }

    public RecentChatAdapter(Context context, ChatItmeInterface mChatItmeInterfac, ArrayList<RecentChatHolder> recentChatHolders) {

        this.context = context;
        this.mChatItmeInterface = mChatItmeInterfac;
        this.recentChatHolders = recentChatHolders;

    }

    @Override
    public RecentChatAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.our_recent_item, viewGroup, false);
        return new RecentChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentChatAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.nameIdTv.setText(recentChatHolders.get(i).getUser());

        if(recentChatHolders.get(i).getUnread().equalsIgnoreCase("0")){
            viewHolder.messageCount.setVisibility(View.GONE);
        }else{
            viewHolder.messageCount.setVisibility(View.VISIBLE);
            viewHolder.messageCount.setText(recentChatHolders.get(i).getUnread());
        }



        if(recentChatHolders.get(i).getMessage_type().equalsIgnoreCase("TEXT")){
            viewHolder.messageId.setText(recentChatHolders.get(i).getMessage());
        }else if(recentChatHolders.get(i).getMessage_type().equalsIgnoreCase("VIDEO")){
            viewHolder.messageId.setText("Video Attachment");
        }else if(recentChatHolders.get(i).getMessage_type().equalsIgnoreCase("IMAGE")){
            viewHolder.messageId.setText("Image Attachment");
        }else{
            viewHolder.messageId.setText(recentChatHolders.get(i).getMessage());
            viewHolder.messageId.setText("Pdf Attachment");
        }

        viewHolder.clickId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatItmeInterface.clickPosition(i);
            }
        });

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
        return recentChatHolders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameIdTv,messageId,messageCount;
       // private ImageView icident_cat_img;
        LinearLayout clickId;
        LinearLayout llviolence;
        public ViewHolder(View view) {
            super(view);
            clickId = (LinearLayout) view.findViewById(R.id.clickId);

            nameIdTv = (TextView) view.findViewById(R.id.nameIdTv);
            messageId = (TextView) view.findViewById(R.id.messageId);
            messageCount = (TextView) view.findViewById(R.id.messageCount);
//
//            icident_cat_img = (ImageView) view.findViewById(R.id.icident_cat_img);
//            llviolence = (LinearLayout) view.findViewById(R.id.llviolence);
        }
    }

}