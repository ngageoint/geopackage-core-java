package mil.nga.geopackage.style;

/**
 * Color representation with support for hex, RBG, arithmetic RBG, and integer
 * colors
 * 
 * @author osbornb
 * @since 3.1.1
 */
public class Color {

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

	/**
	 * Create the color in hex
	 * 
	 * @param color
	 *            hex color in format #RRGGBB, RRGGBB, #RGB, RGB, #AARRGGBB,
	 *            AARRGGBB, #ARGB, or ARGB
	 */
	public Color(String color) {
		setColor(color);
	}

	/**
	 * Create the color in hex with an opacity
	 * 
	 * @param color
	 *            hex color in format #RRGGBB, RRGGBB, #RGB, RGB, #AARRGGBB,
	 *            AARRGGBB, #ARGB, or ARGB
	 * @param opacity
	 *            opacity float inclusively between 0.0 and 1.0
	 */
	public Color(String color, float opacity) {
		setColor(color, opacity);
	}

	/**
	 * Create the color with individual hex colors
	 * 
	 * @param red
	 *            red hex color in format RR
	 * @param green
	 *            green hex color in format GG
	 * @param blue
	 *            blue hex color in format BB
	 */
	public Color(String red, String green, String blue) {
		setColor(red, green, blue);
	}

	/**
	 * Create the color with individual hex colors and alpha
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
	public Color(String red, String green, String blue, String alpha) {
		setColor(red, green, blue, alpha);
	}

	/**
	 * Create the color with individual hex colors and opacity
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
	public Color(String red, String green, String blue, float opacity) {
		setColor(red, green, blue, opacity);
	}

	/**
	 * Create the color with RGB values
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 */
	public Color(int red, int green, int blue) {
		setColor(red, green, blue);
	}

	/**
	 * Create the color with RGBA values
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
	public Color(int red, int green, int blue, int alpha) {
		setColor(red, green, blue, alpha);
	}

	/**
	 * Create the color with RGBA values
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
	public Color(int red, int green, int blue, float opacity) {
		setColor(red, green, blue, opacity);
	}

	/**
	 * Create the color with arithmetic RGB values
	 * 
	 * @param red
	 *            red float color inclusively between 0.0 and 1.0
	 * @param green
	 *            green float color inclusively between 0.0 and 1.0
	 * @param blue
	 *            blue float color inclusively between 0.0 and 1.0
	 */
	public Color(float red, float green, float blue) {
		setColor(red, green, blue);
	}

	/**
	 * Create the color with arithmetic RGB values
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
	public Color(float red, float green, float blue, float opacity) {
		setColor(red, green, blue, opacity);
	}

	/**
	 * Create the color as a single integer
	 * 
	 * @param color
	 *            color integer
	 */
	public Color(int color) {
		setColor(color);
	}

	/**
	 * Set the color in hex
	 * 
	 * @param color
	 *            hex color in format #RRGGBB, RRGGBB, #RGB, RGB, #AARRGGBB,
	 *            AARRGGBB, #ARGB, or ARGB
	 */
	public void setColor(String color) {
		setRed(ColorUtils.getRed(color));
		setGreen(ColorUtils.getGreen(color));
		setBlue(ColorUtils.getBlue(color));
		String alpha = ColorUtils.getAlpha(color);
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
		setRed(ColorUtils.getRed(color));
		setGreen(ColorUtils.getGreen(color));
		setBlue(ColorUtils.getBlue(color));
		setAlpha(ColorUtils.getAlpha(color));
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
		setOpacity(ColorUtils.toArithmeticRGB(alpha));
	}

	/**
	 * Set the red color as an integer
	 * 
	 * @param red
	 *            red integer color inclusively between 0 and 255
	 */
	public void setRed(int red) {
		setRed(ColorUtils.toHex(red));
	}

	/**
	 * Set the green color as an integer
	 * 
	 * @param green
	 *            green integer color inclusively between 0 and 255
	 */
	public void setGreen(int green) {
		setGreen(ColorUtils.toHex(green));
	}

	/**
	 * Set the blue color as an integer
	 * 
	 * @param blue
	 *            blue integer color inclusively between 0 and 255
	 */
	public void setBlue(int blue) {
		setBlue(ColorUtils.toHex(blue));
	}

	/**
	 * Set the alpha color as an integer
	 * 
	 * @param alpha
	 *            alpha integer color inclusively between 0 and 255
	 */
	public void setAlpha(int alpha) {
		setOpacity(ColorUtils.toArithmeticRGB(alpha));
	}

	/**
	 * Set the red color as an arithmetic float
	 * 
	 * @param red
	 *            red float color inclusively between 0.0 and 1.0
	 */
	public void setRed(float red) {
		setRed(ColorUtils.toHex(red));
	}

	/**
	 * Set the green color as an arithmetic float
	 * 
	 * @param green
	 *            green float color inclusively between 0.0 and 1.0
	 */
	public void setGreen(float green) {
		setGreen(ColorUtils.toHex(green));
	}

	/**
	 * Set the blue color as an arithmetic float
	 * 
	 * @param blue
	 *            blue float color inclusively between 0.0 and 1.0
	 */
	public void setBlue(float blue) {
		setBlue(ColorUtils.toHex(blue));
	}

	/**
	 * Set the opacity as an arithmetic float
	 * 
	 * @param opacity
	 *            opacity float color inclusively between 0.0 and 1.0
	 */
	public void setOpacity(float opacity) {
		ColorUtils.validateArithmeticRGB(opacity);
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
		return ColorUtils.toColor(red, green, blue);
	}

	/**
	 * Get the color as a hex string with alpha
	 * 
	 * @return hex color in the format #AARRGGBB
	 */
	public String getColorHexWithAlpha() {
		return ColorUtils.toColorWithAlpha(red, green, blue, getAlphaHex());
	}

	/**
	 * Get the color as a hex string, shorthanded when possible
	 * 
	 * @return hex color in the format #RGB or #RRGGBB
	 */
	public String getColorHexShorthand() {
		return ColorUtils.toColorShorthand(red, green, blue);
	}

	/**
	 * Get the color as a hex string with alpha, shorthanded when possible
	 * 
	 * @return hex color in the format #ARGB or #AARRGGBB
	 */
	public String getColorHexShorthandWithAlpha() {
		return ColorUtils.toColorShorthandWithAlpha(red, green, blue,
				getAlphaHex());
	}

	/**
	 * Get the color as an integer
	 * 
	 * @return integer color
	 */
	public int getColor() {
		return ColorUtils.toColor(getRed(), getGreen(), getBlue());
	}

	/**
	 * Get the color as an integer including the alpha
	 * 
	 * @return integer color
	 */
	public int getColorWithAlpha() {
		return ColorUtils.toColorWithAlpha(getRed(), getGreen(), getBlue(),
				getAlpha());
	}

	/**
	 * Get the red color in hex
	 * 
	 * @return red hex color in format RR
	 */
	public String getRedHex() {
		return ColorUtils.expandShorthandHexSingle(red);
	}

	/**
	 * Get the green color in hex
	 * 
	 * @return green hex color in format GG
	 */
	public String getGreenHex() {
		return ColorUtils.expandShorthandHexSingle(green);
	}

	/**
	 * Get the blue color in hex
	 * 
	 * @return blue hex color in format BB
	 */
	public String getBlueHex() {
		return ColorUtils.expandShorthandHexSingle(blue);
	}

	/**
	 * Get the alpha color in hex
	 * 
	 * @return alpha hex color in format AA
	 */
	public String getAlphaHex() {
		return ColorUtils.toHex(opacity);
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
		return ColorUtils.shorthandHexSingle(getAlphaHex());
	}

	/**
	 * Get the red color as an integer
	 * 
	 * @return red integer color inclusively between 0 and 255
	 */
	public int getRed() {
		return ColorUtils.toRGB(red);
	}

	/**
	 * Get the green color as an integer
	 * 
	 * @return green integer color inclusively between 0 and 255
	 */
	public int getGreen() {
		return ColorUtils.toRGB(green);
	}

	/**
	 * Get the blue color as an integer
	 * 
	 * @return blue integer color inclusively between 0 and 255
	 */
	public int getBlue() {
		return ColorUtils.toRGB(blue);
	}

	/**
	 * Get the alpha color as an integer
	 * 
	 * @return alpha integer color inclusively between 0 and 255
	 */
	public int getAlpha() {
		return ColorUtils.toRGB(opacity);
	}

	/**
	 * Get the red color as an arithmetic float
	 * 
	 * @return red float color inclusively between 0.0 and 1.0
	 */
	public float getRedArithmetic() {
		return ColorUtils.toArithmeticRGB(red);
	}

	/**
	 * Get the green color as an arithmetic float
	 * 
	 * @return green float color inclusively between 0.0 and 1.0
	 */
	public float getGreenArithmetic() {
		return ColorUtils.toArithmeticRGB(green);
	}

	/**
	 * Get the blue color as an arithmetic float
	 * 
	 * @return blue float color inclusively between 0.0 and 1.0
	 */
	public float getBlueArithmetic() {
		return ColorUtils.toArithmeticRGB(blue);
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
	 * Validate and format the hex single color to be upper case and shorthand
	 * 
	 * @param color
	 *            hex single color
	 * @return formatted hex single color
	 */
	private static String formatHexSingle(String color) {
		return ColorUtils.shorthandHexSingle(color).toUpperCase();
	}

}
