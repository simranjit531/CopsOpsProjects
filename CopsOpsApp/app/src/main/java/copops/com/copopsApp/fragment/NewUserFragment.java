package copops.com.copopsApp.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.NewUserListAdapter;
import copops.com.copopsApp.adapter.RecentChatAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUserFragment extends Fragment {

@BindView(R.id.newUser_list_recycler_view)
    RecyclerView newUser_list_recycler_view;

NewUserListAdapter mNewUserListAdapter;
    public NewUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_new_user, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }


    private void initView() {

        mNewUserListAdapter = new NewUserListAdapter(getContext());
        newUser_list_recycler_view.setHasFixedSize(true);
        newUser_list_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        newUser_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        newUser_list_recycler_view.setAdapter(mNewUserListAdapter);

    }

}
