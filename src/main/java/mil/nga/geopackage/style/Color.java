package mil.nga.geopackage.style;

/**
 * Color representation with support for hex, RBG, arithmetic RBG, HSL, and
 * integer colors
 * 
 * @author osbornb
 * @since 3.2.0
 */
public class Color {

	/**
	 * Red arithmetic color value
	 */
	private float red = 0.0f;

	/**
	 * Green arithmetic color value
	 */
	private float green = 0.0f;

	/**
	 * Blue arithmetic color value
	 */
	private float blue = 0.0f;

	/**
	 * Opacity arithmetic value
	 */
	private float opacity = 1.0f;

	/**
	 * Default color constructor, opaque black
	 */
	public Color() {

	}

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
	 * Create the color in hex with an alpha
	 * 
	 * @param color
	 *            hex color in format #RRGGBB, RRGGBB, #RGB, RGB, #AARRGGBB,
	 *            AARRGGBB, #ARGB, or ARGB
	 * @param alpha
	 *            alpha integer color inclusively between 0 and 255
	 */
	public Color(String color, int alpha) {
		setColor(color, alpha);
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
	 * Create the color with HSL (hue, saturation, lightness) or HSL (alpha)
	 * values
	 * 
	 * @param hsl
	 *            HSL array where: 0 = hue, 1 = saturation, 2 = lightness,
	 *            optional 3 = alpha
	 */
	public Color(float[] hsl) {
		if (hsl.length > 3) {
			setColorByHSL(hsl[0], hsl[1], hsl[2], hsl[3]);
		} else {
			setColorByHSL(hsl[0], hsl[1], hsl[2]);
		}
	}

	/**
	 * Create the color with HSLA (hue, saturation, lightness, alpha) values
	 * 
	 * @param hsl
	 *            HSL array where: 0 = hue, 1 = saturation, 2 = lightness
	 * @param alpha
	 *            alpha inclusively between 0.0 and 1.0
	 */
	public Color(float[] hsl, float alpha) {
		setColorByHSL(hsl[0], hsl[1], hsl[2], alpha);
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
	 * Set the color in hex with an alpha
	 * 
	 * @param color
	 *            hex color in format #RRGGBB, RRGGBB, #RGB, RGB, #AARRGGBB,
	 *            AARRGGBB, #ARGB, or ARGB
	 * @param alpha
	 *            alpha integer color inclusively between 0 and 255
	 */
	public void setColor(String color, int alpha) {
		setColor(color);
		setAlpha(alpha);
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
	 * Set the color with HSL (hue, saturation, lightness) values
	 * 
	 * @param hue
	 *            hue value inclusively between 0.0 and 360.0
	 * @param saturation
	 *            saturation inclusively between 0.0 and 1.0
	 * @param lightness
	 *            lightness inclusively between 0.0 and 1.0
	 */
	public void setColorByHSL(float hue, float saturation, float lightness) {
		float[] arithmeticRGB = ColorUtils.toArithmeticRGB(hue, saturation,
				lightness);
		setRed(arithmeticRGB[0]);
		setGreen(arithmeticRGB[1]);
		setBlue(arithmeticRGB[2]);
	}

	/**
	 * Set the color with HSLA (hue, saturation, lightness, alpha) values
	 * 
	 * @param hue
	 *            hue value inclusively between 0.0 and 360.0
	 * @param saturation
	 *            saturation inclusively between 0.0 and 1.0
	 * @param lightness
	 *            lightness inclusively between 0.0 and 1.0
	 * @param alpha
	 *            alpha inclusively between 0.0 and 1.0
	 */
	public void setColorByHSL(float hue, float saturation, float lightness,
			float alpha) {
		setColorByHSL(hue, saturation, lightness);
		setAlpha(alpha);
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
		if (color > 16777215 || color < 0) {
			setAlpha(ColorUtils.getAlpha(color));
		}
	}

	/**
	 * Set the red color in hex
	 * 
	 * @param red
	 *            red hex color in format RR or R
	 */
	public void setRed(String red) {
		setRed(ColorUtils.toArithmeticRGB(red));
	}

	/**
	 * Set the green color in hex
	 * 
	 * @param green
	 *            green hex color in format GG or G
	 */
	public void setGreen(String green) {
		setGreen(ColorUtils.toArithmeticRGB(green));
	}

	/**
	 * Set the blue color in hex
	 * 
	 * @param blue
	 *            blue hex color in format BB or B
	 */
	public void setBlue(String blue) {
		setBlue(ColorUtils.toArithmeticRGB(blue));
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
		ColorUtils.validateArithmeticRGB(red);
		this.red = red;
	}

	/**
	 * Set the green color as an arithmetic float
	 * 
	 * @param green
	 *            green float color inclusively between 0.0 and 1.0
	 */
	public void setGreen(float green) {
		ColorUtils.validateArithmeticRGB(green);
		this.green = green;
	}

	/**
	 * Set the blue color as an arithmetic float
	 * 
	 * @param blue
	 *            blue float color inclusively between 0.0 and 1.0
	 */
	public void setBlue(float blue) {
		ColorUtils.validateArithmeticRGB(blue);
		this.blue = blue;
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
		return ColorUtils.toColor(getRedHex(), getGreenHex(), getBlueHex());
	}

	/**
	 * Get the color as a hex string with alpha
	 * 
	 * @return hex color in the format #AARRGGBB
	 */
	public String getColorHexWithAlpha() {
		return ColorUtils.toColorWithAlpha(getRedHex(), getGreenHex(),
				getBlueHex(), getAlphaHex());
	}

	/**
	 * Get the color as a hex string, shorthanded when possible
	 * 
	 * @return hex color in the format #RGB or #RRGGBB
	 */
	public String getColorHexShorthand() {
		return ColorUtils.toColorShorthand(getRedHex(), getGreenHex(),
				getBlueHex());
	}

	/**
	 * Get the color as a hex string with alpha, shorthanded when possible
	 * 
	 * @return hex color in the format #ARGB or #AARRGGBB
	 */
	public String getColorHexShorthandWithAlpha() {
		return ColorUtils.toColorShorthandWithAlpha(getRedHex(), getGreenHex(),
				getBlueHex(), getAlphaHex());
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
		return ColorUtils.toHex(red);
	}

	/**
	 * Get the green color in hex
	 * 
	 * @return green hex color in format GG
	 */
	public String getGreenHex() {
		return ColorUtils.toHex(green);
	}

	/**
	 * Get the blue color in hex
	 * 
	 * @return blue hex color in format BB
	 */
	public String getBlueHex() {
		return ColorUtils.toHex(blue);
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
		return ColorUtils.shorthandHexSingle(getRedHex());
	}

	/**
	 * Get the green color in hex, shorthand when possible
	 * 
	 * @return green hex color in format G or GG
	 */
	public String getGreenHexShorthand() {
		return ColorUtils.shorthandHexSingle(getGreenHex());
	}

	/**
	 * Get the blue color in hex, shorthand when possible
	 * 
	 * @return blue hex color in format B or BB
	 */
	public String getBlueHexShorthand() {
		return ColorUtils.shorthandHexSingle(getBlueHex());
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
		return red;
	}

	/**
	 * Get the green color as an arithmetic float
	 * 
	 * @return green float color inclusively between 0.0 and 1.0
	 */
	public float getGreenArithmetic() {
		return green;
	}

	/**
	 * Get the blue color as an arithmetic float
	 * 
	 * @return blue float color inclusively between 0.0 and 1.0
	 */
	public float getBlueArithmetic() {
		return blue;
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
	 * Get the HSL (hue, saturation, lightness) values
	 * 
	 * @return HSL array where: 0 = hue, 1 = saturation, 2 = lightness
	 */
	public float[] getHSL() {
		return ColorUtils.toHSL(red, green, blue);
	}

	/**
	 * Get the HSL hue value
	 * 
	 * @return hue value
	 */
	public float getHue() {
		return getHSL()[0];
	}

	/**
	 * Get the HSL saturation value
	 * 
	 * @return saturation value
	 */
	public float getSaturation() {
		return getHSL()[1];
	}

	/**
	 * Get the HSL lightness value
	 * 
	 * @return lightness value
	 */
	public float getLightness() {
		return getHSL()[2];
	}

}
