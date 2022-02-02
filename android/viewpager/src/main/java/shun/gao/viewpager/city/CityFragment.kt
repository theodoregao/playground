package shun.gao.viewpager.city

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import shun.gao.viewpager.R
import shun.gao.viewpager.weather.WeatherFragment

class CityFragment : Fragment() {

    companion object {
        fun newInstance() = CityFragment()
    }

    private lateinit var cityViewModel: CityViewModel
    private lateinit var cityView: CityView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cityView = CityViewImpl.create(requireContext())
        return cityView.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        cityViewModel.cityUiModel.observeForever {
            when (it.state) {
                State.LOADING -> cityView.loading()
                State.LOAD_SUCCEED -> cityView.cityItemsLoaded(it.cities)
            }
        }

        cityView.subscribeCityViewEvent {
            when (it) {
                is CityViewSelectionUpdatedEvent -> cityViewModel.onSelectedCityItemChanged(it.cityItem)
                ActionOkEvent -> activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, WeatherFragment.newInstance(cityViewModel.selectedCities.toList()))
                    ?.commitNow()
            }
        }

        MainScope().launch {
            cityViewModel.loadCities()
            cityViewModel.start()
        }
    }

}