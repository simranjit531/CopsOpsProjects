package copops.com.copopsApp.chatmodule.ui.adapter;


import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Context;


import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;

import com.quickblox.sample.core.ui.adapter.BaseSelectableListAdapter;
import com.quickblox.sample.core.utils.ResourceUtils;


import java.util.List;

import copops.com.copopsApp.R;
import copops.com.copopsApp.chatmodule.utils.qb.QbDialogUtils;

public class DialogsAdapter extends BaseSelectableListAdapter<QBChatDialog> {
    private static NotificationChannel mChannel;
    private static NotificationManager notifManager;
    private static final String EMPTY_STRING = "";
    List<QBChatDialog> dialogs;
    static Context context;
    public DialogsAdapter(Context context, List<QBChatDialog> dialogs) {
        super(context, dialogs);
        this.context=context;
        this.dialogs=dialogs;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_dialog, parent, false);
            holder = new ViewHolder();
            holder.rootLayout = (ViewGroup) convertView.findViewById(R.id.root);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.text_dialog_name);
            holder.lastMessageTextView = (TextView) convertView.findViewById(R.id.text_dialog_last_message);
          //  holder.dialogImageView = (ImageView) convertView.findViewById(R.id.image_dialog_icon);
            holder.unreadCounterTextView = (TextView) convertView.findViewById(R.id.text_dialog_unread_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QBChatDialog dialog = getItem(position);
        if (dialog.getType().equals(QBDialogType.GROUP)) {
            //holder.dialogImageView.setBackgroundDrawable(UiUtils.getGreyCircleDrawable());
           // holder.dialogImageView.setImageResource(R.drawable.ic_chat_group);
        } else {
        //    holder.dialogImageView.setBackgroundDrawable(UiUtils.getColorCircleDrawable(position));
         //   holder.dialogImageView.setImageDrawable(null);
        }

        holder.nameTextView.setText(QbDialogUtils.getDialogName(dialog));
        holder.lastMessageTextView.setText(prepareTextLastMessage(dialog));

        int unreadMessagesCount = getUnreadMsgCount(dialog);

        String title = "Copops";
        String author = "AAA";

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(author)) {

            if(unreadMessagesCount>0){

                for(int i=0;i<dialogs.size();i++){
                    if(unreadMessagesCount>0) {

                    //  displayCustomNotificationForOrders(title,QbDialogUtils.getDialogName(dialog) + " " + unreadMessagesCount + "\n");

                    }
                }

            }

        }
        if (unreadMessagesCount == 0) {
            holder.unreadCounterTextView.setVisibility(View.GONE);
        } else {
            holder.unreadCounterTextView.setVisibility(View.VISIBLE);
            holder.unreadCounterTextView.setText(String.valueOf(unreadMessagesCount > 99 ? "99+" : unreadMessagesCount));
        }

        holder.rootLayout.setBackgroundColor(isItemSelected(position) ? ResourceUtils.getColor(R.color.selected_list_item_color) :
                ResourceUtils.getColor(android.R.color.transparent));

        return convertView;
    }

    public int getUnreadMsgCount(QBChatDialog chatDialog){
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            return 0;
        } else {
            return unreadMessageCount;
        }
    }

    private boolean isLastMessageAttachment(QBChatDialog dialog) {
        String lastMessage = dialog.getLastMessage();
        Integer lastMessageSenderId = dialog.getLastMessageUserId();
        return TextUtils.isEmpty(lastMessage) && lastMessageSenderId != null;
    }

    private String prepareTextLastMessage(QBChatDialog chatDialog){
        if (isLastMessageAttachment(chatDialog)){
            return context.getString(R.string.chat_attachment);
        } else {
            return chatDialog.getLastMessage();
        }
    }

    private static class ViewHolder {
        ViewGroup rootLayout;
        ImageView dialogImageView;
        TextView nameTextView;
        TextView lastMessageTextView;
        TextView unreadCounterTextView;
    }


}
