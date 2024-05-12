package nz.ac.canterbury.team1000.gardenersgrove.entity;

import nz.ac.canterbury.team1000.gardenersgrove.api.WeatherType;

public class WeatherData {
  public WeatherType type;

  public WeatherData(WeatherType type) {
    this.type = type;
  }

  public WeatherType getType() {
    return type;
  }

  public void setType(WeatherType type) {
    this.type = type;
  }
}
