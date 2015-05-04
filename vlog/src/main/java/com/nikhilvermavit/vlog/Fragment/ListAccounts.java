package com.nikhilvermavit.vlog.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhilvermavit.vlog.Adapter.myAdapter;
import com.nikhilvermavit.vlog.Dialog.GetDetails;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.Sql.DataSource;
import com.nikhilvermavit.vlog.SqlModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 2/20/2015.
 */
public class ListAccounts extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static myAdapter myAdapterr;
    public static ListView listView;
    public static TextView error;
    private static List<SqlModel> list;
    private static Context context;
    ImageView newAccount;

    public ListAccounts() {
    }

    public static ListAccounts newInstance(String text) {

        ListAccounts f = new ListAccounts();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public void addFirst(SqlModel m) {
        List<SqlModel> list1 = new ArrayList<>();
        list1.add(m);
        listView.setAdapter(new myAdapter(list1, context, listView));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_search, container, false);
        listView = (ListView) v.findViewById(R.id.recycler_view_list);
        error = (TextView) v.findViewById(R.id.error_inlist);
        newAccount = (ImageView) v.findViewById(R.id.registernewaccount);
        newAccount.setOnClickListener(this);
        context = getActivity();
        listView.setOnItemClickListener(this);
        list = new DataSource(getActivity()).getAllCredentials();
        myAdapterr = new myAdapter(list, getActivity(), listView);
        if (list != null) {
            listView.setAdapter(myAdapterr);
        } else {
            listView.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

        return v;
    }

    private void showGetDetailsDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        GetDetails df = GetDetails.newInstance("");
        df.show(fm, "dialoger");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registernewaccount:
                showGetDetailsDialog();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            ImageButton s = (ImageButton) listView.getChildAt(position).findViewById(R.id.imageButton_select);
            s.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).edit().putString("username_volsbb", myAdapterr.getItem(position).getUsername()).commit();
        getActivity().getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).edit().putString("password_volsbb", myAdapterr.getItem(position).getPassword()).commit();
        Toast.makeText(getActivity(), "Selected ' " + myAdapterr.getItem(position).getUsername() + " 'as Current Account", Toast.LENGTH_SHORT).show();

        int items = 0;
        try {
            items = listView.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < items; i++) {
            if (i != position) {
                try {
                    ImageButton ss = (ImageButton) listView.getChildAt(i).findViewById(R.id.imageButton_select);
                    ss.setVisibility(View.INVISIBLE);
                } catch (Exception e) {


                }
            }
        }
    }

}

