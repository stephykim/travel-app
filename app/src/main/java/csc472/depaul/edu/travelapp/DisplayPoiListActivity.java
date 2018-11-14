package csc472.depaul.edu.travelapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayPoiListActivity extends AppCompatActivity {
    SharedPreference sharedPreference;
    private String poiKey;
    private ArrayList<PointOfInterest> poiYes = new ArrayList<>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("currentYesPoiList", poiYes);
        outState.putString("poiKey", poiKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_poi_list);
        sharedPreference = new SharedPreference();

        if (savedInstanceState != null) {
            poiYes = savedInstanceState.getParcelableArrayList("currentYesPoiList");
            poiKey = savedInstanceState.getString("poiKey");

        } else {
            Bundle data = getIntent().getExtras();
            poiKey = data.getString("poiKey");
        }

        if (poiKey != null) {
            TextView tv = findViewById(R.id.poiHeaderTxt);
            tv.setText(poiKey);
            poiYes = sharedPreference.getFavorites(DisplayPoiListActivity.this, poiKey);
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerViewPoi);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        rv.addItemDecoration(divider );

        RecyclerView.Adapter mAdapter = new PoiAdapter(poiKey, poiYes);
        rv.setAdapter(mAdapter);

        Button btn = findViewById(R.id.mapBtn);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //start new activity
                Context context = v.getContext();
                Intent mapListActivity = new Intent(context, MapsActivity.class);
                mapListActivity.putExtra("poiList", poiYes);
                context.startActivity(mapListActivity);
            }
        });
    }
}
