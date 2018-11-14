package csc472.depaul.edu.travelapp;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.*;

public class OpenJSONUtils {
    public static ArrayList<PointOfInterest> getPointsOfInterests(Context context, String pointsOfInterestResponse) throws JSONException {
        final String OWM_MESSAGE_CODE = "cod";
        JSONObject pointsOfInterestJSON = new JSONObject(pointsOfInterestResponse);
        if (pointsOfInterestJSON.has(OWM_MESSAGE_CODE)) {
            int errorCode = pointsOfInterestJSON.getInt(OWM_MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray resultsArray = pointsOfInterestJSON.getJSONArray("results");
        ArrayList<PointOfInterest> poiList = new ArrayList<>();
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject poiJSON = resultsArray.getJSONObject(i);
            PointOfInterest poi = new PointOfInterest();
            poi.formattedAddress = poiJSON.getString("formatted_address");
            poi.name = poiJSON.getString("name");

            JSONObject locationJSON = poiJSON.getJSONObject("geometry").getJSONObject("location");
            poi.latitude = locationJSON.getString("lat");
            poi.longitude = locationJSON.getString("lng");

            try {
                JSONArray photosArrayJSON = poiJSON.getJSONArray("photos");
                JSONObject photosJSON = photosArrayJSON.getJSONObject(0);
                poi.photoRef = photosJSON.getString("photo_reference");
            } catch (JSONException e){
                poi.photoRef = null;
            }

            poiList.add(poi);
        }

        return poiList;
    }
}