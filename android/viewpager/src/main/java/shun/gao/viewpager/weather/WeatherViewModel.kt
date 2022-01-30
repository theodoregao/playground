package shun.gao.viewpager.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import shun.gao.viewpager.models.City
import shun.gao.viewpager.networking.WeatherNetworkManager

internal class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "WeatherViewModel"
    }

    private val _weatherUiModel = MutableLiveData(WeatherUiModel())
    val weatherUiModel: LiveData<WeatherUiModel> = _weatherUiModel

    suspend fun loadWeather(cities: List<City>) {
        Log.d(TAG, "loadWeather() for ${cities.size} cities")
        val context = getApplication<Application>().applicationContext
        _weatherUiModel.postValue(WeatherUiModel(State.LOAD_SUCCEED,
            cities.mapNotNull { city ->
                withContext(Dispatchers.IO) {
                    WeatherNetworkManager.getCityWeather(context, city.id)
                }
            }.toList()))
    }

}