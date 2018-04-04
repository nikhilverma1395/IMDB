package com.nikhilvermavit.vlog.Dialog;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.TabActivity;


/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class AboutApp extends DialogFragment implements View.OnClickListener {

    private Button b2;
    private TextView head_info, info1, info2, info3, info4;

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
        b2 = (Button) v.findViewById(R.id.Dialog_b2);
        head_info = (TextView) v.findViewById(R.id.head_info);
        info1 = (TextView) v.findViewById(R.id.data_info_1);
        info2 = (TextView) v.findViewById(R.id.data_info_2);
        info3 = (TextView) v.findViewById(R.id.data_info_3);
        info4 = (TextView) v.findViewById(R.id.data_info_4);
        b2.setOnClickListener(this);
        changeFont();
    }

    private void changeFont() {
        Typeface typeface = TabActivity.getRaleway(Config.RALEWAY_REG);
        head_info.setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        b2.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        info1.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        info2.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        info3.setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        info4.setTypeface(TabActivity.getRaleway(Config.RALEWAY_LIGHT));
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Dialog_b2) {
            getDialog().dismiss();
        }

    }

}
