package shun.gao.infinitetimerlist.main

import androidx.lifecycle.*

internal class MainViewModel : ViewModel() {

    private val timerItemsMap = mutableMapOf(0 to TimerItem(0))
    private val _size = MutableLiveData(1)
    private val _visibleItemRange = MutableLiveData(IntRange.EMPTY)
    private val _mainUiModel = MediatorLiveData<MainUiModel>()

    val mainUiModel: LiveData<MainUiModel> = _mainUiModel

    init {
        _mainUiModel.addSource(_size) {
            _mainUiModel.value = generateMainUiModel()
        }
        _mainUiModel.addSource(_visibleItemRange) {
            _mainUiModel.value = generateMainUiModel()
        }
    }

    fun addTimers() {
        _size.postValue((_size.value ?: 1) * 2)
    }

    fun updateVisibleItemRangeChanged(range: IntRange) {
        range.forEach {
            if (!timerItemsMap.containsKey(it)) {
                timerItemsMap[it] = TimerItem(it)
            }
        }
        _visibleItemRange.postValue(range)
    }

    private fun generateMainUiModel() =
        MainUiModel(
            _size.value ?: 0,
            _visibleItemRange.value?.mapNotNull {
                it to timerItemsMap[it]
            }?.toMap() ?: mapOf()
        )
}
