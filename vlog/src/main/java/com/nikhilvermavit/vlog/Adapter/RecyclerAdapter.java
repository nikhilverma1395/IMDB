package com.nikhilvermavit.vlog.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nikhilvermavit.vlog.Config;
import com.nikhilvermavit.vlog.Fragment.ListAccounts;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.Sql.DataSource;
import com.nikhilvermavit.vlog.SqlModel;
import com.nikhilvermavit.vlog.TabActivity;

import java.util.List;

/**
 * Created by Nikhil Verma on 1/24/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Account> {
    public static List<SqlModel> list;
    private static Context context;

    public RecyclerAdapter(List<SqlModel> list, Context context) {
        RecyclerAdapter.list = list;
        RecyclerAdapter.context = context;
    }

    @Override
    public Account onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        Account pvh = new Account(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(Account holder, final int position) {
        holder.uname.setText(getItem(position).getUsername());
        try {
            String enc = getItem(position).getPassword().substring(0, 2);
            String enc1 = getItem(position).getPassword().substring(2, getItem(position).getPassword().length());
            int l = enc1.length();
            String f = "";
            while (l-- != 0) {
                f = f + "*";
            }
            holder.pass.setText(enc + f);
        } catch (Exception e) {
            e.printStackTrace();
            holder.pass.setText(getItem(position).getPassword());
        }
        String username = TabActivity.sharedPrefs.getValue(Config.unamePREF, "null");
        try {
            if (username.equals(getItem(position).getUsername())) {
                holder.selected.setVisibility(View.VISIBLE);
            } else if (list.size() == 1) {
                holder.selected.setVisibility(View.VISIBLE);
                TabActivity.sharedPrefs.storeValue(Config.unamePREF, getItem(0).getUsername());
                TabActivity.sharedPrefs.storeValue(Config.passPREF, getItem(0).getPassword());
            } else {
                holder.selected.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete this account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                delete(getItem(position).getUsername(), position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.trash)
                        .show();

            }
        });

    }

    private void delete(final String _id, final int pos) {
        final DataSource dataSource = new DataSource(context);
        try {

            try {
                try {
                    list.remove(pos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    dataSource.deleteFrompass(_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (dataSource.getEntriesCount() == 0) {
                ListAccounts.recyclerView.setVisibility(View.INVISIBLE);
                ListAccounts.error.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SqlModel getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(SqlModel object) {
        list.add(object);
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Account extends RecyclerView.ViewHolder {
        TextView uname;
        TextView pass;
        ImageButton selected, delete;

        Account(View itemView) {
            super(itemView);
            selected = (ImageButton) itemView.findViewById(R.id.imageButton_select);
            delete = (ImageButton) itemView.findViewById(R.id.imageButton_delete);
            uname = (TextView) itemView.findViewById(R.id.uname_search);
            pass = (TextView) itemView.findViewById(R.id.pass_search);
            uname.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            pass.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Light.ttf"));
        }
    }

}
