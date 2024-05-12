package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import nz.ac.canterbury.team1000.gardenersgrove.api.WeatherType;

@Entity
@Table(name = "weather_data")
public class WeatherData {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private Long gardenId;
  @Column
  public String text;
  @Column
  public String imagePath;
  @Column(nullable = false)
  public LocalDateTime expiry;
  // TODO OTHER THINGS LIKE TEMPERATURE
  // TODO proper schema design with many to one or something

  public WeatherData() {}

  public WeatherData(Long gardenId, WeatherType type) {
    this.gardenId = gardenId;
    this.text = type.getText();
    this.imagePath = type.getImagePath();
    this.expiry = LocalDateTime.now().plusMinutes(60); //TODO CONSTANT for expiry time
  }
}
