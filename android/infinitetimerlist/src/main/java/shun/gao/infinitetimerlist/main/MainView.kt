package shun.gao.infinitetimerlist.main

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import shun.gao.infinitetimerlist.R

internal interface MainView {
    val view: View
    fun updateUi(mainUiModel: MainUiModel?)
    fun subscribeMainViewEvent(eventSubscriber: MainViewEventSubscriber)
    suspend fun start()
}

internal interface MainViewEventSubscriber {
    fun onMainViewEvent(event: MainViewEvent)
}

internal class MainViewImpl private constructor(
    context: Context
): FrameLayout(context), MainView {

    private val rv : RecyclerView
    private val myAdapter : MyListAdapter
    private val myLayoutManager : LinearLayoutManager
    private lateinit var eventSubscriber: MainViewEventSubscriber

    private var visibleItemRange: IntRange = IntRange.EMPTY

    companion object {
        const val TIMER_DELAY_MS = 1000L / 30L // 30 refreshes per seconds
        fun create(context: Context) = MainViewImpl(context)
    }

    init {
        inflate(context, R.layout.main_fragment, this)
        rv = findViewById(R.id.my_rv)

        with(rv) {
            myAdapter = MyListAdapter()
            adapter = myAdapter
            myLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            layoutManager = myLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (myLayoutManager.findLastCompletelyVisibleItemPosition() == myLayoutManager.itemCount - 1) {
                        eventSubscriber.onMainViewEvent(UserSeeLastItem)
                    }
                }
            })
        }
    }

    override val view: View = this

    override fun updateUi(mainUiModel: MainUiModel?) {
        myAdapter.updateList(mainUiModel?.size, mainUiModel?.timerItems)
    }

    override fun subscribeMainViewEvent(eventSubscriber: MainViewEventSubscriber) {
        this.eventSubscriber = eventSubscriber
    }

    override suspend fun start() {
        while (true) {
            checkVisibleItems()
            updateUi()
            delay(TIMER_DELAY_MS)
        }
    }

    private fun checkVisibleItems() {
        val newRange =
            myLayoutManager.findFirstVisibleItemPosition()..myLayoutManager.findLastVisibleItemPosition()
        if (newRange != visibleItemRange) {
            visibleItemRange = newRange
            eventSubscriber.onMainViewEvent(VisibleItemRangeChanged(visibleItemRange))
        }
    }

    private fun updateUi() = updateUi(null)
}
