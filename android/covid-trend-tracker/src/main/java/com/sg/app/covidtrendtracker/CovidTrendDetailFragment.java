package com.sg.app.covidtrendtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.sg.app.covidtrendtracker.data.CovidTrendItem;

import static com.sg.app.covidtrendtracker.constants.Constants.COVID_TREND_ITEM;

public class CovidTrendDetailFragment extends Fragment {
  private CovidTrendItem covidTrendItem;

  public CovidTrendDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(COVID_TREND_ITEM)) {
      covidTrendItem = (CovidTrendItem) (getArguments().getSerializable(COVID_TREND_ITEM));

      final Activity activity = this.getActivity();
      final CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
      if (appBarLayout != null) {
        appBarLayout.setTitle(covidTrendItem.region);
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.covid_trend_detail, container, false);

    if (covidTrendItem != null) {
      ((TextView) rootView.findViewById(R.id.item_detail)).setText(covidTrendItem.details);
    }

    return rootView;
  }
}