package shun.gao.viewpager.city

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import shun.gao.viewpager.R

internal interface CityView {
    val view: View
    fun loading()
    fun cityItemsLoaded(cities: List<CityItem>)
    fun subscribeCityViewEvent(eventHandler: CityViewEventHandler)
}

internal typealias CityViewEventHandler = (CityViewEvent) -> Unit

internal class CityViewImpl private constructor(context: Context): FrameLayout(context), CityView {

    companion object {
        private const val TAG = "CityViewImpl"
        fun create(context: Context) = CityViewImpl(context)
    }

    private val loadingProgress: ProgressBar
    private val cityItems: RecyclerView
    private val okButton: Button
    private val cityItemAdapter: CityItemAdapter

    private lateinit var eventHandler: CityViewEventHandler

    init {
        inflate(context, R.layout.city_fragment, this)
        loadingProgress = findViewById(R.id.loading_progress)
        cityItems = findViewById(R.id.city_list)
        okButton = findViewById(R.id.action_ok)
        with(cityItems) {
            cityItemAdapter = CityItemAdapter {
                eventHandler(CityViewSelectionUpdatedEvent(it))
            }
            adapter = cityItemAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override val view: View
        get() = this

    override fun loading() {
        Log.d(TAG, "loading()")
        loadingProgress.isVisible = true
        okButton.isVisible = false
    }

    override fun cityItemsLoaded(cityItems: List<CityItem>) {
        Log.d(TAG, "cityDataLoaded() with size ${cityItems.size}")
        loadingProgress.isVisible = false
        cityItemAdapter.updateCityItems(cityItems)
        okButton.isVisible = true
        okButton.setOnClickListener { eventHandler(ActionOkEvent) }
    }

    override fun subscribeCityViewEvent(eventHandler: CityViewEventHandler) {
        this.eventHandler = eventHandler
    }

}
