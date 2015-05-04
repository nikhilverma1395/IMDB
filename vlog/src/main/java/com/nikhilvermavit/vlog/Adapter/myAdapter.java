package com.nikhilvermavit.vlog.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nikhilvermavit.vlog.Fragment.ListAccounts;
import com.nikhilvermavit.vlog.R;
import com.nikhilvermavit.vlog.Sql.DataSource;
import com.nikhilvermavit.vlog.SqlModel;

import java.util.List;


/**
 * Created by Nikhil Verma on 2/21/2015.
 */
public class myAdapter extends ArrayAdapter<SqlModel> {
    public static List<SqlModel> list;
    Context activity;
    ListView lister;

    public myAdapter(List<SqlModel> list, Context activity, ListView listView) {
        super(activity, 0, 0, list);
        this.list = list;
        this.activity = activity;
        lister = listView;
        try {
            lister.getAdapter().notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SqlModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public void add(SqlModel object) {
        list.add(object);
        try {
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View v = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder.uname = (TextView) convertView.findViewById(R.id.uname_search);
            holder.pass = (TextView) convertView.findViewById(R.id.pass_search);
            holder.delete = (ImageButton) convertView.findViewById(R.id.imageButton_delete);
            holder.select = (ImageButton) convertView.findViewById(R.id.imageButton_select);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
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
        String username = activity.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("username_volsbb", "null");
        String pass = activity.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).getString("password_volsbb", "null");
        try {
            if (username.equals(getItem(position).getUsername())) {
                holder.select.setVisibility(View.VISIBLE);
            } else if (list.size() == 1) {
                holder.select.setVisibility(View.VISIBLE);
                activity.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).edit().putString("username_volsbb", getItem(0).getUsername()).commit();
                activity.getSharedPreferences("VOLSBB_LOGIN", Context.MODE_PRIVATE).edit().putString("password_volsbb", getItem(0).getPassword()).commit();
            } else {
                holder.select.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final View vier = convertView;
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete this account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                delete(getItem(position).getUsername(), position, vier);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_delete)
                        .show();

            }
        });
        return convertView;
    }

    private void delete(final String _id, final int pos, View v) {
        final DataSource dataSource = new DataSource(activity);
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
                    boolean fr = dataSource.deleteFrompass(_id);
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
                ListAccounts.listView.setVisibility(View.INVISIBLE);
                ListAccounts.error.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder {
        TextView uname;
        TextView pass;
        ImageButton delete, select;
    }
}


