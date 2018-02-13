package mil.nga.geopackage.extension.coverage;

/**
 * Contains values relevant to a source pixel location when finding a coverage
 * data value
 * 
 * @author osbornb
 * @since 2.0.1
 */
public class CoverageDataSourcePixel {

	/**
	 * Pixel value of where the pixel fits into the source
	 */
	private float pixel;

	/**
	 * Min pixel value
	 */
	private int min;

	/**
	 * Max pixel value
	 */
	private int max;

	/**
	 * Offset between the two pixels
	 */
	private float offset;

	/**
	 * Constructor
	 * 
	 * @param pixel
	 *            pixel value
	 * @param min
	 *            min value
	 * @param max
	 *            max value
	 * @param offset
	 *            pixel offset
	 */
	public CoverageDataSourcePixel(float pixel, int min, int max, float offset) {
		this.pixel = pixel;
		this.min = min;
		this.max = max;
		this.offset = offset;
	}

	/**
	 * Get the pixel
	 * 
	 * @return pixel
	 */
	public float getPixel() {
		return pixel;
	}

	/**
	 * Get the min
	 * 
	 * @return min
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Get the max
	 * 
	 * @return max
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Get the offset
	 * 
	 * @return offset
	 */
	public float getOffset() {
		return offset;
	}

	/**
	 * Set the pixel
	 * 
	 * @param pixel
	 *            pixel value
	 */
	public void setPixel(float pixel) {
		this.pixel = pixel;
	}

	/**
	 * Set the min
	 * 
	 * @param min
	 *            min pixel
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * Set the max
	 * 
	 * @param max
	 *            max pixel
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Set the offset
	 * 
	 * @param offset
	 *            pixel offset
	 */
	public void setOffset(float offset) {
		this.offset = offset;
	}

}
