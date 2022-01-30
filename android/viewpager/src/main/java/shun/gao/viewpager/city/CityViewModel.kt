package shun.gao.viewpager.city

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import shun.gao.viewpager.models.City
import shun.gao.viewpager.networking.WeatherNetworkManager

internal class CityViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _cityUiModel = MutableLiveData(CityUiModel())
    val cityUiModel: LiveData<CityUiModel> = _cityUiModel

    private val _selectedCities = mutableSetOf<City>()
    val selectedCities: Set<City>
        get() = _selectedCities

    suspend fun loadCities() {
        Log.d(TAG, "loadCities()")
        val context = getApplication<Application>().applicationContext
        withContext(Dispatchers.IO) {
            WeatherNetworkManager.getCities(context)?.let { cities ->
                _cityUiModel.postValue(CityUiModel(State.LOAD_SUCCEED, cities.map { it.toCityItem() }))
            } ?: _cityUiModel.postValue(CityUiModel(State.LOAD_ERROR))
        }
    }

    fun onSelectedCityItemChanged(cityItem: CityItem) {
        Log.d(TAG, "onSelectedCityItemChanged() ${cityItem.city.name} - ${cityItem.isSelected}")
        if (cityItem.isSelected) _selectedCities.add(cityItem.city) else _selectedCities.remove(cityItem.city)
    }

}