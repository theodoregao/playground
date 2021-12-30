package shun.gao.infinitetimerlist.main

internal data class MainUiModel(
    val size: Int,
    val timerItems: Map<Int, TimerItem?>
)
