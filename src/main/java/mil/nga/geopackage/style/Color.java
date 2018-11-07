package mil.nga.geopackage.style;

import java.util.regex.Pattern;

import mil.nga.geopackage.GeoPackageException;

/**
 * 
 * @author osbornb
 * @since 3.1.1
 */
public class Color {

	/**
	 * Hex color pattern
	 */
	private static Pattern hexColorPattern = Pattern
			.compile("^[#]{0,1}([0-9a-fA-F]{3,4}){1,2}$");

	/**
	 * Hex single color pattern
	 */
	private static Pattern hexSingleColorPattern = Pattern
			.compile("^([0-9a-fA-F]){1,2}$");

	/**
	 * Red hex color, shorthanded when possible
	 */
	private String red = "0";

	/**
	 * Green hex color, shorthanded when possible
	 */
	private String green = "0";

	/**
	 * Blue hex color, shorthanded when possible
	 */
	private String blue = "0";

	/**
	 * Opacity arithmetic value
	 */
	private float opacity = 1.0f;

	public static void main(String[] args) {

		System.out.println(toArithmeticRGB(95));
		System.out.println(toRGB(toArithmeticRGB(95)));

		System.out.println(toRGB("00"));
		System.out.println(toArithmeticRGB("00"));
		System.out.println(toRGB("80"));
		System.out.println(toArithmeticRGB("80"));
		System.out.println(toRGB("FF"));
		System.out.println(toArithmeticRGB("FF"));

		System.out.println(toRGB("ff"));
		System.out.println(toArithmeticRGB("ff"));
		System.out.println();
		System.out.println(toRGB("f"));
		System.out.println(toArithmeticRGB("f"));

		System.out.println(toHex(0));
		System.out.println(toHex(0.0f));
		System.out.println(toHex(6));
		System.out.println(toHex(0.02352941176f));
		System.out.println(toHex(128));
		System.out.println(toHex(0.5f));
		System.out.println(toHex(255));
		System.out.println(toHex(1.0f));

		System.out.println(getRed("A1B2C3"));
		System.out.println(getGreen("A1B2C3"));
		System.out.println(getBlue("A1B2C3"));
		System.out.println(getAlpha("A1B2C3"));
		System.out.println(getRed("D4A1B2C3"));
		System.out.println(getGreen("D4A1B2C3"));
		System.out.println(getBlue("D4A1B2C3"));
		System.out.println(getAlpha("D4A1B2C3"));
		System.out.println(getRed("#A1B2C3"));
		System.out.println(getGreen("#A1B2C3"));
		System.out.println(getBlue("#A1B2C3"));
		System.out.println(getAlpha("#A1B2C3"));
		System.out.println(getRed("#D4A1B2C3"));
		System.out.println(getGreen("#D4A1B2C3"));
		System.out.println(getBlue("#D4A1B2C3"));
		System.out.println(getAlpha("#D4A1B2C3"));

		System.out.println(getRed("ABC"));
		System.out.println(getGreen("ABC"));
		System.out.println(getBlue("ABC"));
		System.out.println(getAlpha("ABC"));
		System.out.println(getRed("DABC"));
		System.out.println(getGreen("DABC"));
		System.out.println(getBlue("DABC"));
		System.out.println(getAlpha("DABC"));
		System.out.println(getRed("#ABC"));
		System.out.println(getGreen("#ABC"));
		System.out.println(getBlue("#ABC"));
		System.out.println(getAlpha("#ABC"));
		System.out.println(getRed("#DABC"));
		System.out.println(getGreen("#DABC"));
		System.out.println(getBlue("#DABC"));
		System.out.println(getAlpha("#DABC"));

		System.out.println(getRed("010203"));
		System.out.println(getGreen("010203"));
		System.out.println(getBlue("010203"));
		System.out.println(getAlpha("010203"));
		System.out.println(getRed("04010203"));
		System.out.println(getGreen("04010203"));
		System.out.println(getBlue("04010203"));
		System.out.println(getAlpha("04010203"));
		System.out.println(getRed("#010203"));
		System.out.println(getGreen("#010203"));
		System.out.println(getBlue("#010203"));
		System.out.println(getAlpha("#010203"));
		System.out.println(getRed("#04010203"));
		System.out.println(getGreen("#04010203"));
		System.out.println(getBlue("#04010203"));
		System.out.println(getAlpha("#04010203"));

		System.out.println(getRed("123"));
		System.out.println(getGreen("123"));
		System.out.println(getBlue("123"));
		System.out.println(getAlpha("123"));
		System.out.println(getRed("4123"));
		System.out.println(getGreen("4123"));
		System.out.println(getBlue("4123"));
		System.out.println(getAlpha("4123"));
		System.out.println(getRed("#123"));
		System.out.println(getGreen("#123"));
		System.out.println(getBlue("#123"));
		System.out.println(getAlpha("#123"));
		System.out.println(getRed("#4123"));
		System.out.println(getGreen("#4123"));
		System.out.println(getBlue("#4123"));
		System.out.println(getAlpha("#4123"));

		System.out.println(getRed("112233"));
		System.out.println(getGreen("112233"));
		System.out.println(getBlue("112233"));
		System.out.println(getAlpha("112233"));
		System.out.println(getRed("44112233"));
		System.out.println(getGreen("44112233"));
		System.out.println(getBlue("44112233"));
		System.out.println(getAlpha("44112233"));
		System.out.println(getRed("#112233"));
		System.out.println(getGreen("#112233"));
		System.out.println(getBlue("#112233"));
		System.out.println(getAlpha("#112233"));
		System.out.println(getRed("#44112233"));
		System.out.println(getGreen("#44112233"));
		System.out.println(getBlue("#44112233"));
		System.out.println(getAlpha("#44112233"));

		System.out.println();
		System.out.println(getRed(-16711936));
		System.out.println(getGreen(-16711936));
		System.out.println(getBlue(-16711936));
		System.out.println(getAlpha(-16711936));

		System.out.println(getRed(65280));
		System.out.println(getGreen(65280));
		System.out.println(getBlue(65280));
		System.out.println(getAlpha(65280));

		System.out.println(toColor(toRGB("00"), toRGB("FF"), toRGB("00")));
		System.out.println(toColorWithAlpha(toRGB("00"), toRGB("FF"),
				toRGB("00"), toRGB("FF")));

		System.out.println(toColor("a0", "b0", "c0"));
		System.out.println(toColorWithAlpha("a0", "b0", "c0"));
		System.out.println(toColorShorthand("a0", "b0", "c0"));
		System.out.println(toColorShorthand("aa", "bb", "cc"));
		System.out.println(toColorShorthandWithAlpha("a0", "b0", "c0"));
		System.out.println(toColorShorthandWithAlpha("aa", "bb", "cc"));
		System.out.println(toColorWithAlpha("a0", "b0", "c0", "d0"));
		System.out.println(toColorShorthandWithAlpha("a0", "b0", "c0", "d0"));
		System.out.println(toColorShorthandWithAlpha("aa", "bb", "cc", "d0"));
		System.out.println(toColorShorthandWithAlpha("aa", "bb", "cc", "dd"));

	}

	/**
	 * Set the color in hex
	 * 
	 * @param color
	 *            hex color in format #RRGGBB, RRGGBB, #RGB, RGB, #AARRGGBB,
	 *            AARRGGBB, #ARGB, or ARGB
	 */
	public void setColor(String color) {
		setRed(getRed(color));
		setGreen(getGreen(color));
		setBlue(getBlue(color));
		String alpha = getAlpha(color);
		if (alpha != null) {
			setAlpha(alpha);
		}
	}

	/**
	 * Set the color in hex with an opacity
	 * 
	 * @param color
	 *            hex color in format #RRGGBB, RRGGBB, #RGB, RGB, #AARRGGBB,
	 *            AARRGGBB, #ARGB, or ARGB
	 * @param opacity
	 *            opacity float inclusively between 0.0 and 1.0
	 */
	public void setColor(String color, float opacity) {
		setColor(color);
		setOpacity(opacity);
	}

	/**
	 * Set the color with individual hex colors
	 * 
	 * @param red
	 *            red hex color in format RR
	 * @param green
	 *            green hex color in format GG
	 * @param blue
	 *            blue hex color in format BB
	 */
	public void setColor(String red, String green, String blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
	}

	/**
	 * Set the color with individual hex colors and alpha
	 * 
	 * @param red
	 *            red hex color in format RR
	 * @param green
	 *            green hex color in format GG
	 * @param blue
	 *            blue hex color in format BB
	 * @param alpha
	 *            alpha hex color in format AA
	 */
	public void setColor(String red, String green, String blue, String alpha) {
		setColor(red, green, blue);
		setAlpha(alpha);
	}

	/**
	 * Set the color with individual hex colors and opacity
	 * 
	 * @param red
	 *            red hex color in format RR
	 * @param green
	 *            green hex color in format GG
	 * @param blue
	 *            blue hex color in format BB
	 * @param opacity
	 *            opacity float inclusively between 0.0 and 1.0
	 */
	public void setColor(String red, String green, String blue, float opacity) {
		setColor(red, green, blue);
		setOpacity(opacity);
	}

	/**
	 * Set the color with RGB values
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 */
	public void setColor(int red, int green, int blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
	}

	/**
	 * Set the color with RGBA values
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 * @param alpha
	 *            alpha integer color inclusively between 0 and 255
	 */
	public void setColor(int red, int green, int blue, int alpha) {
		setColor(red, green, blue);
		setAlpha(alpha);
	}

	/**
	 * Set the color with RGBA values
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 * @param opacity
	 *            opacity float inclusively between 0.0 and 1.0
	 */
	public void setColor(int red, int green, int blue, float opacity) {
		setColor(red, green, blue);
		setOpacity(opacity);
	}

	/**
	 * Set the color with arithmetic RGB values
	 * 
	 * @param red
	 *            red float color inclusively between 0.0 and 1.0
	 * @param green
	 *            green float color inclusively between 0.0 and 1.0
	 * @param blue
	 *            blue float color inclusively between 0.0 and 1.0
	 */
	public void setColor(float red, float green, float blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
	}

	/**
	 * Set the color with arithmetic RGB values
	 * 
	 * @param red
	 *            red float color inclusively between 0.0 and 1.0
	 * @param green
	 *            green float color inclusively between 0.0 and 1.0
	 * @param blue
	 *            blue float color inclusively between 0.0 and 1.0
	 * @param opacity
	 *            opacity float inclusively between 0.0 and 1.0
	 */
	public void setColor(float red, float green, float blue, float opacity) {
		setColor(red, green, blue);
		setOpacity(opacity);
	}

	/**
	 * Set the color as a single integer
	 * 
	 * @param color
	 *            color integer
	 */
	public void setColor(int color) {
		setRed(getRed(color));
		setGreen(getGreen(color));
		setBlue(getBlue(color));
		setAlpha(getAlpha(color));
	}

	/**
	 * Set the red color in hex
	 * 
	 * @param red
	 *            red hex color in format RR or R
	 */
	public void setRed(String red) {
		this.red = formatHexSingle(red);
	}

	/**
	 * Set the green color in hex
	 * 
	 * @param green
	 *            green hex color in format GG or G
	 */
	public void setGreen(String green) {
		this.green = formatHexSingle(green);
	}

	/**
	 * Set the blue color in hex
	 * 
	 * @param blue
	 *            blue hex color in format BB or B
	 */
	public void setBlue(String blue) {
		this.blue = formatHexSingle(blue);
	}

	/**
	 * Set the alpha color in hex
	 * 
	 * @param alpha
	 *            alpha hex color in format AA or A
	 */
	public void setAlpha(String alpha) {
		setOpacity(toArithmeticRGB(alpha));
	}

	/**
	 * Set the red color as an integer
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 */
	public void setRed(int red) {
		setRed(toHex(red));
	}

	/**
	 * Set the green color as an integer
	 * 
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 */
	public void setGreen(int green) {
		setGreen(toHex(green));
	}

	/**
	 * Set the blue color as an integer
	 * 
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 */
	public void setBlue(int blue) {
		setBlue(toHex(blue));
	}

	/**
	 * Set the alpha color as an integer
	 * 
	 * @param alpha
	 *            alpha integer color inclusively between 0 and 255
	 */
	public void setAlpha(int alpha) {
		setOpacity(toArithmeticRGB(alpha));
	}

	/**
	 * Set the red color as an arithmetic float
	 * 
	 * @param red
	 *            red float color inclusively between 0.0 and 1.0
	 */
	public void setRed(float red) {
		setRed(toHex(red));
	}

	/**
	 * Set the green color as an arithmetic float
	 * 
	 * @param green
	 *            green float color inclusively between 0.0 and 1.0
	 */
	public void setGreen(float green) {
		setGreen(toHex(green));
	}

	/**
	 * Set the blue color as an arithmetic float
	 * 
	 * @param blue
	 *            blue float color inclusively between 0.0 and 1.0
	 */
	public void setBlue(float blue) {
		setBlue(toHex(blue));
	}

	/**
	 * Set the opacity as an arithmetic float
	 * 
	 * @param opacity
	 *            opacity float color inclusively between 0.0 and 1.0
	 */
	public void setOpacity(float opacity) {
		validateArithmeticRGB(opacity);
		this.opacity = opacity;
	}

	/**
	 * Set the alpha color as an arithmetic float
	 * 
	 * @param alpha
	 *            alpha float color inclusively between 0.0 and 1.0
	 */
	public void setAlpha(float alpha) {
		setOpacity(alpha);
	}

	/**
	 * Check if the color is opaque (opacity or alpha of 1.0, 255, or x00)
	 * 
	 * @return true if opaque
	 */
	public boolean isOpaque() {
		return opacity == 1.0f;
	}

	/**
	 * Get the color as a hex string
	 * 
	 * @return hex color in the format #RRGGBB
	 */
	public String getColorHex() {
		return toColor(red, green, blue);
	}

	/**
	 * Get the color as a hex string with alpha
	 * 
	 * @return hex color in the format #AARRGGBB
	 */
	public String getColorHexWithAlpha() {
		return toColorWithAlpha(red, green, blue, getAlphaHex());
	}

	/**
	 * Get the color as a hex string, shorthanded when possible
	 * 
	 * @return hex color in the format #RGB or #RRGGBB
	 */
	public String getColorHexShorthand() {
		return toColorShorthand(red, green, blue);
	}

	/**
	 * Get the color as a hex string with alpha, shorthanded when possible
	 * 
	 * @return hex color in the format #ARGB or #AARRGGBB
	 */
	public String getColorHexShorthandWithAlpha() {
		return toColorShorthandWithAlpha(red, green, blue, getAlphaHex());
	}

	/**
	 * Get the color as an integer
	 * 
	 * @return integer color
	 */
	public int getColor() {
		return toColor(getRed(), getGreen(), getBlue());
	}

	/**
	 * Get the color as an integer including the alpha
	 * 
	 * @return integer color
	 */
	public int getColorWithAlpha() {
		return toColorWithAlpha(getRed(), getGreen(), getBlue(), getAlpha());
	}

	/**
	 * Get the red color in hex
	 * 
	 * @return red hex color in format RR
	 */
	public String getRedHex() {
		return expandShorthandHexSingle(red);
	}

	/**
	 * Get the green color in hex
	 * 
	 * @return green hex color in format GG
	 */
	public String getGreenHex() {
		return expandShorthandHexSingle(green);
	}

	/**
	 * Get the blue color in hex
	 * 
	 * @return blue hex color in format BB
	 */
	public String getBlueHex() {
		return expandShorthandHexSingle(blue);
	}

	/**
	 * Get the alpha color in hex
	 * 
	 * @return alpha hex color in format AA
	 */
	public String getAlphaHex() {
		return toHex(opacity);
	}

	/**
	 * Get the red color in hex, shorthand when possible
	 * 
	 * @return red hex color in format R or RR
	 */
	public String getRedHexShorthand() {
		return red;
	}

	/**
	 * Get the green color in hex, shorthand when possible
	 * 
	 * @return green hex color in format G or GG
	 */
	public String getGreenHexShorthand() {
		return green;
	}

	/**
	 * Get the blue color in hex, shorthand when possible
	 * 
	 * @return blue hex color in format B or BB
	 */
	public String getBlueHexShorthand() {
		return blue;
	}

	/**
	 * Get the alpha color in hex, shorthand when possible
	 * 
	 * @return alpha hex color in format A or AA
	 */
	public String getAlphaHexShorthand() {
		return shorthandHexSingle(getAlphaHex());
	}

	/**
	 * Get the red color as an integer
	 * 
	 * @return red integer color inclusively between 0 and 255
	 */
	public int getRed() {
		return toRGB(red);
	}

	/**
	 * Get the green color as an integer
	 * 
	 * @return green integer color inclusively between 0 and 255
	 */
	public int getGreen() {
		return toRGB(green);
	}

	/**
	 * Get the blue color as an integer
	 * 
	 * @return blue integer color inclusively between 0 and 255
	 */
	public int getBlue() {
		return toRGB(blue);
	}

	/**
	 * Get the alpha color as an integer
	 * 
	 * @return alpha integer color inclusively between 0 and 255
	 */
	public int getAlpha() {
		return toRGB(opacity);
	}

	/**
	 * Get the red color as an arithmetic float
	 * 
	 * @return red float color inclusively between 0.0 and 1.0
	 */
	public float getRedArithmetic() {
		return toArithmeticRGB(red);
	}

	/**
	 * Get the green color as an arithmetic float
	 * 
	 * @return green float color inclusively between 0.0 and 1.0
	 */
	public float getGreenArithmetic() {
		return toArithmeticRGB(green);
	}

	/**
	 * Get the blue color as an arithmetic float
	 * 
	 * @return blue float color inclusively between 0.0 and 1.0
	 */
	public float getBlueArithmetic() {
		return toArithmeticRGB(blue);
	}

	/**
	 * Get the opacity as an arithmetic float
	 * 
	 * @return opacity float inclusively between 0.0 and 1.0
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * Get the alpha color as an arithmetic float
	 * 
	 * @return alpha float color inclusively between 0.0 and 1.0
	 */
	public float getAlphaArithmetic() {
		return getOpacity();
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
	public static String toColorShorthand(String red, String green, String blue) {
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
	public static String toColorWithAlpha(String red, String green, String blue) {
		return toColorWithAlpha(red, green, blue, "FF"); // TODO
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
	public static String toColorWithAlpha(String red, String green,
			String blue, String alpha) {
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
	public static int toColorWithAlpha(int red, int green, int blue, int alpha) {
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
	 * Get the hex red color from the hex string
	 * 
	 * @param hex
	 *            hex color
	 * @return hex red color
	 */
	public static String getRed(String hex) {
		return getHexSingle(hex, 0);
	}

	/**
	 * Get the hex green color from the hex string
	 * 
	 * @param hex
	 *            hex color
	 * @return hex green color
	 */
	public static String getGreen(String hex) {
		return getHexSingle(hex, 1);
	}

	/**
	 * Get the hex blue color from the hex string
	 * 
	 * @param hex
	 *            hex color
	 * @return hex blue color
	 */
	public static String getBlue(String hex) {
		return getHexSingle(hex, 2);
	}

	/**
	 * Get the hex alpha color from the hex string if it exists
	 * 
	 * @param hex
	 *            hex color
	 * @return hex alpha color or null
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
	 * @return
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
			color = hex.substring(startIndex, startIndex + colorCharacters)
					.toUpperCase();
		}

		return color;
	}

	/**
	 * Validate and format the hex single color to be upper case and shorthand
	 * 
	 * @param color
	 *            hex single color
	 * @return formatted hex single color
	 */
	private static String formatHexSingle(String color) {
		return shorthandHexSingle(color).toUpperCase();
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
				String shorthand = shorthandHexSingle(color.substring(
						startIndex, startIndex + 2));
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
				String expand = expandShorthandHexSingle(color.substring(
						startIndex, startIndex + 1));
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
			color = color.substring(1, 2);
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
	 * Validate the hex color value
	 * 
	 * @param color
	 *            hex color
	 */
	public static void validateHex(String color) {
		if (color == null || !hexColorPattern.matcher(color).matches()) {
			throw new GeoPackageException(
					"Hex color must be in format #RRGGBB, #RGB, #AARRGGBB, or #ARGB, invalid value: "
							+ color);
		}
	}

	/**
	 * Validate the hex single color value
	 * 
	 * @param color
	 *            hex single color
	 */
	public static void validateHexSingle(String color) {
		if (color == null || !hexSingleColorPattern.matcher(color).matches()) {
			throw new GeoPackageException(
					"Must be in format FF or F, invalid value: " + color);
		}
	}

	/**
	 * Validate the RBG integer color is inclusively between 0 and 255
	 * 
	 * @param color
	 *            decimal color
	 */
	public static void validateRGB(int color) {
		if (color < 0 || color > 255) {
			throw new GeoPackageException(
					"Must be inclusively between 0 and 255, invalid value: "
							+ color);
		}
	}

	/**
	 * Validate the arithmetic RGB float color is inclusively between 0.0 and
	 * 1.0
	 * 
	 * @param color
	 *            decimal color
	 */
	public static void validateArithmeticRGB(float color) {
		if (color < 0.0 || color > 1.0) {
			throw new GeoPackageException(
					"Must be inclusively between 0.0 and 1.0, invalid value: "
							+ color);
		}
	}

}
