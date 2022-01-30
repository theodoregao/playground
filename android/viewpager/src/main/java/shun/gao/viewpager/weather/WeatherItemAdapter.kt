package shun.gao.viewpager.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import shun.gao.viewpager.R
import shun.gao.viewpager.models.CityWeather

internal class WeatherItemAdapter: RecyclerView.Adapter<WeatherItemViewHolder>() {

    private var weatherItems: List<CityWeather> = emptyList()

    fun updateWeatherItems(weathers: List<CityWeather>) {
        this.weatherItems = weathers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        val weatherItemView = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherItemViewHolder(weatherItemView)
    }

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.bind(weatherItems[position])
    }

    override fun getItemCount() = weatherItems.size
}

internal class WeatherItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val cityName = view.findViewById<TextView>(R.id.city_name)
    private val cityWeather = view.findViewById<TextView>(R.id.city_weather)
    private val cityTemperature = view.findViewById<TextView>(R.id.city_temperature)

    fun bind(weather: CityWeather) {
        cityName.text = "City: ${weather.name}"
        cityWeather.text = "Weather: ${weather.weather}"
        cityTemperature.text = "Temperature: ${weather.temperature}"
    }
}