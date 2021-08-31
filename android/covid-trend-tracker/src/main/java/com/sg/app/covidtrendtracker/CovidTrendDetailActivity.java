package com.sg.app.covidtrendtracker;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sg.app.covidtrendtracker.data.CovidTrendItem;

import static com.sg.app.covidtrendtracker.constants.Constants.COVID_TREND_ITEM;

public class CovidTrendDetailActivity extends AppCompatActivity {
  private CovidTrendItem covidTrendItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_covid_trend_detail);
    Toolbar toolbar = findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);

    covidTrendItem = (CovidTrendItem) (getIntent().getSerializableExtra(COVID_TREND_ITEM));
    setTitle(covidTrendItem.region);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }
}