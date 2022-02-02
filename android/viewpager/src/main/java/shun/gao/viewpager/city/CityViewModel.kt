package shun.gao.viewpager.city

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import shun.gao.viewpager.models.City
import shun.gao.viewpager.networking.WeatherNetworkManager

internal class CityViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _cityUiModel = MutableLiveData(CityUiModel())
    val cityUiModel: LiveData<CityUiModel> = _cityUiModel

    private val _selectedCityItemChangedActions = MutableSharedFlow<CityItem>()
    val selectedCityItemChangedActions: SharedFlow<CityItem> = _selectedCityItemChangedActions

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
        MainScope().launch {
            withContext(Dispatchers.IO) {
                _selectedCityItemChangedActions.emit(cityItem)
            }
        }
    }

    suspend fun start() {
        withContext(Dispatchers.IO) {
            selectedCityItemChangedActions.debounce(5000)
                .collect { updatedCityItem ->
                    if (updatedCityItem.isSelected) _selectedCities.add(updatedCityItem.city) else _selectedCities.remove(updatedCityItem.city)
                    val updatedCityItems = _cityUiModel.value?.cities?.let { cities ->
                        cities.map {
                            CityItem(it.city, _selectedCities.contains(it.city))
                        }
                    }?.toList()
                    updatedCityItems?.let {
                        _cityUiModel.postValue(CityUiModel(State.LOAD_SUCCEED, it))
                    }
                }
        }
    }

}