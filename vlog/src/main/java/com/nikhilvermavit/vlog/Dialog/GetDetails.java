package com.nikhilvermavit.vlog.Dialog;

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
import com.nikhilvermavit.vlog.Fragment.ListAccounts;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.Sql.DataSource;
import com.nikhilvermavit.vlog.SqlModel;

import java.util.List;


/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class GetDetails extends DialogFragment implements View.OnClickListener, TextWatcher {

    private EditText uname, pass, pass_re, wifi_network_name;
    private ButtonRectangle dismiss_reg;
    private TextView pass_no_match;

    public GetDetails() {
    }

    public static GetDetails newInstance(String title) {
        GetDetails df = new GetDetails();
        Bundle args = new Bundle();
        args.putString("data", title);
        df.setArguments(args);
        return df;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.get_details_dialog, container, false);
        init(v);
        dismiss_reg.setEnabled(false);
        pass_no_match.setVisibility(View.GONE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        uname.requestFocus();
        String username = uname.getText().toString();
        uname.setText(getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("username_volsbb", ""));
        pass.setText(getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("password_volsbb", ""));
        pass_re.setText(getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("password_volsbb", ""));
        String pass1 = pass.getText().toString();
        String pass2 = pass_re.getText().toString();
        String tr = wifi_network_name.getText().toString();
        wifi_network_name.setText(getActivity().getSharedPreferences("prefs.Volsbb", Context.MODE_PRIVATE).getString("networkName", tr));
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pass_re.getText().toString().equals(s.toString())) {
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
        if (one.equals("") || one1.equals("") || uname.getText().toString().equals(""))
            dismiss_reg.setEnabled(false);
        return v;
    }


    private void init(ViewGroup v) {
        uname = (EditText) v.findViewById(R.id.uname_req);
        pass = (EditText) v.findViewById(R.id.pass_reg);
        pass_re = (EditText) v.findViewById(R.id.pass_confirm_reg);
        dismiss_reg = (ButtonRectangle) v.findViewById(R.id.dismiss_reg);
        pass_no_match = (TextView) v.findViewById(R.id.pass_no_match);
        wifi_network_name = (EditText) v.findViewById(R.id.wifi_network_name);
        dismiss_reg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dismiss_reg) {
            Toast.makeText(getActivity(), "Credentials Saved ", Toast.LENGTH_LONG).show();
            String unamea = uname.getText().toString();
            String passa = pass_re.getText().toString();

            getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).edit().putString("username_volsbb", uname.getText().toString()).commit();
            getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).edit().putString("password_volsbb", pass_re.getText().toString()).commit();
            getActivity().getSharedPreferences("prefs.Volsbb", Context.MODE_PRIVATE).edit().putString("networkName", wifi_network_name.getText().toString().trim()).commit();
            DataSource dataSource = new DataSource(getActivity());
            List<SqlModel> list = dataSource.getAllCredentials();
            try {
                Log.d("ls", list.size() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list == null) {
                dataSource.create(new SqlModel(uname.getText().toString(), pass_re.getText().toString()));
                try {
                    new ListAccounts().addFirst(new SqlModel(uname.getText().toString(), pass_re.getText().toString()));

                    ListAccounts.listView.setVisibility(View.VISIBLE);
                    ListAccounts.error.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (checkdup(list, unamea, passa)) {
                    try {
                        dataSource.create(new SqlModel(uname.getText().toString(), pass_re.getText().toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ListAccounts.myAdapterr.add(new SqlModel(uname.getText().toString(), pass_re.getText().toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(getActivity(), "Account Already Exists", Toast.LENGTH_SHORT).show();
            }
            getDialog().dismiss();


        }
    }

    private boolean checkdup(List<SqlModel> list, String unamea, String passa) {
        for (SqlModel ob : list) {
            if (ob.getUsername().equals(unamea)) {
                return false;
            }
        }
        return true;
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
