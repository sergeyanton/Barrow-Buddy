package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity class reflecting weather data for a single day.
 * Fields include the garden this weather is for, the type of weather, the temperature,
 * and the humidity for a given date.
 * A garden's profile page will be able to view the information of multiple of these entities,
 * for the current & future weather.
 */
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
    private final int EXPIRY_INTERVAL = 60;

  /**
   * JPA required no-args constructor
   */
  public Weather() {}

    /**
     * Creates a new Weather object
     * @param gardenId    ID of the garden where this Weather is relevant
     * @param date        date that this Weather is relevant for
     * @param type        type of Weather, e.g. CLEAR, OVERCAST, RAIN
     * @param temperature the temperature for the relevant day
     * @param humidity    the humidity for the relevant day
     */
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

  /**
   * Sets the Weather entity to the given new Weather entity.
   * This method is useful for when you want to update a Weather entity,
   * and specifically not create a new one.
   * @param newWeather the Weather object that you want to replace this Weather entity with
   */
  public void setTo(Weather newWeather) {
        this.date = newWeather.getDate();
        this.type = newWeather.getType();
        this.temperature = newWeather.getTemperature();
        this.humidity = newWeather.getHumidity();
        updateExpiry();
    }

  /**
   * Resets the time of expiry to a time in the future, EXPIRY_INTERVAL minutes from now
   */
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
