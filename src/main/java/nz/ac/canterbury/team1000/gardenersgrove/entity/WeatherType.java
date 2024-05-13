package nz.ac.canterbury.team1000.gardenersgrove.entity;

import java.util.Arrays;
import java.util.Optional;

/**
 * A simple enum for the type of weather, also associates a textual description and an image path
 * with each weather type.
 */
public enum WeatherType {
	NO_RAIN(new int[]{0, 1, 2, 3, 45, 48, 71, 73, 75, 77, 85, 86, 96, 99}, "It did not rain", ""),
	RAIN(new int[]{51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82, 95}, "It rained", "");

	//TODO MORE ENUMS

	/**
	 * The Open-Meteo codes that correlate to the given WeatherType
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

	public int[] getCodes() {
		return codes;
	}

	public String getText() {
		return text;
	}

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
		Optional<WeatherType> optionalType = Arrays.stream(WeatherType.values())
			.filter(type -> Arrays.stream(type.codes).anyMatch(c -> c == code))
			.findFirst();

		return optionalType.orElse(null);
	}
}
