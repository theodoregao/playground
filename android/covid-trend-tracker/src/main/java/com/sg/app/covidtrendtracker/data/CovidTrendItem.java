package com.sg.app.covidtrendtracker.data;

import java.io.Serializable;

public class CovidTrendItem implements Serializable {
  public final String region;
  public final String details;

  public CovidTrendItem(String region, String details) {
    this.region = region;
    this.details = details;
  }

  @Override
  public String toString() {
    return region;
  }
}