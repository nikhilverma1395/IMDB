package com.example.vlog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;


/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class FirstRunDialog extends DialogFragment implements View.OnClickListener, TextWatcher {

    private EditText uname, pass, pass_re;
    private ButtonRectangle dismiss_reg;
    private TextView pass_no_match;

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
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.get_details_dialog, container, false);
        init(v);
        dismiss_reg.setEnabled(false);
        pass_no_match.setVisibility(View.GONE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        uname.requestFocus();
        String username = uname.getText().toString();
        String pass1 = pass.getText().toString();
        String pass2 = pass_re.getText().toString();
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pass_re.getText().toString().equals(s.toString())) {
                    pass_no_match.setVisibility(View.GONE);

                    dismiss_reg.setEnabled(true);
                } else
                    pass_no_match.setVisibility(View.VISIBLE);
                dismiss_reg.setEnabled(false);
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pass_re.addTextChangedListener(this);
        if (!pass.getText().toString().equals(pass_re.getText().toString())) {
            pass_no_match.setVisibility(View.VISIBLE);
            dismiss_reg.setEnabled(false);
        } else
            dismiss_reg.setEnabled(true);

        Log.d("boooyyy", username + "\t" + pass.getText().toString() + "/t" + pass_re.getText().toString());
        String one = pass.getText().toString();
        String one1 = pass_re.getText().toString();
        if (one.equals("") || one1.equals("")||uname.getText().toString().equals(""))
            dismiss_reg.setEnabled(false);
        return v;
    }


    private void init(ViewGroup v) {
        uname = (EditText) v.findViewById(R.id.uname_req);
        pass = (EditText) v.findViewById(R.id.pass_reg);
        pass_re = (EditText) v.findViewById(R.id.pass_confirm_reg);
        dismiss_reg = (ButtonRectangle) v.findViewById(R.id.dismiss_reg);
        pass_no_match = (TextView) v.findViewById(R.id.pass_no_match);
        dismiss_reg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dismiss_reg) {
            Toast.makeText(getActivity(), "Credentials Saved ", Toast.LENGTH_LONG).show();
            getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).edit().putString("username_volsbb", uname.getText().toString()).commit();
            getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).edit().putString("password_volsbb", pass_re.getText().toString()).commit();
            getDialog().dismiss();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (pass.getText().toString().equals(s.toString())) {
            pass_no_match.setVisibility(View.GONE);
            dismiss_reg.setEnabled(true);
        } else {
            pass_no_match.setVisibility(View.VISIBLE);
            dismiss_reg.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
