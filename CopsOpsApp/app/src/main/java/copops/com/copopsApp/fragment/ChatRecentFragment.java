package copops.com.copopsApp.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.base.CaseFormat;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.AssignmentInsidentListAdapter;
import copops.com.copopsApp.adapter.RecentChatAdapter;
import copops.com.copopsApp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatRecentFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.recent_recycler_view)
    RecyclerView recent_recycler_view;



    @BindView(R.id.iv_person_add)
    ImageView iv_person_add;


    RecentChatAdapter mRecentChatAdapter;
    public ChatRecentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_chat_recent, container, false);
        ButterKnife.bind(this,view);

        initView();
        return view;
    }

    private void initView() {

        iv_person_add.setOnClickListener(this);

        mRecentChatAdapter = new RecentChatAdapter(getContext());
        recent_recycler_view.setHasFixedSize(true);
        recent_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recent_recycler_view.setItemAnimator(new DefaultItemAnimator());
        recent_recycler_view.setAdapter(mRecentChatAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case R.id.iv_person_add:
            Utils.fragmentCall(new NewUserFragment(), getFragmentManager());
        }
    }
}
