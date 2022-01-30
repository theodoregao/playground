package shun.gao.viewpager.weather

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import shun.gao.viewpager.R
import shun.gao.viewpager.models.CityWeather

internal interface WeatherView {
    val view: View
    fun loading()
    fun weatherLoaded(weathers: List<CityWeather>)
}

internal class WeatherViewImpl private constructor(context: Context): FrameLayout(context), WeatherView {
    companion object {
        private val TAG = "WeatherViewImpl"
        fun create(context: Context) = WeatherViewImpl(context)
    }

    private val loadingProgress: ProgressBar
    private val weatherPager: ViewPager2
    private val weatherItemAdapter: WeatherItemAdapter

    init {
        inflate(context, R.layout.weather_fragment, this)
        loadingProgress = findViewById(R.id.loading_progress)
        weatherPager = findViewById(R.id.weather_pager)
        with(weatherPager) {
            weatherItemAdapter = WeatherItemAdapter()
            adapter = weatherItemAdapter
        }
    }

    override val view: View
        get() = this

    override fun loading() {
        loadingProgress.isVisible = true
    }

    override fun weatherLoaded(weathers: List<CityWeather>) {
        loadingProgress.isVisible = false
        weatherItemAdapter.updateWeatherItems(weathers)
    }
}
