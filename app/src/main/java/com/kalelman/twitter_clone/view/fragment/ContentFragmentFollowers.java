package com.kalelman.twitter_clone.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kalelman.twitter_clone.R;
import com.kalelman.twitter_clone.model.ViewModel;
import com.kalelman.twitter_clone.view.adapter.ListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//import com.kalelman.twitter_clone.view.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentFragmentFollowers extends Fragment {

    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    private View view;
    private List<ViewModel> resultList = new ArrayList<>();
    private ListAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content_followers, container, false);
        ButterKnife.bind(this, view);

        /*users.add("Erick");
        users.add("Tono");
        users.add("Jose Luis");
        users.add("Ana Bertha");
        users.add("Brenda");
        users.add("Gabriel");
        users.add("Chucho");*/

        listAdapter = new ListAdapter(resultList);
        recyclerView.setAdapter(listAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateData();

        return view;
    }

    private void updateData() {
        ViewModel viewModel = new ViewModel();
        resultList.add(viewModel);
        listAdapter.notifyItemInserted(resultList.size()-1);
    }

    @Override
    public void onStop() {
        super.onStop();
        resultList.clear();
        listAdapter.notifyDataSetChanged();
    }
}
