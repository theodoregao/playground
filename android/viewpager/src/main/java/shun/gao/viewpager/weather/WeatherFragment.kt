package shun.gao.viewpager.weather

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import shun.gao.viewpager.models.City

class WeatherFragment(private val cities: List<City>) : Fragment() {

    companion object {
        fun newInstance(cities: List<City>) = WeatherFragment(cities)
    }

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherView: WeatherView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherView = WeatherViewImpl.create(requireContext())
        return weatherView.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherViewModel.weatherUiModel.observeForever {
            when (it.state) {
                State.LOADING -> weatherView.loading()
                State.LOAD_SUCCEED -> weatherView.weatherLoaded(it.weathers)
            }
        }

        MainScope().launch { weatherViewModel.loadWeather(cities) }
    }

}