package csc472.depaul.edu.travelapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.MyViewHolder>{
    private ArrayList<PointOfInterest> savedList;
    private String poiKey;
    SharedPreference sharedPreference = new SharedPreference();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView poiNameTxt;
        public TextView poiAddressTxt;
        public Button poiDeleteBtn;
        public MyViewHolder(View v) {
            super(v);
            poiNameTxt = (TextView) v.findViewById(R.id.poiNameTxt);
            poiAddressTxt = (TextView) v.findViewById(R.id.poiAddressTxt);
            poiDeleteBtn = (Button) v.findViewById(R.id.deleteBtn);
        }
    }

    public PoiAdapter(String poi, ArrayList<PointOfInterest> list) {
        poiKey = poi;
        savedList = list;
    }

    @Override
    public PoiAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View placeView = inflater.inflate(R.layout.recycler_list_poi, parent, false);
        MyViewHolder vh = new MyViewHolder(placeView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PoiAdapter.MyViewHolder myViewHolder, final int i) {
        final PointOfInterest poi = savedList.get(i);
        TextView tvName = myViewHolder.poiNameTxt;
        tvName.setText(poi.name);
        TextView tvAddress = myViewHolder.poiAddressTxt;
        tvAddress.setText(poi.formattedAddress);
        Button btn = myViewHolder.poiDeleteBtn;
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //start new activity
                Context context = v.getContext();
                sharedPreference.removeFavorite(context, poiKey, i);
                savedList = sharedPreference.getFavorites(context, poiKey);
                refreshEvents();
            }
        });
    }

    public void refreshEvents() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return savedList.size();
    }
}