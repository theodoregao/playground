package shun.gao.viewpager.city

import shun.gao.viewpager.models.City

internal enum class State {
    LOADING,
    LOAD_SUCCEED,
    LOAD_ERROR
}

internal data class CityItem(val city: City, val isSelected: Boolean)

internal fun City.toCityItem() = CityItem(this, false)

internal data class CityUiModel(
    val state: State = State.LOADING,
    val cities: List<CityItem> = emptyList(),
)