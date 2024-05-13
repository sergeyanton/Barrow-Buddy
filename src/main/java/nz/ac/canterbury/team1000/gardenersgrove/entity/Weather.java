package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Weather {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private Long gardenId;
  @Column(nullable = false)
  public LocalDate date;
  @Column(nullable = false)
  public WeatherType type;
  @Column(nullable = false)
  public Double temperature;
  @Column(nullable = false)
  public Double humidity;
  @Column(nullable = false)
  public LocalDateTime expiry;

  /**
   * The period of time in minutes that the weather is valid for, after this time, the weather
   * should be updated via Open-Meteo.
   */
  private final int EXPIRY_INTERVAL = 1;

  public Weather() {}

  public Weather(Long gardenId, LocalDate date, WeatherType type, Double temperature, Double humidity) {
    this.gardenId = gardenId;
    this.date = date;
    this.type = type;
    this.temperature = temperature;
    this.humidity = humidity;
    updateExpiry();
  }

  public LocalDate getDate() {
    return date;
  }

  public WeatherType getType() {
    return type;
  }

  public Double getTemperature() {
    return temperature;
  }

  public Double getHumidity() {
    return humidity;
  }

  public LocalDateTime getExpiry() {
    return expiry;
  }

  public void setTo(Weather newWeather) {
    this.date = newWeather.getDate();
    this.type = newWeather.getType();
    this.temperature = newWeather.getTemperature();
    this.humidity = newWeather.getHumidity();
    updateExpiry();
  }

  public void updateExpiry() {
    this.expiry = LocalDateTime.now().plusMinutes(EXPIRY_INTERVAL);
  }

  @Override
  public String toString() {
    return "Weather{" +
        "id=" + id +
        ", gardenId=" + gardenId +
        ", date=" + date +
        ", type=" + type +
        ", temperature=" + temperature +
        ", humidity=" + humidity +
        ", expiry=" + expiry +
        '}';
  }
}
