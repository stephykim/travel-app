package csc472.depaul.edu.travelapp;

import android.net.Uri;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    //private static final String format = "json";
    //private static final int numResults = 10;

    private static final String apiKey = "";

    private static final String GOOGLE_PLACE_BASE_URL = "https://maps.googleapis.com/maps/api/place";

    public static URL buildUrl_PointsOfInterest(String locationQuery) {
        Uri.Builder uriBuilder = Uri.parse(GOOGLE_PLACE_BASE_URL).buildUpon()
                .appendPath("textsearch")
                .appendPath("json")
                .appendQueryParameter("query", locationQuery)
                .appendQueryParameter("language", "en")
                .appendQueryParameter("key", apiKey);

        URL url = null;
        try {
            url = new URL(uriBuilder.toString().replaceAll("%2B", "+"));
            //url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }

    public static URL buildUrl_PhotoRef(String photoRef) {
        Uri.Builder uriBuilder = Uri.parse(GOOGLE_PLACE_BASE_URL).buildUpon()
                .appendPath("photo")
                .appendQueryParameter("maxwidth", "800")
                .appendQueryParameter("photoreference", photoRef)
                .appendQueryParameter("key", apiKey);

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Photo Ref URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.v("HTTP_", "HTTP_RESPONSE");

        HttpURLConnection huc = (HttpURLConnection) url.openConnection();


        try {

            InputStream in = new BufferedInputStream(huc.getInputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(in), 1000);

            String y;
            StringBuilder sb = new StringBuilder();
            while((y = br.readLine())!=null){
                sb.append(y);
                Log.v("HTTP_OUTPUT", y);
            }
            return sb.toString();
        } catch(Exception e){
            Log.v("HTTP_", "HTTP_FAILED");
            Log.v("HTTP_", e.getLocalizedMessage());
            return null;
        }
        finally {
            huc.disconnect();
        }
    }


}