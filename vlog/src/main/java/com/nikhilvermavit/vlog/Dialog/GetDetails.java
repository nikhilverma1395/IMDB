package com.nikhilvermavit.vlog.Dialog;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.Fragment.ListAccounts;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.Sql.DataSource;
import com.nikhilvermavit.vlog.SqlModel;
import com.nikhilvermavit.vlog.TabActivity;

import java.util.List;


/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class GetDetails extends DialogFragment implements View.OnClickListener, TextWatcher {

    private EditText uname, pass, pass_re, wifi_network_name;
    private Button dismiss_reg;
    private TextView pass_no_match, studVit;
    EditText datepickop;
    private String day = "-1";
    TextView enter_cred_head;

    public GetDetails() {
    }

    public void setTypeFaceforAll() {
        Typeface typeface = TabActivity.getRaleway(Config.RALEWAY_REG);
        uname.setTypeface(typeface);
        pass.setTypeface(typeface);
        pass_re.setTypeface(typeface);
        wifi_network_name.setTypeface(typeface);
        dismiss_reg.setTypeface(typeface);
        pass_no_match.setTypeface(typeface);
        datepickop.setTypeface(typeface);
        enter_cred_head.setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        studVit.setTypeface(TabActivity.getRaleway(Config.RALEWAY_LIGHT));
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
        setTypeFaceforAll();
        dismiss_reg.setEnabled(false);
        pass_no_match.setVisibility(View.GONE);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        uname.requestFocus();
        String tr = wifi_network_name.getText().toString();
        wifi_network_name.setText(TabActivity.sharedPrefs.getValue(Config.networkNamePREF, tr));
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
        dismiss_reg = (Button) v.findViewById(R.id.dismiss_reg);
        pass_no_match = (TextView) v.findViewById(R.id.pass_no_match);
        studVit = (TextView) v.findViewById(R.id.studVit);
        datepickop = (EditText) v.findViewById(R.id.datepicketop);
        enter_cred_head = (TextView) v.findViewById(R.id.enter_cred_head);
        datepickop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().trim().equals("") || s.length() != 0)
                        if (Integer.parseInt(s.toString().trim()) >= 31) {
                            datepickop.setError("Invalid Date");
                        } else {
                            dismiss_reg.setEnabled(true);
                        }
                } catch (NumberFormatException e) {
                    datepickop.setError("Enter Date");
                    dismiss_reg.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        wifi_network_name = (EditText) v.findViewById(R.id.wifi_network_name);
        dismiss_reg.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dismiss_reg) {
            day = datepickop.getText().toString().trim();
            if (day.equals("") || day.length() == 0)
                day = "01";
            Toast.makeText(getActivity(), "Credentials Saved ", Toast.LENGTH_SHORT).show();
            String unamea = uname.getText().toString();
            String passa = pass_re.getText().toString();

            TabActivity.sharedPrefs.storeIntValue(Config.dateReneWPREF, Integer.parseInt(day));
            TabActivity.sharedPrefs.storeValue(Config.unamePREF, uname.getText().toString());
            TabActivity.sharedPrefs.storeValue(Config.passPREF, pass_re.getText().toString());
            TabActivity.sharedPrefs.storeValue(Config.networkNamePREF, wifi_network_name.getText().toString().trim());
            DataSource dataSource = new DataSource(getActivity());
            List<SqlModel> list = dataSource.getAllCredentials();
            if (list == null) {
                dataSource.create(new SqlModel(uname.getText().toString(), pass_re.getText().toString(), day + ""));
                try {
                    new ListAccounts().addFirst(new SqlModel(uname.getText().toString(), pass_re.getText().toString(), day + ""));

                    ListAccounts.recyclerView.setVisibility(View.VISIBLE);
                    ListAccounts.error.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (checkdup(list, unamea, passa)) {
                    try {
                        dataSource.create(new SqlModel(uname.getText().toString(), pass_re.getText().toString(), day + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ListAccounts.adapter.add(new SqlModel(uname.getText().toString(), pass_re.getText().toString(), day + ""));
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
