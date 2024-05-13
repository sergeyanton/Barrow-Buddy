package nz.ac.canterbury.team1000.gardenersgrove.entity;

import java.util.Arrays;
import java.util.Optional;

public enum WeatherType {
	//TODO MORE BETTER ENUMS
	NO_RAIN(new int[]{0, 1, 2, 3, 45, 48, 71, 73, 75, 77, 85, 86, 96, 99}, "It did not rain", ""),
	RAIN(new int[]{51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82, 95}, "It rained", "");
	private final int[] codes;
	private final String text;
	private final String imagePath;

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

	public static WeatherType getByCode(int code) {
		Optional<WeatherType> optionalType = Arrays.stream(WeatherType.values())
			.filter(type -> Arrays.stream(type.codes).anyMatch(c -> c == code))
			.findFirst();

		return optionalType.orElse(null);
	}
}
