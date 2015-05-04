package com.nikhilvermavit.vlog.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.nikhilvermavit.vlog.R;


/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class AboutApp extends DialogFragment implements View.OnClickListener {
    String tet;
    private TextView head, body;
    private ButtonRectangle b2, howtouse;

    public AboutApp() {
    }

    public static AboutApp newInstance(String title) {
        AboutApp df = new AboutApp();
        Bundle args = new Bundle();
        args.putString("data", title);
        df.setArguments(args);
        return df;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.firstd;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.info_dialog, container, false);
        init(v);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return v;
    }


    private void init(ViewGroup v) {
        b2 = (ButtonRectangle) v.findViewById(R.id.Dialog_b2);
        howtouse = (ButtonRectangle) v.findViewById(R.id.howtouse);
        b2.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Dialog_b2) {
            getDialog().dismiss();
        }

    }

}
