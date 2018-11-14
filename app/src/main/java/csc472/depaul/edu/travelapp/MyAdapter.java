package csc472.depaul.edu.travelapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private ArrayList<String> savedList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView placeTxt;
        public Button placeBtn;
        public MyViewHolder(View v) {
            super(v);
            placeTxt = (TextView) v.findViewById(R.id.placeTxt);
            placeBtn = (Button) v.findViewById(R.id.placeBtn);
        }
    }

    public MyAdapter(ArrayList<String> list) {
        savedList = list;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View placeView = inflater.inflate(R.layout.recycler_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(placeView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final String savedPlace = savedList.get(i);
        TextView tv = myViewHolder.placeTxt;
        tv.setText(savedPlace);
        Button btn = myViewHolder.placeBtn;
        btn.setText("See List");
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //start new activity
                Context context = v.getContext();
                Intent displayPoiListActivity = new Intent(context, DisplayPoiListActivity.class);
                displayPoiListActivity.putExtra("poiKey", savedPlace);
                context.startActivity(displayPoiListActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedList.size();
    }
}
