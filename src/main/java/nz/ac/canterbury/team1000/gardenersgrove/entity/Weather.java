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
    @Column
    public LocalDateTime dateTime;
    @Column(nullable = false)
    public String dayOfTheWeek;
    @Column(nullable = false)
    public WeatherType type;
    @Column
    public Double temperature;
    @Column
    public Integer humidity;
    @Column(nullable = false)
    public LocalDateTime expiry;
    @Column
    public Integer precipitation;
    @Column
    public Double minTemperature;
    @Column
    public Double maxTemperature;

    @Column
    public String sunSet;

    @Column
    public String sunRise;

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
     * @param dateTime        date and time that this Weather is relevant for
     * @param type        type of Weather, e.g. CLEAR, OVERCAST, RAIN
     * @param temperature the temperature for the relevant day
     * @param humidity    the humidity for the relevant day
     */
    public Weather(Long gardenId, LocalDateTime dateTime, WeatherType type, Double temperature,
            Integer humidity, String dayOfTheWeek)
    {
        this.gardenId = gardenId;
        this.dateTime = dateTime;
        this.dayOfTheWeek = dayOfTheWeek;
        this.type = type;
        this.temperature = temperature;
        this.humidity = humidity;
        updateExpiry();
    }

    public Weather(Long gardenId, WeatherType type, Double minTemperature, Double maxTemperature,
            Integer precipitation, String dayOfTheWeek, String sunSet, String sunRise)
    {
        this.gardenId = gardenId;
        this.type = type;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.precipitation = precipitation;
        this.dayOfTheWeek = dayOfTheWeek;
        this.sunSet = sunSet;
        this.sunRise = sunRise;
        updateExpiry();
    }

  public LocalDateTime getDateTime() {
      return dateTime;
    }

    public WeatherType getType() {
      return type;
    }

    public void setType(WeatherType weatherType) {
        this.type = weatherType;
    }

    public Double getTemperature() {
      return temperature;
    }

    public Integer getHumidity() {
      return humidity;
    }

    /**
     * Returns the day of the week with the first letter capitalized and the rest in lowercase.
     *
     * @return a string representing the day of the week with proper capitalization
     */
    public String getDayOfTheWeek() {
        return dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1).toLowerCase();
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
        this.dateTime = newWeather.getDateTime();
        this.type = newWeather.getType();
        this.temperature = newWeather.getTemperature();
        this.dayOfTheWeek = newWeather.getDayOfTheWeek();
        this.humidity = newWeather.getHumidity();
        updateExpiry();
    }

  /**
   * Resets the time of expiry to a time in the future, EXPIRY_INTERVAL minutes from now
   */
  public void updateExpiry() {
      this.expiry = LocalDateTime.now().plusMinutes(EXPIRY_INTERVAL);
    }

//    @Override
//    public String toString() {
//        return "Weather{" +
//            "id=" + id +
//            ", gardenId=" + gardenId +
//            ", type=" + type +
//            ", dayOfTheWeek=" + dayOfTheWeek +
//            ", expiry=" + expiry +
//            ", precipitation=" + precipitation +
//            ", minTemperature=" + minTemperature +
//            ", maxTemperature=" + maxTemperature +
//            '}';
//    }
}
