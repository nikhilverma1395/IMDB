package com.example.vlog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Barry Allen on 2/1/2015.
 */
public class about_me extends Fragment {
    public about_me() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about_me, container, false);
        TextView detail = (TextView) view.findViewById(R.id.details);
        String DET = "\t\tNikhil Verma \n\t\t Btech 2nd year Cse" +
                "\n\n About The App : \n\n  #\t\tI was inspired by Volsbb Auto Login App and thought to make  a app myself.\n \n " +
                "#\t\t This App is made for students of Vellore Institute Of Technology , but it will work with any ProntoNetworks host.\n \n " +
                "#\t\t How to Use The App :\n " +
                "#\t\t Enter The details at the start of App and Click blue button for Login And Red for Logout . \n \n" +
                "#\t\t If your Wifi is Turned Off(If Mobile Data is turned On , it will turn it off automatically in the way of turning wifi on ) , app will show a button in bottom  , click that to turn wifi On and it will try to  connect" +
                "to VOLSBB automatically . Sometimes it doesn't connect to VOLSBB at first attempt ,so try 1-2 times again , or manually connect from settings." +
                "\n\n#\t\t There is a reset button in settings to reset your Login Credentials ." +
                "#\n\n\t\t Thanks For Reading .";
        detail.setText(DET);
        return view;
    }
}
