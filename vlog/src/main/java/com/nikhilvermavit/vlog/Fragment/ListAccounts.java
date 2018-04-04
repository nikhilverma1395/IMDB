package com.nikhilvermavit.vlog.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhilvermavit.vlog.Adapter.RecyclerAdapter;
import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.Dialog.GetDetails;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.Sql.DataSource;
import com.nikhilvermavit.vlog.SqlModel;
import com.nikhilvermavit.vlog.TabActivity;
import com.nikhilvermavit.vlog.view.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 2/20/2015.
 */
public class ListAccounts extends Fragment implements View.OnClickListener {
    public static RecyclerAdapter adapter;
    public static RecyclerView recyclerView;
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
        recyclerView.setAdapter(adapter = new RecyclerAdapter(list1, context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_search, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_list);
        error = (TextView) v.findViewById(R.id.error_inlist);
        error.setTypeface(TabActivity.getRaleway(Config.RALEWAY_BOLD));
        newAccount = (ImageView) v.findViewById(R.id.registernewaccount);
        newAccount.setOnClickListener(this);
        context = getActivity();
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        try {
            list = new DataSource(getActivity()).getAllCredentials();
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        }
        adapter = new RecyclerAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onItem(position);
            }
        }));
        if (list != null) {
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

        return v;
    }

    private void showGetDetailsDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        GetDetails df = GetDetails.newInstance("");
        df.show(fm, "dialog");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registernewaccount:
                showGetDetailsDialog();
                break;
        }
    }

    public void onItem(int position) {
        try {
            ImageButton s = (ImageButton) recyclerView.getChildAt(position).findViewById(R.id.imageButton_select);
            s.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TabActivity.sharedPrefs.storeValue(Config.unamePREF, adapter.getItem(position).getUsername());
        TabActivity.sharedPrefs.storeValue(Config.passPREF, adapter.getItem(position).getPassword());
        TabActivity.sharedPrefs.storeIntValue(Config.dateReneWPREF, Integer.parseInt(adapter.getItem(position).getRndate()));
        Toast.makeText(getActivity(), "Selected ' " + adapter.getItem(position).getUsername() + " '", Toast.LENGTH_SHORT).show();
        int items = 0;
        try {
            items = adapter.getItemCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < items; i++) {
            if (i != position) {
                try {
                    ImageButton ss = (ImageButton) recyclerView.getChildAt(i).findViewById(R.id.imageButton_select);
                    ss.setVisibility(View.INVISIBLE);
                } catch (Exception e) {


                }
            }
        }
    }

}

