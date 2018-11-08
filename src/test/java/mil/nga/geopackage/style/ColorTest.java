package mil.nga.geopackage.style;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Color Test
 * 
 * @author osbornb
 */
public class ColorTest {

	/**
	 * Test color hex contants
	 */
	@Test
	public void testColorHexConstants() {

		validateColor(new Color(ColorConstants.BLACK), 0, "#000000", "#000", 0,
				0, 0);
		validateColor(new Color(ColorConstants.BLUE), 255, "#0000FF", "#00F",
				0, 0, 255);
		validateColor(new Color(ColorConstants.BROWN), 10824234, "#A52A2A",
				"#A52A2A", 165, 42, 42);
		validateColor(new Color(ColorConstants.CYAN), 65535, "#00FFFF", "#0FF",
				0, 255, 255);
		validateColor(new Color(ColorConstants.DKGRAY), 4473924, "#444444",
				"#444", 68, 68, 68);
		validateColor(new Color(ColorConstants.GRAY), 8947848, "#888888",
				"#888", 136, 136, 136);
		validateColor(new Color(ColorConstants.GREEN), 65280, "#00FF00",
				"#0F0", 0, 255, 0);
		validateColor(new Color(ColorConstants.LTGRAY), 13421772, "#CCCCCC",
				"#CCC", 204, 204, 204);
		validateColor(new Color(ColorConstants.MAGENTA), 16711935, "#FF00FF",
				"#F0F", 255, 0, 255);
		validateColor(new Color(ColorConstants.ORANGE), 16753920, "#FFA500",
				"#FFA500", 255, 165, 0);
		validateColor(new Color(ColorConstants.PINK), 16761035, "#FFC0CB",
				"#FFC0CB", 255, 192, 203);
		validateColor(new Color(ColorConstants.PURPLE), 8388736, "#800080",
				"#800080", 128, 0, 128);
		validateColor(new Color(ColorConstants.RED), 16711680, "#FF0000",
				"#F00", 255, 0, 0);
		validateColor(new Color(ColorConstants.VIOLET), 15631086, "#EE82EE",
				"#EE82EE", 238, 130, 238);
		validateColor(new Color(ColorConstants.WHITE), 16777215, "#FFFFFF",
				"#FFF", 255, 255, 255);
		validateColor(new Color(ColorConstants.YELLOW), 16776960, "#FFFF00",
				"#FF0", 255, 255, 0);

		validateColor(new Color(ColorConstants.BLACK, 0.5f), 0,
				Integer.MIN_VALUE, "#000000", "#000", "#80000000", "#80000000",
				0, 0, 0, 128, 0.5f);
		validateColor(new Color(ColorConstants.ORANGE, 0.25f), 16753920,
				1090495744, "#FFA500", "#FFA500", "#40FFA500", "#40FFA500",
				255, 165, 0, 64, 0.25f);
		validateColor(new Color(ColorConstants.YELLOW, 0.85f), 16776960,
				-637534464, "#FFFF00", "#FF0", "#D9FFFF00", "#D9FFFF00", 255,
				255, 0, 217, 0.85f);

		validateColor(new Color("#80000000"), 0, Integer.MIN_VALUE, "#000000",
				"#000", "#80000000", "#80000000", 0, 0, 0, 128, 128.0f / 255.0f);
		validateColor(new Color("#40FFA500"), 16753920, 1090495744, "#FFA500",
				"#FFA500", "#40FFA500", "#40FFA500", 255, 165, 0, 64,
				64.0f / 255.0f);
		validateColor(new Color("#D9FFFF00"), 16776960, -637534464, "#FFFF00",
				"#FF0", "#D9FFFF00", "#D9FFFF00", 255, 255, 0, 217,
				217.0f / 255.0f);

	}

	/**
	 * Test color with integers
	 */
	@Test
	public void testColorIntegers() {

		validateColor(new Color(0), 0, "#000000", "#000", 0, 0, 0);
		validateColor(new Color(255), 255, "#0000FF", "#00F", 0, 0, 255);
		validateColor(new Color(10824234), 10824234, "#A52A2A", "#A52A2A", 165,
				42, 42);
		validateColor(new Color(65535), 65535, "#00FFFF", "#0FF", 0, 255, 255);
		validateColor(new Color(4473924), 4473924, "#444444", "#444", 68, 68,
				68);
		validateColor(new Color(8947848), 8947848, "#888888", "#888", 136, 136,
				136);
		validateColor(new Color(65280), 65280, "#00FF00", "#0F0", 0, 255, 0);
		validateColor(new Color(13421772), 13421772, "#CCCCCC", "#CCC", 204,
				204, 204);
		validateColor(new Color(16711935), 16711935, "#FF00FF", "#F0F", 255, 0,
				255);
		validateColor(new Color(16753920), 16753920, "#FFA500", "#FFA500", 255,
				165, 0);
		validateColor(new Color(16761035), 16761035, "#FFC0CB", "#FFC0CB", 255,
				192, 203);
		validateColor(new Color(8388736), 8388736, "#800080", "#800080", 128,
				0, 128);
		validateColor(new Color(16711680), 16711680, "#FF0000", "#F00", 255, 0,
				0);
		validateColor(new Color(15631086), 15631086, "#EE82EE", "#EE82EE", 238,
				130, 238);
		validateColor(new Color(16777215), 16777215, "#FFFFFF", "#FFF", 255,
				255, 255);
		validateColor(new Color(16776960), 16776960, "#FFFF00", "#FF0", 255,
				255, 0);

	}

	/**
	 * Test color with single alpha based integers
	 */
	@Test
	public void testColorAlphaIntegers() {

		validateColor(new Color(-16777216), 0, "#000000", "#000", 0, 0, 0);
		validateColor(new Color(-16776961), 255, "#0000FF", "#00F", 0, 0, 255);
		validateColor(new Color(-5952982), 10824234, "#A52A2A", "#A52A2A", 165,
				42, 42);
		validateColor(new Color(-16711681), 65535, "#00FFFF", "#0FF", 0, 255,
				255);
		validateColor(new Color(-12303292), 4473924, "#444444", "#444", 68, 68,
				68);
		validateColor(new Color(-7829368), 8947848, "#888888", "#888", 136,
				136, 136);
		validateColor(new Color(-16711936), 65280, "#00FF00", "#0F0", 0, 255, 0);
		validateColor(new Color(-3355444), 13421772, "#CCCCCC", "#CCC", 204,
				204, 204);
		validateColor(new Color(-65281), 16711935, "#FF00FF", "#F0F", 255, 0,
				255);
		validateColor(new Color(-23296), 16753920, "#FFA500", "#FFA500", 255,
				165, 0);
		validateColor(new Color(-16181), 16761035, "#FFC0CB", "#FFC0CB", 255,
				192, 203);
		validateColor(new Color(-8388480), 8388736, "#800080", "#800080", 128,
				0, 128);
		validateColor(new Color(-65536), 16711680, "#FF0000", "#F00", 255, 0, 0);
		validateColor(new Color(-1146130), 15631086, "#EE82EE", "#EE82EE", 238,
				130, 238);
		validateColor(new Color(-1), 16777215, "#FFFFFF", "#FFF", 255, 255, 255);
		validateColor(new Color(-256), 16776960, "#FFFF00", "#FF0", 255, 255, 0);

		validateColor(new Color(16777216), 0, 16777216, "#000000", "#000",
				"#01000000", "#01000000", 0, 0, 0, 1, 0.00392156862f);
		validateColor(new Color(Integer.MAX_VALUE), 16777215, 2147483647,
				"#FFFFFF", "#FFF", "#7FFFFFFF", "#7FFFFFFF", 255, 255, 255,
				127, 127.0f / 255.0f);
		validateColor(new Color(Integer.MIN_VALUE), 0, Integer.MIN_VALUE,
				"#000000", "#000", "#80000000", "#80000000", 0, 0, 0, 128,
				128.0f / 255.0f);
		validateColor(new Color(-1), 16777215, -1, "#FFFFFF", "#FFF",
				"#FFFFFFFF", "#FFFF", 255, 255, 255, 255, 1.0f);

	}

	/**
	 * Test color with RGB integer values
	 */
	@Test
	public void testColorRGB() {

		validateColor(new Color(0, 0, 0), 0, "#000000", "#000", 0, 0, 0);
		validateColor(new Color(0, 0, 255), 255, "#0000FF", "#00F", 0, 0, 255);
		validateColor(new Color(165, 42, 42), 10824234, "#A52A2A", "#A52A2A",
				165, 42, 42);
		validateColor(new Color(0, 255, 255), 65535, "#00FFFF", "#0FF", 0, 255,
				255);
		validateColor(new Color(68, 68, 68), 4473924, "#444444", "#444", 68,
				68, 68);
		validateColor(new Color(136, 136, 136), 8947848, "#888888", "#888",
				136, 136, 136);
		validateColor(new Color(0, 255, 0), 65280, "#00FF00", "#0F0", 0, 255, 0);
		validateColor(new Color(204, 204, 204), 13421772, "#CCCCCC", "#CCC",
				204, 204, 204);
		validateColor(new Color(255, 0, 255), 16711935, "#FF00FF", "#F0F", 255,
				0, 255);
		validateColor(new Color(255, 165, 0), 16753920, "#FFA500", "#FFA500",
				255, 165, 0);
		validateColor(new Color(255, 192, 203), 16761035, "#FFC0CB", "#FFC0CB",
				255, 192, 203);
		validateColor(new Color(128, 0, 128), 8388736, "#800080", "#800080",
				128, 0, 128);
		validateColor(new Color(255, 0, 0), 16711680, "#FF0000", "#F00", 255,
				0, 0);
		validateColor(new Color(238, 130, 238), 15631086, "#EE82EE", "#EE82EE",
				238, 130, 238);
		validateColor(new Color(255, 255, 255), 16777215, "#FFFFFF", "#FFF",
				255, 255, 255);
		validateColor(new Color(255, 255, 0), 16776960, "#FFFF00", "#FF0", 255,
				255, 0);

		validateColor(new Color(0, 0, 0, 128), 0, Integer.MIN_VALUE, "#000000",
				"#000", "#80000000", "#80000000", 0, 0, 0, 128, 128.0 / 255.0f);
		validateColor(new Color(255, 165, 0, 64), 16753920, 1090495744,
				"#FFA500", "#FFA500", "#40FFA500", "#40FFA500", 255, 165, 0,
				64, 64.0 / 255.0f);
		validateColor(new Color(255, 255, 0, 217), 16776960, -637534464,
				"#FFFF00", "#FF0", "#D9FFFF00", "#D9FFFF00", 255, 255, 0, 217,
				217.0 / 255.0f);

		validateColor(new Color(0, 0, 0, 0.5f), 0, Integer.MIN_VALUE,
				"#000000", "#000", "#80000000", "#80000000", 0, 0, 0, 128, 0.5f);
		validateColor(new Color(255, 165, 0, 0.25f), 16753920, 1090495744,
				"#FFA500", "#FFA500", "#40FFA500", "#40FFA500", 255, 165, 0,
				64, 0.25f);
		validateColor(new Color(255, 255, 0, 0.85f), 16776960, -637534464,
				"#FFFF00", "#FF0", "#D9FFFF00", "#D9FFFF00", 255, 255, 0, 217,
				0.85f);

	}

	/**
	 * Test color with arithmetic values
	 */
	@Test
	public void testColorArithmetic() {

		validateColor(new Color(0.0f, 0.0f, 0.0f), 0, "#000000", "#000", 0, 0,
				0);
		validateColor(new Color(0.0f, 0.0f, 1.0f), 255, "#0000FF", "#00F", 0,
				0, 255);
		validateColor(
				new Color(0.64705882352f, 0.16470588235f, 0.16470588235f),
				10824234, "#A52A2A", "#A52A2A", 165, 42, 42);
		validateColor(new Color(0.0f, 1.0f, 1.0f), 65535, "#00FFFF", "#0FF", 0,
				255, 255);
		validateColor(
				new Color(0.26666666666f, 0.26666666666f, 0.26666666666f),
				4473924, "#444444", "#444", 68, 68, 68);
		validateColor(
				new Color(0.53333333333f, 0.53333333333f, 0.53333333333f),
				8947848, "#888888", "#888", 136, 136, 136);
		validateColor(new Color(0.0f, 1.0f, 0.0f), 65280, "#00FF00", "#0F0", 0,
				255, 0);
		validateColor(new Color(0.8f, 0.8f, 0.8f), 13421772, "#CCCCCC", "#CCC",
				204, 204, 204);
		validateColor(new Color(1.0f, 0.0f, 1.0f), 16711935, "#FF00FF", "#F0F",
				255, 0, 255);
		validateColor(new Color(1.0f, 0.64705882352f, 0.0f), 16753920,
				"#FFA500", "#FFA500", 255, 165, 0);
		validateColor(new Color(1.0f, 0.75294117647f, 0.79607843137f),
				16761035, "#FFC0CB", "#FFC0CB", 255, 192, 203);
		validateColor(new Color(0.50196078431f, 0.0f, 0.50196078431f), 8388736,
				"#800080", "#800080", 128, 0, 128);
		validateColor(new Color(1.0f, 0.0f, 0.0f), 16711680, "#FF0000", "#F00",
				255, 0, 0);
		validateColor(
				new Color(0.93333333333f, 0.50980392156f, 0.93333333333f),
				15631086, "#EE82EE", "#EE82EE", 238, 130, 238);
		validateColor(new Color(1.0f, 1.0f, 1.0f), 16777215, "#FFFFFF", "#FFF",
				255, 255, 255);
		validateColor(new Color(1.0f, 1.0f, 0.0f), 16776960, "#FFFF00", "#FF0",
				255, 255, 0);

		validateColor(new Color(0.0f, 0.0f, 0.0f, 0.50196078431f), 0,
				Integer.MIN_VALUE, "#000000", "#000", "#80000000", "#80000000",
				0, 0, 0, 128, 128.0 / 255.0f);
		validateColor(new Color(1.0f, 0.64705882352f, 0.0f, 0.25098039215f),
				16753920, 1090495744, "#FFA500", "#FFA500", "#40FFA500",
				"#40FFA500", 255, 165, 0, 64, 64.0 / 255.0f);
		validateColor(new Color(1.0f, 1.0f, 0.0f, 0.85098039215f), 16776960,
				-637534464, "#FFFF00", "#FF0", "#D9FFFF00", "#D9FFFF00", 255,
				255, 0, 217, 217.0 / 255.0f);

	}

	/**
	 * Test color with hex single color values
	 */
	@Test
	public void testColorHexSingles() {

		validateColor(new Color("00", "00", "00"), 0, "#000000", "#000", 0, 0,
				0);
		validateColor(new Color("00", "00", "FF"), 255, "#0000FF", "#00F", 0,
				0, 255);
		validateColor(new Color("a5", "2a", "2a"), 10824234, "#A52A2A",
				"#A52A2A", 165, 42, 42);
		validateColor(new Color("00", "FF", "ff"), 65535, "#00FFFF", "#0FF", 0,
				255, 255);
		validateColor(new Color("44", "4", "44"), 4473924, "#444444", "#444",
				68, 68, 68);
		validateColor(new Color("8", "88", "8"), 8947848, "#888888", "#888",
				136, 136, 136);
		validateColor(new Color("00", "ff", "00"), 65280, "#00FF00", "#0F0", 0,
				255, 0);
		validateColor(new Color("c", "C", "c"), 13421772, "#CCCCCC", "#CCC",
				204, 204, 204);
		validateColor(new Color("fF", "00", "Ff"), 16711935, "#FF00FF", "#F0F",
				255, 0, 255);
		validateColor(new Color("F", "A5", "0"), 16753920, "#FFA500",
				"#FFA500", 255, 165, 0);
		validateColor(new Color("FF", "C0", "CB"), 16761035, "#FFC0CB",
				"#FFC0CB", 255, 192, 203);
		validateColor(new Color("80", "00", "80"), 8388736, "#800080",
				"#800080", 128, 0, 128);
		validateColor(new Color("ff", "00", "00"), 16711680, "#FF0000", "#F00",
				255, 0, 0);
		validateColor(new Color("ee", "82", "ee"), 15631086, "#EE82EE",
				"#EE82EE", 238, 130, 238);
		validateColor(new Color("FF", "FF", "FF"), 16777215, "#FFFFFF", "#FFF",
				255, 255, 255);
		validateColor(new Color("f", "f", "0"), 16776960, "#FFFF00", "#FF0",
				255, 255, 0);

		validateColor(new Color("00", "00", "00", "80"), 0, Integer.MIN_VALUE,
				"#000000", "#000", "#80000000", "#80000000", 0, 0, 0, 128,
				128.0 / 255.0f);
		validateColor(new Color("f", "A5", "0", "40"), 16753920, 1090495744,
				"#FFA500", "#FFA500", "#40FFA500", "#40FFA500", 255, 165, 0,
				64, 64.0 / 255.0f);
		validateColor(new Color("ff", "F", "00", "D9"), 16776960, -637534464,
				"#FFFF00", "#FF0", "#D9FFFF00", "#D9FFFF00", 255, 255, 0, 217,
				217.0 / 255.0f);

		validateColor(new Color("00", "00", "00", 0.5f), 0, Integer.MIN_VALUE,
				"#000000", "#000", "#80000000", "#80000000", 0, 0, 0, 128, 0.5f);
		validateColor(new Color("ff", "a5", "00", 0.25f), 16753920, 1090495744,
				"#FFA500", "#FFA500", "#40FFA500", "#40FFA500", 255, 165, 0,
				64, 0.25f);
		validateColor(new Color("FF", "FF", "00", 0.85f), 16776960, -637534464,
				"#FFFF00", "#FF0", "#D9FFFF00", "#D9FFFF00", 255, 255, 0, 217,
				0.85f);

	}

	private void validateColor(Color color, int colorInt, String hex,
			String hexShorthand, int red, int green, int blue) {

		String hexAlpha = "#FF" + hex.substring(1);
		String hexShorthandAlpha = null;
		if (hexShorthand.length() <= 4) {
			hexShorthandAlpha = "#F" + hexShorthand.substring(1);
		} else {
			hexShorthandAlpha = "#FF" + hexShorthand.substring(1);
		}
		int alpha = 255;
		double opacity = 1.0f;
		int colorAlphaInt = (alpha & 0xff) << 24 | colorInt;

		validateColor(color, colorInt, colorAlphaInt, hex, hexShorthand,
				hexAlpha, hexShorthandAlpha, red, green, blue, alpha, opacity);

	}

	private void validateColor(Color color, int colorInt, int colorAlphaInt,
			String hex, String hexShorthand, String hexAlpha,
			String hexShorthandAlpha, int red, int green, int blue, int alpha,
			double opacity) {

		TestCase.assertEquals(hex, color.getColorHex());
		TestCase.assertEquals(hexShorthand, color.getColorHexShorthand());
		TestCase.assertEquals(hexAlpha, color.getColorHexWithAlpha());
		TestCase.assertEquals(hexShorthandAlpha,
				color.getColorHexShorthandWithAlpha());

		TestCase.assertEquals(colorInt, color.getColor());
		TestCase.assertEquals(colorAlphaInt, color.getColorWithAlpha());

		TestCase.assertEquals(red, color.getRed());
		TestCase.assertEquals(red / 255.0f, color.getRedArithmetic(),
				0.0000001f);
		String redHex = hex.substring(1, 3);
		TestCase.assertEquals(redHex, color.getRedHex());
		TestCase.assertEquals(ColorUtils.shorthandHexSingle(redHex),
				color.getRedHexShorthand());

		TestCase.assertEquals(green, color.getGreen());
		TestCase.assertEquals(green / 255.0f, color.getGreenArithmetic(),
				0.0000001f);
		String greenHex = hex.substring(3, 5);
		TestCase.assertEquals(greenHex, color.getGreenHex());
		TestCase.assertEquals(ColorUtils.shorthandHexSingle(greenHex),
				color.getGreenHexShorthand());

		TestCase.assertEquals(blue, color.getBlue());
		TestCase.assertEquals(blue / 255.0f, color.getBlueArithmetic(),
				0.0000001f);
		String blueHex = hex.substring(5, 7);
		TestCase.assertEquals(blueHex, color.getBlueHex());
		TestCase.assertEquals(ColorUtils.shorthandHexSingle(blueHex),
				color.getBlueHexShorthand());

		TestCase.assertEquals(opacity, color.getOpacity(), 0.0000001f);
		TestCase.assertEquals(alpha, color.getAlpha());
		TestCase.assertEquals(opacity, color.getAlphaArithmetic(), 0.0000001f);
		String alphaHex = hexAlpha.substring(1, 3);
		TestCase.assertEquals(alphaHex, color.getAlphaHex());
		TestCase.assertEquals(ColorUtils.shorthandHexSingle(alphaHex),
				color.getAlphaHexShorthand());

	}

}
