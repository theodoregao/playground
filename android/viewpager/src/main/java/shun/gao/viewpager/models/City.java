package shun.gao.viewpager.models;

import androidx.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

public class City {

    public static final String TAG = City.class.getSimpleName();

    public final int id;
    public final String name;

    public City(@NonNull JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.name = jsonObject.getString("name");
        } catch (JSONException e) {
            throw new IllegalArgumentException("Unable to parse json into city: " + jsonObject.toString(), e);
        }
    }
}
