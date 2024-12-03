package com.example.b07group57;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.b07group57.models.OffSetProjectsList;
import com.example.b07group57.models.OffsetProject;

import java.util.List;

public class EcoBalanceFragment extends Fragment {
    private RecyclerView recyclerView;
    private OffSetProjectsList projectsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eco_balance, container, false);

        ((MainActivity) getActivity()).showNavigationBar(false);
        recyclerView = view.findViewById(R.id.recycler_view_offset_projects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        projectsList = new OffSetProjectsList();
        List<OffsetProject> projects = projectsList.fetchProjects();

        OffsetProjectsAdapter adapter = new OffsetProjectsAdapter(projects);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
