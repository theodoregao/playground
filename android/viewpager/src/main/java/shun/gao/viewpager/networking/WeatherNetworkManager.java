package shun.gao.viewpager.networking;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import shun.gao.viewpager.models.City;
import shun.gao.viewpager.models.CityWeather;

import java.util.ArrayList;
import java.util.List;

public class WeatherNetworkManager extends NetworkManager {

    private static final String TAG = WeatherNetworkManager.class.getSimpleName();

    private static final String CITIES_FILENAME = "weather_cities.json";
    private static final String CITY_FILENAME_TEMPLATE = "weather_city_%s.json";

    @Nullable
    public static List<City> getCities(@NonNull Context context) {
        byte[] data = getJson(context, CITIES_FILENAME);
        if (data.length == 0) {
            return null;
        }

        String jsonString = new String(data);
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray cities = root.getJSONArray("cities");
            ArrayList<City> ret = new ArrayList<>(cities.length());
            for (int i = 0; i < cities.length(); i++) {
                try {
                    ret.add(new City(cities.getJSONObject(i)));
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Unable to create city: " + cities.getJSONObject(i).toString(), e);
                }
            }
            return ret;
        } catch (JSONException e) {
            Log.e(TAG, "Unable to parse JSON: " + jsonString, e);
            return null;
        }
    }

    @Nullable
    public static CityWeather getCityWeather(@NonNull Context context, int cityId) {
        byte[] data = getJson(context, String.format(CITY_FILENAME_TEMPLATE, cityId));
        if (data.length == 0) {
            return null;
        }

        String jsonString = new String(data);
        try {
            return new CityWeather(new JSONObject(jsonString));
        } catch (JSONException e) {
            Log.e(TAG, "Unable to parse JSON: " + jsonString, e);
            return null;
        }
    }

}
