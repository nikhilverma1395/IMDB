package com.example.nikhilverma.imdb.Activites;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.nikhilverma.imdb.Adapters.CardAdapter;
import com.example.nikhilverma.imdb.Models.ActorDetailModel;
import com.example.nikhilverma.imdb.R;
import com.example.nikhilverma.imdb.Fragments.web;

import java.util.List;

/**
 * Created by Nikhil Verma on 04-01-2015.
 */
public class ActorDetail extends ActionBarActivity {
    RecyclerView recyclerView;
    static Context context;
    static List<ActorDetailModel> list;
    static android.support.v4.app.Fragment fragment;

    public ActorDetail() {
        list = MainActivity.getList();
        context = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);
        recyclerView = (RecyclerView) findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);
        context = getApplicationContext();
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        CardAdapter ca = new CardAdapter(list, context);
        recyclerView.setAdapter(ca);
        ca.SetOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                fragment = new web(list.get(position).getUrlPhoto());
                Log.i("fragment", list.get(position).getUrlPhoto());
                getSupportFragmentManager().beginTransaction().add(R.id.recylerview_, fragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        recyclerView.setAdapter(null);
        finish();
    }
}
