package com.sg.app.covidtrendtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.sg.app.covidtrendtracker.data.CovidTrendItem;

import java.util.List;

import static com.sg.app.covidtrendtracker.constants.Constants.COVID_TREND_ITEM;

public class CovidTrendActivity extends AppCompatActivity {
  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_covid_trend);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    if (findViewById(R.id.item_detail_container) != null) {
      mTwoPane = true;
    }

    View recyclerView = findViewById(R.id.item_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    final Context context = getApplicationContext();
    recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(
        this,
        List.of(new CovidTrendItem(context.getString(R.string.region_united_state), context.getString(R.string.hint_dev_in_progress)),
            new CovidTrendItem(context.getString(R.string.region_california), context.getString(R.string.hint_dev_in_progress)),
            new CovidTrendItem(context.getString(R.string.region_alameda_county), context.getString(R.string.hint_dev_in_progress))),
        mTwoPane));
  }

  public static class SimpleItemRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final CovidTrendActivity mParentActivity;
    private final List<CovidTrendItem> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final CovidTrendItem covidTrendItem = (CovidTrendItem) view.getTag();
        if (mTwoPane) {
          Bundle arguments = new Bundle();
          arguments.putSerializable(COVID_TREND_ITEM, covidTrendItem);
          CovidTrendDetailFragment fragment = new CovidTrendDetailFragment();
          fragment.setArguments(arguments);
          mParentActivity.getSupportFragmentManager().beginTransaction()
              .replace(R.id.item_detail_container, fragment)
              .commit();
        } else {
          Context context = view.getContext();
          Intent intent = new Intent(context, CovidTrendDetailActivity.class);
          intent.putExtra(COVID_TREND_ITEM, covidTrendItem);

          context.startActivity(intent);
        }
      }
    };

    SimpleItemRecyclerViewAdapter(
        CovidTrendActivity parent,
        List<CovidTrendItem> items,
        boolean twoPane) {
      mValues = items;
      mParentActivity = parent;
      mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.covid_trend_list_item, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.region.setText(mValues.get(position).region);
      holder.itemView.setTag(mValues.get(position));
      holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
      return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      final TextView region;

      ViewHolder(View view) {
        super(view);
        region = view.findViewById(R.id.region);
      }
    }
  }
}