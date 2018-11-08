package csc472.depaul.edu.travelapp;


import android.graphics.Point;
import android.net.Uri;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.net.URL;
import android.graphics.BitmapFactory;

public class DisplayPoiActivity extends AppCompatActivity {

    private PointOfInterest poi;
    private ArrayList<PointOfInterest> poiList;
    ImageView iView = null;
    private ArrayList<PointOfInterest> poiYes;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v("Rotation", "THREAD_ONSAVE");
        super.onSaveInstanceState(outState);
        outState.putParcelable("currentPoi", poi);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("onCreate", "THREAD_INITIALLY_CREATED");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_poi);

        if (savedInstanceState != null) {
            Log.v("Rotation", "ROTATION");
            // poiList = savedInstanceState.getParcelable("currentInvestment");

        } else {
            Bundle data = getIntent().getExtras();
            poiList = data.getParcelableArrayList("poiList");
        }


        TextView  locTv = findViewById(R.id.place_location);
        TextView descTv = findViewById(R.id.place_description);
        Button no = (Button) findViewById(R.id.no_button);
        Button yes = (Button) findViewById(R.id.yes_button);

        PointOfInterest p = poiList.get(0);
        new ImageDownloadTask((ImageView) findViewById(R.id.place_image)).execute(p.photoUrl);

        locTv.setText(p.getFormattedAddress());
        descTv.setText(p.getName());

        /*for(final PointOfInterest p : poiList) {
            new ImageDownloadTask((ImageView) findViewById(R.id.place_image)).execute(p.photoUrl);

            locTv.setText(p.getFormattedAddress());
            descTv.setText(p.getName());

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    return;
                }
            });
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    poiYes.add(p);
                    return;
                }
            });
        }*/
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
}