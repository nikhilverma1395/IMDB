package com.example.vlog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;


/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class FirstRunDialog_info extends DialogFragment implements View.OnClickListener {
    String tet;
    private TextView head, body;
    private ButtonRectangle b2;

    public FirstRunDialog_info() {
    }

    public static FirstRunDialog_info newInstance(String title) {
        FirstRunDialog_info df = new FirstRunDialog_info();
        Bundle args = new Bundle();
        args.putString("data", title);
        df.setArguments(args);
        return df;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.dialog_frament, container, false);
        init(v);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        body.requestFocus();
        return v;
    }


    private void init(ViewGroup v) {
        head = (TextView) v.findViewById(R.id.text_dialog_head);
        tet = "\n#\t\t This App is made for students of Vellore Institute Of Technology , but it will work with any ProntoNetworks host.\n \n " +
                "#\t\t How to Use The App :\n " +
                "#\t\t Enter The details at the after you dismiss this dialog and Click blue button for Login And Red for Logout . \n \n" +
                "#\t\t If your Wifi is Turned Off (If Mobile Data is turned On , it will turn it off mobile-data automatically in the way of turning wifi on ) , app will show a button in bottom  , click that to turn wifi On and it will try to  connect" +
                "to VOLSBB automatically . Sometimes it doesn't connect to VOLSBB in the  first attempt ,so try again , or manually connect from settings." +
                "\n\n#\t\t There is a reset item in settings to reset your Login Credentials if you want to change your account ." +
                "#\n\n\t\t Thanks For Reading .";
        body = (TextView) v.findViewById(R.id.text_dialog);
        b2 = (ButtonRectangle) v.findViewById(R.id.Dialog_b2);
        b2.setOnClickListener(this);
        body.setText(tet);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Dialog_b2) {
            getDialog().dismiss();
        }
    }

}
