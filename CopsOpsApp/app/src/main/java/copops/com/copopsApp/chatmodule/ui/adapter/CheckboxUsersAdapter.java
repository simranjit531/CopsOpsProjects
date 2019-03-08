package copops.com.copopsApp.chatmodule.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckboxUsersAdapter extends UsersAdapter {

    private List<Integer> initiallySelectedUsers;
    private Set<QBUser> selectedUsers;
    clickPos mClickPos;

    public CheckboxUsersAdapter(Context context, List<QBUser> users, clickPos mClickPos) {
        super(context, users,mClickPos);
        selectedUsers = new HashSet<>();
        this.selectedUsers.add(currentUser);
        this.mClickPos=mClickPos;

        this.initiallySelectedUsers = new ArrayList<>();
    }

    public void addSelectedUsers(List<Integer> userIds) {
        for (QBUser user : objectsList) {
            for (Integer id : userIds) {
                if (user.getId().equals(id)) {
                    selectedUsers.add(user);
                    initiallySelectedUsers.add(user.getId());
                    break;
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        final QBUser user = getItem(position);
        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.clickId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUsers.add(user);
                mClickPos.clickPostion();
            }
        });

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isAvailableForSelection(user)) {
//                    return;
//                }
//
//                holder.userCheckBox.setChecked(!holder.userCheckBox.isChecked());
//                if (holder.userCheckBox.isChecked()) {
//                    selectedUsers.add(user);
//
//                } else {
//                    selectedUsers.remove(user);
//                }
//            }
//        });

        holder.userCheckBox.setVisibility(View.GONE);
        holder.userCheckBox.setChecked(selectedUsers.contains(user));

        return view;
    }

    public Set<QBUser> getSelectedUsers() {
        return selectedUsers;
    }

    @Override
    protected boolean isAvailableForSelection(QBUser user) {
        return super.isAvailableForSelection(user) && !initiallySelectedUsers.contains(user.getId());
    }
}
