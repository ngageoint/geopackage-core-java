package mil.nga.tiff;

import mil.nga.tiff.util.TiffException;

/**
 * Raster image values
 * 
 * @author osbornb
 */
public class Rasters {

	/**
	 * Values separated by sample
	 */
	private final Number[][] sampleValues;

	/**
	 * Interleaved pixel sample values
	 */
	private final Number[] interleaveValues;

	/**
	 * Width of pixels
	 */
	private final int width;

	/**
	 * Height of pixels
	 */
	private final int height;

	/**
	 * Samples per pixel
	 */
	private final int samplesPerPixel;

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of pixels
	 * @param height
	 *            height of pixels
	 * @param samplesPerPixel
	 *            samples per pixel
	 * @param sampleValues
	 *            empty sample values double array
	 * @param interleaveValues
	 *            empty interleaved values array
	 */
	public Rasters(int width, int height, int samplesPerPixel,
			Number[][] sampleValues, Number[] interleaveValues) {
		if (sampleValues == null && interleaveValues == null) {
			throw new TiffException(
					"Results must be sample and/or interleave based");
		}
		this.width = width;
		this.height = height;
		this.samplesPerPixel = samplesPerPixel;
		this.sampleValues = sampleValues;
		this.interleaveValues = interleaveValues;
	}

	/**
	 * True if the results are stored by samples
	 * 
	 * @return true if results exist
	 */
	public boolean hasSampleValues() {
		return sampleValues != null;
	}

	/**
	 * True if the results are stored interleaved
	 * 
	 * @return true if results exist
	 */
	public boolean hasInterleaveValues() {
		return interleaveValues != null;
	}

	/**
	 * Add a value to the sample results
	 * 
	 * @param sampleIndex
	 *            sample index
	 * @param coordinate
	 *            coordinate location
	 * @param value
	 *            value
	 */
	public void addToSample(int sampleIndex, int coordinate, Number value) {
		sampleValues[sampleIndex][coordinate] = value;
	}

	/**
	 * Add a value to the interleaved results
	 * 
	 * @param coordinate
	 *            coordinate location
	 * @param value
	 *            value
	 */
	public void addToInterleave(int coordinate, Number value) {
		interleaveValues[coordinate] = value;
	}

	/**
	 * Get the width of pixels
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of pixels
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Return the number of pixels
	 * 
	 * @return number of pixels
	 */
	public int getNumPixels() {
		return width * height;
	}

	/**
	 * Get the number of samples per pixel
	 * 
	 * @return samples per pixel
	 */
	public int getSamplesPerPixel() {
		return samplesPerPixel;
	}

	/**
	 * Get the results stored by samples
	 * 
	 * @return sample values
	 */
	public Number[][] getSampleValues() {
		return sampleValues;
	}

	/**
	 * Get the results stored as interleaved pixel samples
	 * 
	 * @return interleaved values
	 */
	public Number[] getInterleaveValues() {
		return interleaveValues;
	}

	/**
	 * Get the pixel sample values
	 * 
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @return pixel sample values
	 */
	public Number[] getPixel(int x, int y) {

		if (x < 0 || x >= width || y < 0 || y > height) {
			throw new TiffException("Pixel oustide of raster range. Width: "
					+ width + ", Height: " + height + ", x: " + x + ", y: " + y);
		}

		// Pixel with each sample value
		Number[] pixel = new Number[samplesPerPixel];

		// Get the pixel values from each sample
		if (sampleValues != null) {
			int sampleIndex = getSampleIndex(x, y);
			for (int i = 0; i < samplesPerPixel; i++) {
				pixel[i] = sampleValues[i][sampleIndex];
			}
		} else {
			int interleaveIndex = getInterleaveIndex(x, y);
			for (int i = 0; i < samplesPerPixel; i++) {
				pixel[i] = interleaveValues[interleaveIndex++];
			}
		}

		return pixel;
	}

	/**
	 * Get a pixel sample value
	 * 
	 * @param sample
	 *            sample index (>= 0 && < {@link #samplesPerPixel})
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @return pixel sample
	 */
	public Number getPixelSample(int sample, int x, int y) {

		if (x < 0 || x >= width || y < 0 || y > height) {
			throw new TiffException("Pixel oustide of raster range. Width: "
					+ width + ", Height: " + height + ", x: " + x + ", y: " + y);
		}
		if (sample < 0 || sample >= samplesPerPixel) {
			throw new TiffException("Pixel sample out of bounds. sample: "
					+ sample + ", samples per pixel: " + samplesPerPixel);
		}

		// Pixel sample value
		Number pixelSample = null;

		// Get the pixel sample
		if (sampleValues != null) {
			int sampleIndex = getSampleIndex(x, y);
			pixelSample = sampleValues[sample][sampleIndex];
		} else {
			int interleaveIndex = getInterleaveIndex(x, y);
			pixelSample = interleaveValues[interleaveIndex + sample];
		}

		return pixelSample;
	}

	/**
	 * Get the first pixel sample value, useful for single sample pixels
	 * (grayscale)
	 * 
	 * @param x
	 *            x coordinate (>= 0 && < {@link #getWidth()})
	 * @param y
	 *            y coordinate (>= 0 && < {@link #getHeight()})
	 * @return first pixel sample
	 */
	public Number getFirstPixelSample(int x, int y) {
		return getPixelSample(0, x, y);
	}

	/**
	 * Get the sample index location
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return sample index
	 */
	public int getSampleIndex(int x, int y) {
		return y * width + x;
	}

	/**
	 * Get the interleave index location
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return interleave index
	 */
	public int getInterleaveIndex(int x, int y) {
		return (y * width * samplesPerPixel) + (x * samplesPerPixel);
	}

}