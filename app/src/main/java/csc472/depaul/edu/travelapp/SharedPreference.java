package csc472.depaul.edu.travelapp;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class SharedPreference {
    public static final String PREFS_NAME = "TRAVEL_APP";
    public static final String FAVORITES_KEYS = "POI_KEYS";

    public SharedPreference() {
        super();
    }

    public void saveFavorites(Context context, String favoritesName, List<PointOfInterest> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(favoritesName, jsonFavorites);

        editor.commit();
    }

    public void saveFavoritesKey(Context context, String favoritesName) {
        SharedPreferences settings;
        Editor editor;
        ArrayList<String> keys = new ArrayList<String>();
        Gson gson = new Gson();
        try {
            settings = context.getSharedPreferences(FAVORITES_KEYS,
                    Context.MODE_PRIVATE);
            if (settings.contains(FAVORITES_KEYS)) {
                String jsonFavorites = settings.getString(FAVORITES_KEYS, null);
                String[] keysJson = gson.fromJson(jsonFavorites, String[].class);
                List<String> keysAsList = Arrays.asList(keysJson);
                keys = new ArrayList<>(keysAsList);
                for (String s: keys) {
                    Log.v("Fav Key Array Contents", s);
                }
                keys.add(favoritesName);
                editor = settings.edit();
                String putBackToFavs = gson.toJson(keys);
                editor.putString(FAVORITES_KEYS, putBackToFavs);
                editor.commit();
            }
            else {
                editor = settings.edit();
                keys.add(favoritesName);
                String putBackToFavs = gson.toJson(keys);
                editor.putString(FAVORITES_KEYS, putBackToFavs);
                editor.commit();
            }
        } catch (Exception e) { Log.v("SharedPref", "failed to save fav keys");}
    }

    public void  removeFavorite(Context context, String favoritesName, int poiIndex) {
        ArrayList<PointOfInterest> favorites = getFavorites(context, favoritesName);
        if (favorites != null) {
            favorites.remove(poiIndex);
            saveFavorites(context, favoritesName, favorites);
        }
    }

    public ArrayList<PointOfInterest> getFavorites(Context context, String favoritesName) {
        SharedPreferences settings;
        List<PointOfInterest> favorites;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(favoritesName)) {
            String jsonFavorites = settings.getString(favoritesName, null);
            Gson gson = new Gson();
            PointOfInterest[] favoritePOIs = gson.fromJson(jsonFavorites, PointOfInterest[].class);
            favorites = Arrays.asList(favoritePOIs);
            favorites = new ArrayList<PointOfInterest>(favorites);
        }
        else {
            return null;
        }

        return (ArrayList<PointOfInterest>) favorites;
    }

    public ArrayList<String> getFavoritesKeys(Context context) {
        SharedPreferences settings;
        List<String> keys;

        settings = context.getSharedPreferences(FAVORITES_KEYS, Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES_KEYS)) {
            String jsonFavoritesKeys = settings.getString(FAVORITES_KEYS, null);
            Gson gson = new Gson();
            return gson.fromJson(jsonFavoritesKeys, ArrayList.class);
        }
        return null;
    }
}
