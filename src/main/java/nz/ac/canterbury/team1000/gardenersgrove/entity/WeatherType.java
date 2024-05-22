package nz.ac.canterbury.team1000.gardenersgrove.entity;

import java.util.Arrays;
import java.util.Optional;

/**
 * A simple enum for the type of weather, also associates a textual description and an image path
 * with each weather type.
 */
public enum WeatherType {
	NO_RAIN(new int[]{0, 1, 2, 3, 45, 48, 71, 73, 75, 77, 85, 86, 96, 99}, "It did not rain", ""),
	DRIZZLE(new int[]{20, 25, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59 }, "It drizzled", ""),
	SNOW(new int[]{22, 26, 36, 37, 38, 39, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 93, 94}, "It snowed", ""),
	THUNDERSTORM(new int[]{ 13, 17, 95, 96, 97, 98, 99 }, "It thurnderstormed", ""),
	DUST_SAND_STORM(new int[]{ 30, 31, 32, 33, 34, 35 }, "It thurnderstormed", ""),
	FOG(new int[]{ 10, 11, 12, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49}, "It fogged", ""),
	CLOUDY(new int[]{ 1, 19, 3 }, "It fogged", ""),
	HAZE(new int[]{ 5 }, "Haze", ""),
	CLEAR(new int[]{ 0 }, "Haze", ""),
	HAIL(new int[]{ 27, 87, 88, 89, 90 }, "It hailed", ""),
	RAIN_SHOWER(new int[]{ 14, 15, 16, 18, 21, 23, 24, 25, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 80, 81, 82, 83, 84, 85, 86, 91, 92}, "It showered", ""),
	// 2: State of sky on the whole unchanged, 4: Visibility reduced by smoke, e.g. veldt or forest fires, industrial smoke or volcanic ashes
	EXTRAS(new int[]{ 2, 4 }, "Extras", "");

	// TODO MORE ENUMS

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
	private final String imagePath;

	/**
	 * Constructs the WeatherType.
	 *
	 * @param codes The Open-Meteo codes that correlate to the given WeatherType
	 * @param text A description of the weather
	 * @param imagePath A URL to an image representing the weather
	 */
	WeatherType(int[] codes, String text, String imagePath) {
		this.codes = codes;
		this.text = text;
		this.imagePath = imagePath;
	}
	public String getText() { return text; }
	public String getImagePath() {
		return imagePath;
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
