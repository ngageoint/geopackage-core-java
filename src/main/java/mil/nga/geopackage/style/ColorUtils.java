package mil.nga.geopackage.style;

import java.util.regex.Pattern;

import mil.nga.geopackage.GeoPackageException;

/**
 * Color utilities with support for hex, RBG, arithmetic RBG, HSL, and integer
 * colors
 * 
 * @author osbornb
 * @since 3.2.0
 */
public class ColorUtils {

	/**
	 * Hex color pattern
	 */
	private static final Pattern hexColorPattern = Pattern
			.compile("^#?((\\p{XDigit}{3}){1,2}|(\\p{XDigit}{4}){1,2})$");

	/**
	 * Hex single color pattern
	 */
	private static final Pattern hexSingleColorPattern = Pattern
			.compile("^\\p{XDigit}{1,2}$");

	/**
	 * Convert the hex color values to a hex color
	 * 
	 * @param red
	 *            red hex color in format RR or R
	 * @param green
	 *            green hex color in format GG or G
	 * @param blue
	 *            blue hex color in format BB or B
	 * 
	 * @return hex color in format #RRGGBB
	 */
	public static String toColor(String red, String green, String blue) {
		return toColorWithAlpha(red, green, blue, null);
	}

	/**
	 * Convert the hex color values to a hex color, shorthanded when possible
	 * 
	 * @param red
	 *            red hex color in format RR or R
	 * @param green
	 *            green hex color in format GG or G
	 * @param blue
	 *            blue hex color in format BB or B
	 * 
	 * @return hex color in format #RGB or #RRGGBB
	 */
	public static String toColorShorthand(String red, String green,
			String blue) {
		return shorthandHex(toColor(red, green, blue));
	}

	/**
	 * Convert the hex color values to a hex color including an opaque alpha
	 * value of FF
	 * 
	 * @param red
	 *            red hex color in format RR or R
	 * @param green
	 *            green hex color in format GG or G
	 * @param blue
	 *            blue hex color in format BB or B
	 * 
	 * @return hex color in format #AARRGGBB
	 */
	public static String toColorWithAlpha(String red, String green,
			String blue) {
		String defaultAlpha = "FF";
		if (red != null && !red.isEmpty()
				&& Character.isLowerCase(red.charAt(0))) {
			defaultAlpha = defaultAlpha.toLowerCase();
		}
		return toColorWithAlpha(red, green, blue, defaultAlpha);
	}

	/**
	 * Convert the hex color values to a hex color including an opaque alpha
	 * value of FF or F, shorthanded when possible
	 * 
	 * @param red
	 *            red hex color in format RR or R
	 * @param green
	 *            green hex color in format GG or G
	 * @param blue
	 *            blue hex color in format BB or B
	 * 
	 * @return hex color in format #ARGB or #AARRGGBB
	 */
	public static String toColorShorthandWithAlpha(String red, String green,
			String blue) {
		return shorthandHex(toColorWithAlpha(red, green, blue));
	}

	/**
	 * Convert the hex color values to a hex color
	 * 
	 * @param red
	 *            red hex color in format RR or R
	 * @param green
	 *            green hex color in format GG or G
	 * @param blue
	 *            blue hex color in format BB or B
	 * @param alpha
	 *            alpha hex color in format AA or A, null to not include alpha
	 * 
	 * @return hex color in format #AARRGGBB or #RRGGBB
	 */
	public static String toColorWithAlpha(String red, String green, String blue,
			String alpha) {
		validateHexSingle(red);
		validateHexSingle(green);
		validateHexSingle(blue);
		StringBuilder color = new StringBuilder("#");
		if (alpha != null) {
			color.append(expandShorthandHexSingle(alpha));
		}
		color.append(expandShorthandHexSingle(red));
		color.append(expandShorthandHexSingle(green));
		color.append(expandShorthandHexSingle(blue));
		return color.toString();
	}

	/**
	 * Convert the hex color values to a hex color, shorthanded when possible
	 * 
	 * @param red
	 *            red hex color in format RR or R
	 * @param green
	 *            green hex color in format GG or G
	 * @param blue
	 *            blue hex color in format BB or B
	 * @param alpha
	 *            alpha hex color in format AA or A, null to not include alpha
	 * 
	 * @return hex color in format #ARGB, #RGB, #AARRGGBB, or #RRGGBB
	 */
	public static String toColorShorthandWithAlpha(String red, String green,
			String blue, String alpha) {
		return shorthandHex(toColorWithAlpha(red, green, blue, alpha));
	}

	/**
	 * Convert the RBG values to a color integer
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 * 
	 * @return integer color
	 */
	public static int toColor(int red, int green, int blue) {
		return toColorWithAlpha(red, green, blue, -1);
	}

	/**
	 * Convert the RBG values to a color integer including an opaque alpha value
	 * of 255
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 * 
	 * @return integer color
	 */
	public static int toColorWithAlpha(int red, int green, int blue) {
		return toColorWithAlpha(red, green, blue, 255);
	}

	/**
	 * Convert the RBGA values to a color integer
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 * @param alpha
	 *            alpha integer color inclusively between 0 and 255, -1 to not
	 *            include alpha
	 * 
	 * @return integer color
	 */
	public static int toColorWithAlpha(int red, int green, int blue,
			int alpha) {
		validateRGB(red);
		validateRGB(green);
		validateRGB(blue);
		int color = (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
		if (alpha != -1) {
			validateRGB(alpha);
			color = (alpha & 0xff) << 24 | color;
		}
		return color;
	}

	/**
	 * Convert the RGB integer to a hex single color
	 * 
	 * @param color
	 *            integer color inclusively between 0 and 255
	 * @return hex single color in format FF
	 */
	public static String toHex(int color) {
		validateRGB(color);
		String hex = Integer.toHexString(color).toUpperCase();
		if (hex.length() == 1) {
			hex = "0" + hex;
		}
		return hex;
	}

	/**
	 * Convert the arithmetic RGB float to a hex single color
	 * 
	 * @param color
	 *            float color inclusively between 0.0 and 1.0
	 * @return hex single color in format FF
	 */
	public static String toHex(float color) {
		return toHex(toRGB(color));
	}

	/**
	 * Convert the hex single color to a RBG integer
	 * 
	 * @param color
	 *            hex single color in format FF or F
	 * 
	 * @return integer color inclusively between 0 and 255
	 */
	public static int toRGB(String color) {
		validateHexSingle(color);
		if (color.length() == 1) {
			color += color;
		}
		return Integer.parseInt(color, 16);
	}

	/**
	 * Convert the arithmetic RGB float to a RBG integer
	 * 
	 * @param color
	 *            float color inclusively between 0.0 and 1.0
	 * 
	 * @return integer color inclusively between 0 and 255
	 */
	public static int toRGB(float color) {
		validateArithmeticRGB(color);
		return Math.round(255 * color);
	}

	/**
	 * Convert the hex single color to an arithmetic RBG float
	 * 
	 * @param color
	 *            hex single color in format FF or F
	 * @return float color inclusively between 0.0 and 1.0
	 */
	public static float toArithmeticRGB(String color) {
		return toArithmeticRGB(toRGB(color));
	}

	/**
	 * Convert the RGB integer to an arithmetic RBG float
	 * 
	 * @param color
	 *            integer color inclusively between 0 and 255
	 * @return float color inclusively between 0.0 and 1.0
	 */
	public static float toArithmeticRGB(int color) {
		validateRGB(color);
		return color / 255.0f;
	}

	/**
	 * Convert red, green, and blue arithmetic values to HSL (hue, saturation,
	 * lightness) values
	 * 
	 * @param red
	 *            red color inclusively between 0.0 and 1.0
	 * @param green
	 *            green color inclusively between 0.0 and 1.0
	 * @param blue
	 *            blue color inclusively between 0.0 and 1.0
	 * @return HSL array where: 0 = hue, 1 = saturation, 2 = lightness
	 */
	public static float[] toHSL(float red, float green, float blue) {

		validateArithmeticRGB(red);
		validateArithmeticRGB(green);
		validateArithmeticRGB(blue);

		float min = Math.min(Math.min(red, green), blue);
		float max = Math.max(Math.max(red, green), blue);

		float range = max - min;

		float hue = 0.0f;
		if (range > 0.0f) {
			if (red >= green && red >= blue) {
				hue = (green - blue) / range;
			} else if (green >= blue) {
				hue = 2 + (blue - red) / range;
			} else {
				hue = 4 + (red - green) / range;
			}
		}

		hue *= 60.0f;
		if (hue < 0.0f) {
			hue += 360.0f;
		}

		float sum = min + max;

		float lightness = sum / 2.0f;

		float saturation;
		if (min == max) {
			saturation = 0.0f;
		} else {
			if (lightness < 0.5f) {
				saturation = range / sum;
			} else {
				saturation = range / (2.0f - max - min);
			}
		}

		return new float[] { hue, saturation, lightness };
	}

	/**
	 * Convert red, green, and blue integer values to HSL (hue, saturation,
	 * lightness) values
	 * 
	 * @param red
	 *            red color inclusively between 0 and 255
	 * @param green
	 *            green color inclusively between 0 and 255
	 * @param blue
	 *            blue color inclusively between 0 and 255
	 * @return HSL array where: 0 = hue, 1 = saturation, 2 = lightness
	 */
	public static float[] toHSL(int red, int green, int blue) {
		return toHSL(toArithmeticRGB(red), toArithmeticRGB(green),
				toArithmeticRGB(blue));
	}

	/**
	 * Convert HSL (hue, saturation, and lightness) values to RGB arithmetic
	 * values
	 * 
	 * @param hue
	 *            hue value inclusively between 0.0 and 360.0
	 * @param saturation
	 *            saturation inclusively between 0.0 and 1.0
	 * @param lightness
	 *            lightness inclusively between 0.0 and 1.0
	 * @return arithmetic RGB array where: 0 = red, 1 = green, 2 = blue
	 */
	public static float[] toArithmeticRGB(float hue, float saturation,
			float lightness) {

		validateHue(hue);
		validateSaturation(saturation);
		validateLightness(lightness);

		hue /= 60.0f;
		float t2;
		if (lightness <= 0.5f) {
			t2 = lightness * (saturation + 1);
		} else {
			t2 = lightness + saturation - (lightness * saturation);
		}
		float t1 = lightness * 2.0f - t2;

		float red = hslConvert(t1, t2, hue + 2);
		float green = hslConvert(t1, t2, hue);
		float blue = hslConvert(t1, t2, hue - 2);

		return new float[] { red, green, blue };
	}

	/**
	 * Convert HSL (hue, saturation, and lightness) values to RGB integer values
	 * 
	 * @param hue
	 *            hue value inclusively between 0.0 and 360.0
	 * @param saturation
	 *            saturation inclusively between 0.0 and 1.0
	 * @param lightness
	 *            lightness inclusively between 0.0 and 1.0
	 * @return RGB integer array where: 0 = red, 1 = green, 2 = blue
	 */
	public static int[] toRGB(float hue, float saturation, float lightness) {
		float[] arithmeticRGB = toArithmeticRGB(hue, saturation, lightness);
		int[] rgb = new int[] { toRGB(arithmeticRGB[0]),
				toRGB(arithmeticRGB[1]), toRGB(arithmeticRGB[2]) };
		return rgb;
	}

	/**
	 * HSL convert helper method
	 * 
	 * @param t1
	 *            t1
	 * @param t2
	 *            t2
	 * @param hue
	 *            hue
	 * @return arithmetic RBG value
	 */
	private static float hslConvert(float t1, float t2, float hue) {
		float value;
		if (hue < 0) {
			hue += 6;
		}
		if (hue >= 6) {
			hue -= 6;
		}
		if (hue < 1) {
			value = (t2 - t1) * hue + t1;
		} else if (hue < 3) {
			value = t2;
		} else if (hue < 4) {
			value = (t2 - t1) * (4 - hue) + t1;
		} else {
			value = t1;
		}
		return value;
	}

	/**
	 * Get the hex red color from the hex string
	 * 
	 * @param hex
	 *            hex color
	 * @return hex red color in format RR
	 */
	public static String getRed(String hex) {
		return getHexSingle(hex, 0);
	}

	/**
	 * Get the hex green color from the hex string
	 * 
	 * @param hex
	 *            hex color
	 * @return hex green color in format GG
	 */
	public static String getGreen(String hex) {
		return getHexSingle(hex, 1);
	}

	/**
	 * Get the hex blue color from the hex string
	 * 
	 * @param hex
	 *            hex color
	 * @return hex blue color in format BB
	 */
	public static String getBlue(String hex) {
		return getHexSingle(hex, 2);
	}

	/**
	 * Get the hex alpha color from the hex string if it exists
	 * 
	 * @param hex
	 *            hex color
	 * @return hex alpha color in format AA or null
	 */
	public static String getAlpha(String hex) {
		return getHexSingle(hex, -1);
	}

	/**
	 * Get the hex single color
	 * 
	 * @param hex
	 *            hex color
	 * @param colorIndex
	 *            red=0, green=1, blue=2, alpha=-1
	 * @return hex single color in format FF or null
	 */
	private static String getHexSingle(String hex, int colorIndex) {
		validateHex(hex);

		if (hex.startsWith("#")) {
			hex = hex.substring(1);
		}

		int colorCharacters = 1;
		int numColors = hex.length();
		if (numColors > 4) {
			colorCharacters++;
			numColors /= 2;
		}

		String color = null;
		if (colorIndex >= 0 || numColors > 3) {
			if (numColors > 3) {
				colorIndex++;
			}
			int startIndex = colorIndex;
			if (colorCharacters > 1) {
				startIndex *= 2;
			}
			color = hex.substring(startIndex, startIndex + colorCharacters);
			color = expandShorthandHexSingle(color);
		}

		return color;
	}

	/**
	 * Get the red color from color integer
	 * 
	 * @param color
	 *            color integer
	 * @return red color
	 */
	public static int getRed(int color) {
		return (color >> 16) & 0xff;
	}

	/**
	 * Get the green color from color integer
	 * 
	 * @param color
	 *            color integer
	 * @return green color
	 */
	public static int getGreen(int color) {
		return (color >> 8) & 0xff;
	}

	/**
	 * Get the blue color from color integer
	 * 
	 * @param color
	 *            color integer
	 * @return blue color
	 */
	public static int getBlue(int color) {
		return color & 0xff;
	}

	/**
	 * Get the alpha color from color integer
	 * 
	 * @param color
	 *            color integer
	 * @return alpha color
	 */
	public static int getAlpha(int color) {
		return (color >> 24) & 0xff;
	}

	/**
	 * Shorthand the hex color if possible
	 * 
	 * @param color
	 *            hex color
	 * @return shorthand hex color or original value
	 */
	public static String shorthandHex(String color) {
		validateHex(color);
		if (color.length() > 5) {
			StringBuilder shorthandColor = new StringBuilder();
			int startIndex = 0;
			if (color.startsWith("#")) {
				shorthandColor.append("#");
				startIndex++;
			}
			for (; startIndex < color.length(); startIndex += 2) {
				String shorthand = shorthandHexSingle(
						color.substring(startIndex, startIndex + 2));
				if (shorthand.length() > 1) {
					shorthandColor = null;
					break;
				}
				shorthandColor.append(shorthand);
			}
			if (shorthandColor != null) {
				color = shorthandColor.toString();
			}
		}

		return color;
	}

	/**
	 * Expand the hex if it is in shorthand
	 * 
	 * @param color
	 *            hex color
	 * @return expanded hex color or original value
	 */
	public static String expandShorthandHex(String color) {
		validateHex(color);
		if (color.length() < 6) {
			StringBuilder expandColor = new StringBuilder();
			int startIndex = 0;
			if (color.startsWith("#")) {
				expandColor.append("#");
				startIndex++;
			}
			for (; startIndex < color.length(); startIndex++) {
				String expand = expandShorthandHexSingle(
						color.substring(startIndex, startIndex + 1));
				expandColor.append(expand);
			}
			color = expandColor.toString();
		}
		return color;
	}

	/**
	 * Shorthand the hex single color if possible
	 * 
	 * @param color
	 *            hex single color
	 * @return shorthand hex color or original value
	 */
	public static String shorthandHexSingle(String color) {
		validateHexSingle(color);
		if (color.length() > 1
				&& Character.toUpperCase(color.charAt(0)) == Character
						.toUpperCase(color.charAt(1))) {
			color = color.substring(0, 1);
		}
		return color;
	}

	/**
	 * Expand the hex single if it is in shorthand
	 * 
	 * @param color
	 *            hex single color
	 * @return expanded hex color or original value
	 */
	public static String expandShorthandHexSingle(String color) {
		validateHexSingle(color);
		if (color.length() == 1) {
			color += color;
		}
		return color;
	}

	/**
	 * Check if the hex color value is valid
	 * 
	 * @param color
	 *            hex color
	 * @return true if valid
	 */
	public static boolean isValidHex(String color) {
		return color != null && hexColorPattern.matcher(color).matches();
	}

	/**
	 * Validate the hex color value
	 * 
	 * @param color
	 *            hex color
	 */
	public static void validateHex(String color) {
		if (!isValidHex(color)) {
			throw new GeoPackageException(
					"Hex color must be in format #RRGGBB, #RGB, #AARRGGBB, #ARGB, RRGGBB, RGB, AARRGGBB, or ARGB, invalid value: "
							+ color);
		}
	}

	/**
	 * Check if the hex single color value is valid
	 * 
	 * @param color
	 *            hex single color
	 * @return true if valid
	 */
	public static boolean isValidHexSingle(String color) {
		return color != null && hexSingleColorPattern.matcher(color).matches();
	}

	/**
	 * Validate the hex single color value
	 * 
	 * @param color
	 *            hex single color
	 */
	public static void validateHexSingle(String color) {
		if (!isValidHexSingle(color)) {
			throw new GeoPackageException(
					"Must be in format FF or F, invalid value: " + color);
		}
	}

	/**
	 * Check if the RBG integer color is valid, inclusively between 0 and 255
	 * 
	 * @param color
	 *            decimal color
	 * @return true if valid
	 */
	public static boolean isValidRGB(int color) {
		return color >= 0 && color <= 255;
	}

	/**
	 * Validate the RBG integer color is inclusively between 0 and 255
	 * 
	 * @param color
	 *            decimal color
	 */
	public static void validateRGB(int color) {
		if (!isValidRGB(color)) {
			throw new GeoPackageException(
					"Must be inclusively between 0 and 255, invalid value: "
							+ color);
		}
	}

	/**
	 * Check if the arithmetic RGB float color is valid, inclusively between 0.0
	 * and 1.0
	 * 
	 * @param color
	 *            decimal color
	 * @return true if valid
	 */
	public static boolean isValidArithmeticRGB(float color) {
		return color >= 0.0 && color <= 1.0;
	}

	/**
	 * Validate the arithmetic RGB float color is inclusively between 0.0 and
	 * 1.0
	 * 
	 * @param color
	 *            decimal color
	 */
	public static void validateArithmeticRGB(float color) {
		if (!isValidArithmeticRGB(color)) {
			throw new GeoPackageException(
					"Must be inclusively between 0.0 and 1.0, invalid value: "
							+ color);
		}
	}

	/**
	 * Check if the HSL hue float value is valid, inclusively between 0.0 and
	 * 360.0
	 * 
	 * @param hue
	 *            hue value
	 * @return true if valid
	 */
	public static boolean isValidHue(float hue) {
		return hue >= 0.0 && hue <= 360.0;
	}

	/**
	 * Validate the HSL hue float value is inclusively between 0.0 and 360.0
	 * 
	 * @param hue
	 *            hue value
	 */
	public static void validateHue(float hue) {
		if (!isValidHue(hue)) {
			throw new GeoPackageException(
					"Must be inclusively between 0.0 and 360.0, invalid value: "
							+ hue);
		}
	}

	/**
	 * Check if the HSL saturation float value is valid, inclusively between 0.0
	 * and 1.0
	 * 
	 * @param saturation
	 *            saturation value
	 * @return true if valid
	 */
	public static boolean isValidSaturation(float saturation) {
		return saturation >= 0.0 && saturation <= 1.0;
	}

	/**
	 * Validate the HSL saturation float value is inclusively between 0.0 and
	 * 1.0
	 * 
	 * @param saturation
	 *            saturation value
	 */
	public static void validateSaturation(float saturation) {
		if (!isValidSaturation(saturation)) {
			throw new GeoPackageException(
					"Must be inclusively between 0.0 and 1.0, invalid value: "
							+ saturation);
		}
	}

	/**
	 * Check if the HSL lightness float value is valid, inclusively between 0.0
	 * and 1.0
	 * 
	 * @param lightness
	 *            lightness value
	 * @return true if valid
	 */
	public static boolean isValidLightness(float lightness) {
		return lightness >= 0.0 && lightness <= 1.0;
	}

	/**
	 * Validate the HSL lightness float value is inclusively between 0.0 and 1.0
	 * 
	 * @param lightness
	 *            lightness value
	 */
	public static void validateLightness(float lightness) {
		if (!isValidLightness(lightness)) {
			throw new GeoPackageException(
					"Must be inclusively between 0.0 and 1.0, invalid value: "
							+ lightness);
		}
	}

}
