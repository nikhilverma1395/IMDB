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

import com.gc.materialdesign.views.ButtonRectangle;
import com.nikhilvermavit.vlog.Dialog.AboutApp;
import com.nikhilvermavit.vlog.R;

/**
 * Created by Nikhil Verma on 3/22/2015.
 */
public class License extends Fragment implements View.OnClickListener {
ButtonRectangle howtouse; CardView mdesign, met, aos, me;

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
        mdesign = (CardView) view.findViewById(R.id.mdesign);

        met = (CardView) view.findViewById(R.id.medittext);
        me = (CardView) view.findViewById(R.id.me);
        howtouse = (ButtonRectangle) view.findViewById(R.id.howtouse);

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
        return view;

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
