package com.taskapp.ui.home;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taskapp.R;
import com.taskapp.Task;
import com.taskapp.TaskAdapter;
import com.taskapp.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        initList();
        return root;
    }

    private void initList() {
        list = new ArrayList<>();
        list.add(new Task("Joodar", ""));
        list.add(new Task("Rahat", ""));
        list.add(new Task("Nurdin", ""));
        list.add(new Task("Daniyar", ""));
        list.add(new Task("Nail", ""));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new TaskAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(),"pos = " + position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            Task task = (Task) data.getSerializableExtra("task");
            Log.e("TAG", "title: " + task.getTitle());
            list.add(0, task);
            adapter.notifyDataSetChanged();
        }
    }
}