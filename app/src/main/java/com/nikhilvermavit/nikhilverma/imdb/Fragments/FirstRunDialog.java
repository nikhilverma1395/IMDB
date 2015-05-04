package com.nikhilvermavit.nikhilverma.imdb.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.nikhilvermavit.nikhilverma.imdb.R;
import com.gc.materialdesign.views.ButtonRectangle;


/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class FirstRunDialog extends DialogFragment implements View.OnClickListener {
    String tet;
    private TextView head, body;
    private ButtonRectangle b2;

    public FirstRunDialog() {
    }

    public static FirstRunDialog newInstance(String title) {
        FirstRunDialog df = new FirstRunDialog();
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
        tet = "\t 1\t:\tEnter The Correct Title Please ( Spelling + Format ) otherwise there is a problem Searching Movie . \n" +
                " \t 2\t:\t Examples(It isn't case sensitive) : \n\t*\t The Lord of the Rings: The Fellowship of the Ring (use same format i.e :,' ' must be used) \n\t" +
                " *\t The Godfather: Part III (Part must be entered as three III not 3).\n\t" +
                "*\t Other Examples : Iron Man 2\t,\tGame Of Thrones\t,\t Taken 3\t,\t Supernatural etc.. \n\t\t" +
                "Other Things are simple Year is Required If There is A Need of Movie by year like Don(1978) and Don(2006) .\n\t" +
                "3\t:\t If There Is Problem Searching The Movie Then It Will Give you Link to Google with whatever you entered as Query." +
                "Normally If it isn't totally absurd then The The First Link On Google Will Be IMDB Link Of Google ." +
                "\n\n\t\t Thanks For Your Time .   ";

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
