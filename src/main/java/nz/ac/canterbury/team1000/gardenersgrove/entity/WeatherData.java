package nz.ac.canterbury.team1000.gardenersgrove.entity;

import nz.ac.canterbury.team1000.gardenersgrove.api.WeatherType;

public class WeatherData {
  public String text;
  public String imagePath;
  // TODO OTHER THINGS LIKE TEMPERATURE
  public WeatherData(WeatherType type) {
    this.text = type.getText();
    this.imagePath = type.getImagePath();
  }
}
