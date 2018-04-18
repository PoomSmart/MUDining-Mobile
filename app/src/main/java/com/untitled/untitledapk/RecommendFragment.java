package com.untitled.untitledapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RecommendFragment extends Fragment {

    Button recommendButton;

    public RecommendFragment() {
    }

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        recommendButton = view.findViewById(R.id.recommend_button);
        recommendButton.setOnClickListener(v -> onRecommendButtonPressed());
        return view;
    }

    public void onRecommendButtonPressed() {
        startActivity(new Intent(getActivity(), RecommendListActivity.class));
    }
}
