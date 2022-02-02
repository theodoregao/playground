package shun.gao.viewpager.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import shun.gao.viewpager.R

internal class CityItemAdapter(
    private val onItemSelectionChangedListener: OnItemSelectionChangedListener
): RecyclerView.Adapter<CityItemViewHolder>() {

    private var cityItems: List<CityItem> = emptyList()

    fun updateCityItems(cities: List<CityItem>) {
        this.cityItems = cities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemViewHolder {
        val cityItemView = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return CityItemViewHolder(cityItemView, onItemSelectionChangedListener)
    }

    override fun onBindViewHolder(holder: CityItemViewHolder, position: Int) {
        holder.bind(cityItems[position])
    }

    override fun getItemCount() = cityItems.size
}

internal class CityItemViewHolder(
    view: View,
    private val onItemSelectionChangedListener: OnItemSelectionChangedListener,
): RecyclerView.ViewHolder(view) {

    private val city = view.findViewById<CheckBox>(R.id.city)
    private val actualSelected = view.findViewById<TextView>(R.id.actual_selected)

    fun bind(cityItem: CityItem) {
        city.isChecked = cityItem.isSelected
        city.text = cityItem.city.name
        city.setOnCheckedChangeListener { _, isChecked ->
            onItemSelectionChangedListener(CityItem(cityItem.city, isChecked))
        }
        actualSelected.text = if (cityItem.isSelected) "Y" else "N"
    }
}

internal typealias OnItemSelectionChangedListener = (CityItem) -> Unit
