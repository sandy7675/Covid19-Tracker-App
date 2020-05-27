package com.example.covid19;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class Adapter2 extends ArrayAdapter<Data> {
    List<Data> heroList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public Adapter2(Context context, int resource, List<Data> heroList) {
        super(context, resource, heroList);
        this.context = context;
        this.resource = resource;
        this.heroList = heroList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view

        TextView State = view.findViewById(R.id.loc);
        TextView confirm = view.findViewById(R.id.confirm);
        TextView recover = view.findViewById(R.id.Recovered1);
        TextView deaths = view.findViewById(R.id.Death);
        ConstraintLayout linearLayout=view.findViewById(R.id.l1);


        final Data hero = heroList.get(position);
        State.setText(hero.getLoc());
        confirm.setText(hero.getCon());
        recover.setText(hero.getRec());
        deaths.setText(hero.getDet());
        return view;

    }
}
