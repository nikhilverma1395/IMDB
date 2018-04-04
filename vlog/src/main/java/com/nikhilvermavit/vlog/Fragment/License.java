package com.nikhilvermavit.vlog.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.Dialog.AboutApp;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.TabActivity;

/**
 * Created by Nikhil Verma on 3/22/2015.
 */
public class License extends Fragment implements View.OnClickListener {
    ButtonRectangle howtouse;
    CardView mdesign, met, aos, me, okhttp;

    public License() {
    }

    public static License newInstance(String text) {
        License f = new License();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.changelog, container, false);
        init(view);
        mdesign = (CardView) view.findViewById(R.id.mdesign);

        met = (CardView) view.findViewById(R.id.medittext);
        okhttp = (CardView) view.findViewById(R.id.okhtt);
        me = (CardView) view.findViewById(R.id.me);
        howtouse = (ButtonRectangle) view.findViewById(R.id.howtouse);
        howtouse.getTextView().setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        howtouse.setOnClickListener(this);
        aos = (CardView) view.findViewById(R.id.androidos);
        mdesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/navasmdc/MaterialDesignLibrary";
                redirect(url);
            }
        });
        met.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/rengwuxian/MaterialEditText";
                redirect(url);
            }
        });

        aos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://source.android.com/source/licenses.html";
                redirect(url);
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/nikhilverma1395";
                redirect(url);
            }
        });
        okhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://square.github.io/okhttp/";
                redirect(url);
            }
        });
        return view;

    }

    private void init(View view) {
        ((TextView) view.findViewById(R.id.dad)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.dad_nik)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.clog)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        ((TextView) view.findViewById(R.id.card1_v)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        ((TextView) view.findViewById(R.id.card1_date)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.card1_n)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.card5_n)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.card2_n)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.card1_det)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.head_1)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        ((TextView) view.findViewById(R.id.card3_n)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.card4_n)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_REG));
        ((TextView) view.findViewById(R.id.card4_name)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        ((TextView) view.findViewById(R.id.card3_name)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        ((TextView) view.findViewById(R.id.card5_name)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        ((TextView) view.findViewById(R.id.card2_name)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        ((TextView) view.findViewById(R.id.card2_lic)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_LIGHT));
        ((TextView) view.findViewById(R.id.card3_lic)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_LIGHT));
        ((TextView) view.findViewById(R.id.card4_lic)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_LIGHT));
        ((TextView) view.findViewById(R.id.card5_lic)).setTypeface(TabActivity.getRaleway(Config.RALEWAY_LIGHT));
    }

    private void redirect(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.howtouse) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AboutApp info = AboutApp.newInstance("");
            info.show(fm, "dialoger");
        }
    }
}
