package csc472.depaul.edu.travelapp;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar searchingProgressBar;
    private String search;
    SharedPreference sharedPreference;
    ArrayList<String> savedList =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference = new SharedPreference();
        setContentView(R.layout.activity_main);

        searchingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        searchingProgressBar.setVisibility(View.INVISIBLE);

        final Button search = findViewById(R.id.searchBtn);
        if (search != null) {
            search.setOnClickListener(onClickSearch);
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        rv.addItemDecoration(divider );

        File f = new File("/data/data/" + getPackageName() + "/shared_prefs/" + "POI_KEYS.xml");
        if (f.exists()){
            savedList = sharedPreference.getFavoritesKeys(MainActivity.this);
        }

        RecyclerView.Adapter mAdapter = new MyAdapter(savedList);
        rv.setAdapter(mAdapter);

    }

    private View.OnClickListener onClickSearch = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            if (validateEditTextField(R.id.searchTxt)) {
                search = getEditViewText(R.id.searchTxt);
                String searchText = removeSpaceStrings(search);
                new getActivitiesInfo().execute(searchText);
            }
        }
    };

    public class getActivitiesInfo extends AsyncTask<String, Void, ArrayList<PointOfInterest>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchingProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<PointOfInterest> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            //should be paris for tests
            String location = params[0];
            URL pointsOfInterestURL = NetworkUtils.buildUrl_PointsOfInterest(location);

            try {
                //get JSON object with points of interest
                String pointsOfInterestResponse = NetworkUtils.getResponseFromHttpUrl(pointsOfInterestURL);
                //get pointsOfInterest object from JSON
                ArrayList<PointOfInterest> poiList = OpenJSONUtils.getPointsOfInterests(MainActivity.this, pointsOfInterestResponse);

                for(PointOfInterest poi : poiList) {
                    URL pointsOfInterestPhotoURL = NetworkUtils.buildUrl_PhotoRef(poi.photoRef);
                    poi.photoUrl = pointsOfInterestPhotoURL.toString();
                }

                return poiList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<PointOfInterest> poiList) {
            searchingProgressBar.setVisibility(View.INVISIBLE);
            if (poiList != null) {

                Intent displayResultsActivity = new Intent(getNewUserActivity(), DisplayPoiActivity.class);

                displayResultsActivity.putExtra("poiList", poiList);
                displayResultsActivity.putExtra("poiSearch", search);
                startActivity(displayResultsActivity);
            }
        }
    }


    //helper functions
    private boolean validateEditTextField(int id)
    {
        boolean isValid = false;

        final EditText editText = findViewById(id);
        if (editText != null)
        {
            String sText = editText.getText().toString();

            isValid = ((sText != null) && (!sText.isEmpty()));
        }

        if (!isValid)
        {
            String sToastMessage = getResources().getString(R.string.fields_cannot_be_empty);
            Toast toast = Toast.makeText(getApplicationContext(), sToastMessage, Toast.LENGTH_LONG);
            toast.show();
        }

        return isValid;
    }

    private final String getEditViewText(int id)
    {
        String sText = "";

        final EditText editText = findViewById(id);
        if (editText != null)
        {
            sText = editText.getText().toString();
        }

        return sText;
    }

    private final String removeSpaceStrings(String input) {
        String newString = input.replaceAll("\\s", "+") + "+point+of+interest";
        return newString;
    }

    private final MainActivity getNewUserActivity()
    {
        return this;
    }
}