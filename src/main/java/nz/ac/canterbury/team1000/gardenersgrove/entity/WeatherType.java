package nz.ac.canterbury.team1000.gardenersgrove.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

/**
 * A simple enum for the type of weather, also associates a textual description and an image path
 * with each weather type.
 */
public enum WeatherType {
	DRIZZLE(new int[]{20, 25, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59 }, "Drizzle", "/images/weather/rain.png"),
	SNOW(new int[]{22, 26, 36, 37, 38, 39, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 93, 94}, "Snow", "/images/weather/snowflake.png"),
	THUNDERSTORM(new int[]{ 13, 17, 95, 96, 97, 98, 99 }, "Thunderstorm", "/images/weather/thunderstorm.png"),
	DUST_STORM(new int[]{ 30, 31, 32, 33, 34, 35 }, "Dust Storm", "/images/weather/sand-storm.png"),
	FOG(new int[]{ 10, 11, 12, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49}, "Fog", "/images/weather/fog.png"),
	CLOUDY(new int[]{ 1, 19, 3 }, "Cloud", "/images/weather/clouds.png"),
	HAZE(new int[]{ 5 }, "Haze", "/images/weather/haze.png"),
	CLEAR(new int[]{ 0 }, "Clear", "/images/weather/sunny.png"),
	HAIL(new int[]{ 27, 87, 88, 89, 90 }, "Hail", "/images/weather/hail.png"),
	RAIN_SHOWER(new int[]{ 14, 15, 16, 18, 21, 23, 24, 25, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 80, 81, 82, 83, 84, 85, 86, 91, 92}, "Rain", "/images/weather/shower.png"),
	// 2: State of sky on the whole unchanged - On the getCurrentWeatherByGardenId, the weather type will be set to the previous hour's weather
	SAME(new int[]{ 2 }, "Same Weather", ""),
	// 4: Visibility reduced by smoke, e.g. veldt or forest fires, industrial smoke or volcanic ashes
	FIRE(new int[]{ 4 }, "Smoke", "/images/weather/smoke.png");

	/**
	 * The Open-Meteo codes that correlate to the given WeatherType.
	 * For example, a code of 0 correlates to clear weather and the CLEAR WeatherType,
	 * and the codes 45 and 48 correlate to different types of fog, but the distinction is not
	 * needed for our purposes so the FOG WeatherType correlates to both codes.s
	 */
	private final int[] codes;
	/**
	 * A description of the weather
	 */
	private final String text;
	/**
	 * A URL to an image representing the weather
	 */
	private final String picturePath;

	/**
	 * Constructs the WeatherType.
	 *
	 * @param codes The Open-Meteo codes that correlate to the given WeatherType
	 * @param text A description of the weather
	 * @param picturePath A URL to an image representing the weather
	 */
	WeatherType(int[] codes, String text, String picturePath) {
		this.codes = codes;
		this.text = text;
		this.picturePath = picturePath;
	}
	public String getText() { return text; }
	public String getPicturePath() {
		return picturePath;
	}

	/**
	 * Given an Open-Meteo weather code, this method returns the correct WeatherType.
	 * For example, code 2 means PARTLY_CLOUDY
	 * @param code The Open-Meteo weather code
	 * @return The WeatherType represented by the weather code
	 */
	public static WeatherType getByCode(int code) {
		// Stephen Hockey (sho151) ChatGPT attribution: helping me come up with this stream manipulation
		Optional<WeatherType> optionalType = Arrays.stream(WeatherType.values())
			.filter(type -> Arrays.stream(type.codes).anyMatch(c -> c == code))
			.findFirst();

		return optionalType.orElse(null);
	}
}
