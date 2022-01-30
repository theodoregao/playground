package shun.gao.viewpager.models;

import androidx.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

public class CityWeather {

    public static final String TAG = CityWeather.class.getSimpleName();

    public final int id;
    public final String name;
    public final String weather;
    public final int temperature;

    public CityWeather(@NonNull JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.name = jsonObject.getString("cityName");
            this.weather = jsonObject.getString("weather");
            this.temperature = jsonObject.getInt("temperature");
        } catch (JSONException e) {
            throw new IllegalArgumentException("Unable to parse json into city weather: " + jsonObject.toString(), e);
        }
    }
}
