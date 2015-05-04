package com.nikhilvermavit.nikhilverma.imdb.Activites;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nikhilvermavit.nikhilverma.imdb.Adapters.CardAdapter;
import com.nikhilvermavit.nikhilverma.imdb.Models.ActorDetailModel;
import com.nikhilvermavit.nikhilverma.imdb.R;

import java.util.List;

/**
 * Created by Nikhil Verma on 04-01-2015.
 */
public class ActorDetail extends ActionBarActivity {
    static Context context;
    static List<ActorDetailModel> list;
    RecyclerView recyclerView;

    public ActorDetail() {
        list = MyTask.getList();
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
        }

}
