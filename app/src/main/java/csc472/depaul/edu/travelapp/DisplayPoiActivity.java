package csc472.depaul.edu.travelapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import java.io.IOException;
import android.util.Log;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.net.URL;
import android.graphics.BitmapFactory;

public class DisplayPoiActivity extends AppCompatActivity implements View.OnClickListener {

    private String poiSearch;
    private ArrayList<PointOfInterest> poiList;
    private ArrayList<PointOfInterest> poiYes = new ArrayList<>();
    private TextView poiLoc;
    private TextView poiName;
    private ImageView poiImg;
    SharedPreference sharedPreference;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v("Rotation", "THREAD_ONSAVE");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("currentPoiList", poiList);
        outState.putParcelableArrayList("currentYesPoiList", poiYes);
        outState.putString("poiSearch", poiSearch);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference = new SharedPreference();
        setContentView(R.layout.activity_display_poi);

        if (savedInstanceState != null) {
            Log.v("Rotation", "ROTATION");
            poiList = savedInstanceState.getParcelableArrayList("currentPoiList");
            poiYes = savedInstanceState.getParcelableArrayList("currentYesPoiList");
            poiSearch = savedInstanceState.getString("poiSearch");

        } else {
            Bundle data = getIntent().getExtras();
            poiList = data.getParcelableArrayList("poiList");
            poiSearch = data.getString("poiSearch");
        }


        /*final TextView  locTv = findViewById(R.id.place_location);
        final TextView descTv = findViewById(R.id.place_description);
        final ImageView picTv = findViewById(R.id.place_image);*/
        poiLoc = findViewById(R.id.place_location);
        poiName = findViewById(R.id.place_description);
        poiImg = findViewById(R.id.place_image);
        Button no = (Button) findViewById(R.id.no_button);
        Button yes = (Button) findViewById(R.id.yes_button);
        PointOfInterest p = poiList.get(0);
        if (p.photoRef != null) {
            new ImageDownloadTask((ImageView) findViewById(R.id.place_image)).execute(p.photoUrl);
        }
        else {
            poiImg.setImageResource(R.drawable.no_image_icon);
        }
        poiLoc.setText(p.getFormattedAddress());
        poiName.setText(p.getName());

        no.setOnClickListener(this);
        yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int clicked = v.getId();
        if (clicked == R.id.yes_button) {
            poiYes.add(poiList.get(0));
        }
        poiList.remove(0);

        if (poiList.isEmpty()) {
            sharedPreference.saveFavorites(DisplayPoiActivity.this, poiSearch, poiYes);
            sharedPreference.saveFavoritesKey(DisplayPoiActivity.this, poiSearch);

            Intent goBackMainActivity = new Intent(getNewUserActivity(), MainActivity.class);
            startActivity(goBackMainActivity);
        }
        else {
            PointOfInterest p = poiList.get(0);
            Log.v("POI", p.name);
            if (p != null) {
                if (p.photoRef != null) {
                    new ImageDownloadTask((ImageView) findViewById(R.id.place_image)).execute(p.photoUrl);
                }
                else {
                    poiImg.setImageResource(R.drawable.no_image_icon);
                }
                poiLoc.setText(p.getFormattedAddress());
                poiName.setText(p.getName());
            }
        }
    }

    private Bitmap downloadImage(String stringUrl) throws IOException{
        Bitmap bitmap = null;
        InputStream iStream = null;
        try{
            URL url = new URL(stringUrl);

            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

            urlConnect.connect();

            iStream = urlConnect.getInputStream();

            bitmap = BitmapFactory.decodeStream(iStream);
        } catch(Exception e) {
            Log.d("Exception while downloading photo url", e.toString());
        } finally { iStream.close(); }

        return bitmap;
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapIV;

        public ImageDownloadTask(ImageView bitmapIV) {
            this.bitmapIV = bitmapIV;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap bit = null;
            try {
                bit = downloadImage(url[0]);
            } catch(Exception e) {
                Log.d("Background Task", e.toString());
            }
            return bit;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bitmapIV.setImageBitmap(result);
        }
    }

    private final DisplayPoiActivity getNewUserActivity()
    {
        return this;
    }
}