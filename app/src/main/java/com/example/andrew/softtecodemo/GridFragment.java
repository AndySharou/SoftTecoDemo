package com.example.andrew.softtecodemo;

/**
 * Created by Andrew on 06.01.2017.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

@SuppressLint("ValidFragment")
public class GridFragment extends Fragment {

    private GridView mGridView;
    private GridAdapter mGridAdapter;
    GridItems[] gridItems = {};
    private Activity activity;

    public GridFragment(GridItems[] gridItems, Activity activity) {
        this.gridItems = gridItems;
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.grid, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null) {

            mGridAdapter = new GridAdapter(activity, gridItems);
            if (mGridView != null) {
                mGridView.setAdapter(mGridAdapter);
            }

            mGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view,
                                        int position, long id) {
                    onGridItemClick((GridView) parent, view, position, id);
                }
            });
        }
    }

    public void onGridItemClick(GridView g, View v, int position, long id) {
//        Toast.makeText(
//                activity,
//                "User ID is: "
//                        + gridItems[position].userId, Toast.LENGTH_LONG).show();
        Log.i("TAG", "User ID CLICKED " + gridItems[position].userId);
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra("userId",gridItems[position].userId);
        intent.putExtra("postId",gridItems[position].postId);
        startActivity(intent);

    }
}