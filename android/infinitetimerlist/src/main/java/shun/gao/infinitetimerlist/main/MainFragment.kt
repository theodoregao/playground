package shun.gao.infinitetimerlist.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mainView: MainView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = MainViewImpl.create(requireContext())
        return mainView.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.mainUiModel.observeForever { mainView.updateUi(it) }

        mainView.subscribeMainViewEvent(object : MainViewEventSubscriber {
            override fun onMainViewEvent(event: MainViewEvent) {
                when(event) {
                    is UserSeeLastItem -> viewModel.addTimers()
                    is VisibleItemRangeChanged -> viewModel.updateVisibleItemRangeChanged(event.range)
                }
            }
        })

        MainScope().launch { mainView.start() }
    }

}
