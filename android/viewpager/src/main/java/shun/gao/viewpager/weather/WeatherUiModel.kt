package shun.gao.viewpager.weather

import shun.gao.viewpager.models.CityWeather

internal enum class State {
    LOADING,
    LOAD_SUCCEED,
    LOAD_ERROR
}

internal data class WeatherUiModel(
    val state: State = State.LOADING,
    val weathers: List<CityWeather> = emptyList()
)