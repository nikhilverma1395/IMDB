
package com.example.android.floatingactionbuttonbasic;

import com.example.android.common.logger.Log;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FloatingActionButtonBasicFragment extends Fragment implements FloatingActionButton.OnCheckedChangeListener{

    private final static String TAG = "FloatingActionButtonBasicFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fab_layout, container, false);
        FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab_1);
        fab1.setOnCheckedChangeListener(this);
        FloatingActionButton fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab_2);
        fab2.setOnCheckedChangeListener(this);
        return rootView;
    }


    @Override
    public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {
        switch (fabView.getId()){
            case R.id.fab_1:
                Log.d(TAG, String.format("FAB 1 was %s.", isChecked ? "checked" : "unchecked"));
                break;
            case R.id.fab_2:
                Log.d(TAG, String.format("FAB 2 was %s.", isChecked ? "checked" : "unchecked"));
                break;
            default:
                break;
        }
    }
}
