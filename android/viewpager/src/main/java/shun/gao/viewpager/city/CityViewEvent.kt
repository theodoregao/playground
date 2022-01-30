package shun.gao.viewpager.city

internal sealed class CityViewEvent

internal data class CityViewSelectionUpdatedEvent(val cityItem: CityItem) : CityViewEvent()

internal object ActionOkEvent : CityViewEvent()
