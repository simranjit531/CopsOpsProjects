package copops.com.copopsApp.chatmodule.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quickblox.chat.QBChatService;

import com.quickblox.sample.core.ui.adapter.BaseListAdapter;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.quickblox.sample.core.utils.UiUtils;
import com.quickblox.users.model.QBUser;

import java.util.List;

import copops.com.copopsApp.R;

public class UsersAdapter extends BaseListAdapter<QBUser> {

    protected QBUser currentUser;
    clickPos mClickPos;
    public interface clickPos{

        public void clickPostion();
    }

    public UsersAdapter(Context context, List<QBUser> users,clickPos mClickPos ) {
        super(context, users);
        currentUser = QBChatService.getInstance().getUser();
        this.mClickPos=mClickPos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QBUser user = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_user, parent, false);
            holder = new ViewHolder();
            holder.userImageView = (ImageView) convertView.findViewById(R.id.image_user);
            holder.loginTextView = (TextView) convertView.findViewById(R.id.text_user_login);
            holder.userCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_user);
            holder.clickId = (LinearLayout) convertView.findViewById(R.id.clickId);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (isUserMe(user)) {
       //     holder.loginTextView.setText(context.getString(R.string.placeholder_username_you, user.getFullName()));
        } else {
            holder.loginTextView.setText(user.getFullName());
        }

        if (isAvailableForSelection(user)) {
            holder.loginTextView.setTextColor(ResourceUtils.getColor(R.color.text_color_black));
        } else {
          //  holder.loginTextView.setTextColor(ResourceUtils.getColor(R.color.text_color_medium_grey));
        }

        holder.userImageView.setBackgroundDrawable(UiUtils.getColorCircleDrawable(position));
        holder.userCheckBox.setVisibility(View.GONE);

        return convertView;
    }

    protected boolean isUserMe(QBUser user) {
        return currentUser != null && currentUser.getId().equals(user.getId());
    }

    protected boolean isAvailableForSelection(QBUser user) {
        return currentUser == null || !currentUser.getId().equals(user.getId());
    }

    protected static class ViewHolder {
        ImageView userImageView;
        TextView loginTextView;
        CheckBox userCheckBox;
        LinearLayout clickId;
    }
}
