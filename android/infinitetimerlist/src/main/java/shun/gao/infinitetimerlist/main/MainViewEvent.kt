package shun.gao.infinitetimerlist.main

internal sealed class MainViewEvent

internal object UserSeeLastItem : MainViewEvent()

internal data class VisibleItemRangeChanged(val range: IntRange) : MainViewEvent()
