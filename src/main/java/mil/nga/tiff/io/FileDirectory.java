package mil.nga.tiff.io;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.nga.tiff.io.compression.CompressionDecoder;
import mil.nga.tiff.io.compression.DeflateDecoder;
import mil.nga.tiff.io.compression.LZWDecoder;
import mil.nga.tiff.io.compression.PackbitsDecoder;
import mil.nga.tiff.io.compression.RawDecoder;
import mil.nga.tiff.util.TiffException;

/**
 * File Directory, represents all directory entries and can be used to read the
 * image raster
 * 
 * @author osbornb
 */
public class FileDirectory {

	/**
	 * File directory entries in sorted tag id order
	 */
	private final List<FileDirectoryEntry> entries;

	/**
	 * Mapping between tags and entries
	 */
	private final Map<FieldTagType, FileDirectoryEntry> fieldTagTypeMapping = new HashMap<>();

	/**
	 * Byte reader
	 */
	private final ByteReader reader;

	/**
	 * Tiled flag
	 */
	private final boolean tiled;

	/**
	 * Planar configuration
	 */
	private final int planarConfiguration;

	/**
	 * Compression decoder
	 */
	private final CompressionDecoder decoder;

	/**
	 * Cache
	 */
	private Map<Integer, byte[]> cache = null;

	/**
	 * Constructor
	 * 
	 * @param entries
	 *            file directory entries
	 * @param reader
	 *            TIFF file byte reader
	 * @param cacheTiles
	 *            true to cache tiles
	 */
	public FileDirectory(List<FileDirectoryEntry> entries, ByteReader reader) {
		this(entries, reader, false);
	}

	/**
	 * Constructor
	 * 
	 * @param entries
	 *            file directory entries
	 * @param reader
	 *            TIFF file byte reader
	 * @param cacheData
	 *            true to cache tiles and strips
	 */
	public FileDirectory(List<FileDirectoryEntry> entries, ByteReader reader,
			boolean cacheData) {
		// Set the entries and the field tag type mapping
		this.entries = entries;
		for (FileDirectoryEntry entry : entries) {
			fieldTagTypeMapping.put(entry.getFieldTag(), entry);
		}

		this.reader = reader;

		// Set the cache
		setCache(cacheData);

		// Determine if tiled
		tiled = getStripOffsets() == null;

		// Determine and validate the planar configuration
		Integer pc = getPlanarConfiguration();
		planarConfiguration = pc != null ? pc : 1;
		if (planarConfiguration != 1 && planarConfiguration != 2) {
			throw new TiffException("Invalid planar configuration: "
					+ planarConfiguration);
		}

		// Determine the decoder based upon the compression
		Integer compression = getCompression();
		if (compression == null) {
			compression = 1;
		}
		switch (compression) {
		case 1: // No Compression
			decoder = new RawDecoder();
			break;
		case 2: // CCITT Huffman
			throw new TiffException("CCITT Huffman compression not supported: "
					+ compression);
		case 3: // T4-encoding
			throw new TiffException("T4-encoding compression not supported: "
					+ compression);
		case 4: // T6-encoding
			throw new TiffException("T6-encoding compression not supported: "
					+ compression);
		case 5: // LZW
			decoder = new LZWDecoder();
			break;
		case 6: // JPEG (old)
		case 7: // JPEG (new)
			throw new TiffException("JPEG compression not supported: "
					+ compression);
		case 8: // Deflate
			decoder = new DeflateDecoder();
			break;
		case 32773: // packbits
			decoder = new PackbitsDecoder();
			break;
		default:
			throw new TiffException("Unknown compression method identifier: "
					+ compression);
		}
	}

	/**
	 * Set whether to cache tiles. Does nothing is already caching tiles, clears
	 * the existing cache if set to false.
	 * 
	 * @param cacheData
	 *            true to cache tiles and strips
	 */
	public void setCache(boolean cacheData) {
		if (cacheData) {
			if (cache == null) {
				cache = new HashMap<>();
			}
		} else {
			cache = null;
		}
	}

	/**
	 * Get the byte reader
	 * 
	 * @return byte reader
	 */
	public ByteReader getReader() {
		return reader;
	}

	/**
	 * Is this a tiled image
	 * 
	 * @return true if tiled
	 */
	public boolean isTiled() {
		return tiled;
	}

	/**
	 * Get the compression decoder
	 * 
	 * @return compression decoder
	 */
	public CompressionDecoder getDecoder() {
		return decoder;
	}

	/**
	 * Get the number of entries
	 * 
	 * @return entry count
	 */
	public int numEntries() {
		return entries.size();
	}

	/**
	 * Get a file directory entry from the field tag type
	 * 
	 * @param fieldTagType
	 *            field tag type
	 * @return file directory entry
	 */
	public FileDirectoryEntry get(FieldTagType fieldTagType) {
		return fieldTagTypeMapping.get(fieldTagType);
	}

	/**
	 * Get the file directory entries
	 * 
	 * @return file directory entries
	 */
	public List<FileDirectoryEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	/**
	 * Get the field tag type to file directory entry mapping
	 * 
	 * @return field tag type mapping
	 */
	public Map<FieldTagType, FileDirectoryEntry> getFieldTagTypeMapping() {
		return Collections.unmodifiableMap(fieldTagTypeMapping);
	}

	/**
	 * Get the image width
	 * 
	 * @return image width
	 */
	public Number getImageWidth() {
		return getNumberEntryValue(FieldTagType.ImageWidth);
	}

	/**
	 * Get the image height
	 * 
	 * @return image height
	 */
	public Number getImageHeight() {
		return getNumberEntryValue(FieldTagType.ImageLength);
	}

	/**
	 * Get the tile width
	 * 
	 * @return tile width
	 */
	public Number getTileWidth() {
		return tiled ? getNumberEntryValue(FieldTagType.TileWidth)
				: getImageWidth();
	}

	/**
	 * Get the tile height
	 * 
	 * @return tile height
	 */
	public Number getTileHeight() {
		return tiled ? getNumberEntryValue(FieldTagType.TileLength)
				: getRowsPerStrip();
	}

	/**
	 * Get the samples per pixel
	 * 
	 * @return samples per pixel
	 */
	public Integer getSamplesPerPixel() {
		return getIntegerEntryValue(FieldTagType.SamplesPerPixel);
	}

	/**
	 * Get the bits per sample
	 * 
	 * @return bits per sample
	 */
	public List<Integer> getBitsPerSample() {
		return getIntegerListEntryValue(FieldTagType.BitsPerSample);
	}

	/**
	 * Get the max bits per sample
	 * 
	 * @return max bits per sample
	 */
	public Integer getMaxBitsPerSample() {
		return getMaxIntegerEntryValue(FieldTagType.BitsPerSample);
	}

	/**
	 * Get the compression
	 * 
	 * @return compression
	 */
	public Integer getCompression() {
		return getIntegerEntryValue(FieldTagType.Compression);
	}

	/**
	 * Get the photometric interpretation
	 * 
	 * @return photometric interpretation
	 */
	public Integer getPhotometricInterpretation() {
		return getIntegerEntryValue(FieldTagType.PhotometricInterpretation);
	}

	/**
	 * Get the strip offsets
	 * 
	 * @return strip offsets
	 */
	public List<Number> getStripOffsets() {
		return getNumberListEntryValue(FieldTagType.StripOffsets);
	}

	/**
	 * Get the rows per strip
	 * 
	 * @return rows per strip
	 */
	public Number getRowsPerStrip() {
		return getNumberEntryValue(FieldTagType.RowsPerStrip);
	}

	/**
	 * Get the strip byte counts
	 * 
	 * @return strip byte counts
	 */
	public List<Number> getStripByteCounts() {
		return getNumberListEntryValue(FieldTagType.StripByteCounts);
	}

	/**
	 * Get the planar configuration
	 * 
	 * @return planar configuration
	 */
	public Integer getPlanarConfiguration() {
		return getIntegerEntryValue(FieldTagType.PlanarConfiguration);
	}

	/**
	 * Get the sample format
	 * 
	 * @return sample format
	 */
	public List<Integer> getSampleFormat() {
		return getIntegerListEntryValue(FieldTagType.SampleFormat);
	}

	/**
	 * Get the max sample format
	 * 
	 * @return max sample format
	 */
	public Integer getMaxSampleFormat() {
		return getMaxIntegerEntryValue(FieldTagType.SampleFormat);
	}

	/**
	 * Get the tile offsets
	 * 
	 * @return tile offsets
	 */
	public List<Long> getTileOffsets() {
		return getLongListEntryValue(FieldTagType.TileOffsets);
	}

	/**
	 * Get the tile byte counts
	 * 
	 * @return tile byte counts
	 */
	public List<Number> getTileByteCounts() {
		return getNumberListEntryValue(FieldTagType.TileByteCounts);
	}

	/**
	 * Get the color map
	 * 
	 * @return color map
	 */
	public List<Integer> getColorMap() {
		return getIntegerListEntryValue(FieldTagType.ColorMap);
	}

	/**
	 * Get the x resolution
	 * 
	 * @return x resolution
	 */
	public List<Long> getXResolution() {
		return getLongListEntryValue(FieldTagType.XResolution);
	}

	/**
	 * Get the resolution unit
	 * 
	 * @return resolution unit
	 */
	public Integer getResolutionUnit() {
		return getIntegerEntryValue(FieldTagType.ResolutionUnit);
	}

	/**
	 * Get the y resolution
	 * 
	 * @return y resolution
	 */
	public List<Long> getYResolution() {
		return getLongListEntryValue(FieldTagType.YResolution);
	}

	/**
	 * Read the rasters
	 * 
	 * @return rasters
	 */
	public Rasters readRasters() {
		ImageWindow window = new ImageWindow(this);
		return readRasters(window);
	}

	/**
	 * Read the rasters as interleaved
	 * 
	 * @return rasters
	 */
	public Rasters readInterleavedRasters() {
		ImageWindow window = new ImageWindow(this);
		return readInterleavedRasters(window);
	}

	/**
	 * Read the rasters
	 * 
	 * @param window
	 *            image window
	 * @return rasters
	 */
	public Rasters readRasters(ImageWindow window) {
		return readRasters(window, null);
	}

	/**
	 * Read the rasters as interleaved
	 * 
	 * @param window
	 *            image window
	 * @return rasters
	 */
	public Rasters readInterleavedRasters(ImageWindow window) {
		return readInterleavedRasters(window, null);
	}

	/**
	 * Read the rasters
	 * 
	 * @param samples
	 *            pixel samples to read
	 * @return rasters
	 */
	public Rasters readRasters(int[] samples) {
		ImageWindow window = new ImageWindow(this);
		return readRasters(window, samples);
	}

	/**
	 * Read the rasters as interleaved
	 * 
	 * @param samples
	 *            pixel samples to read
	 * @return rasters
	 */
	public Rasters readInterleavedRasters(int[] samples) {
		ImageWindow window = new ImageWindow(this);
		return readInterleavedRasters(window, samples);
	}

	/**
	 * Read the rasters
	 * 
	 * @param window
	 *            image window
	 * @param samples
	 *            pixel samples to read
	 * @return rasters
	 */
	public Rasters readRasters(ImageWindow window, int[] samples) {
		return readRasters(window, samples, true, false);
	}

	/**
	 * Read the rasters as interleaved
	 * 
	 * @param window
	 *            image window
	 * @param samples
	 *            pixel samples to read
	 * @return rasters
	 */
	public Rasters readInterleavedRasters(ImageWindow window, int[] samples) {
		return readRasters(window, samples, false, true);
	}

	/**
	 * Read the rasters
	 * 
	 * @param sampleValues
	 *            true to read results per sample
	 * @param interleaveValues
	 *            true to read results as interleaved
	 * @return rasters
	 */
	public Rasters readRasters(boolean sampleValues, boolean interleaveValues) {
		ImageWindow window = new ImageWindow(this);
		return readRasters(window, sampleValues, interleaveValues);
	}

	/**
	 * Read the rasters
	 * 
	 * @param window
	 *            image window
	 * @param sampleValues
	 *            true to read results per sample
	 * @param interleaveValues
	 *            true to read results as interleaved
	 * @return rasters
	 */
	public Rasters readRasters(ImageWindow window, boolean sampleValues,
			boolean interleaveValues) {
		return readRasters(window, null, sampleValues, interleaveValues);
	}

	/**
	 * Read the rasters
	 * 
	 * @param samples
	 *            pixel samples to read
	 * @param sampleValues
	 *            true to read results per sample
	 * @param interleaveValues
	 *            true to read results as interleaved
	 * @return rasters
	 */
	public Rasters readRasters(int[] samples, boolean sampleValues,
			boolean interleaveValues) {
		ImageWindow window = new ImageWindow(this);
		return readRasters(window, samples, sampleValues, interleaveValues);
	}

	/**
	 * Read the rasters
	 * 
	 * @param window
	 *            image window
	 * @param samples
	 *            pixel samples to read
	 * @param sampleValues
	 *            true to read results per sample
	 * @param interleaveValues
	 *            true to read results as interleaved
	 * @return rasters
	 */
	public Rasters readRasters(ImageWindow window, int[] samples,
			boolean sampleValues, boolean interleaveValues) {

		int width = getImageWidth().intValue();
		int height = getImageHeight().intValue();

		// Validate the image window
		if (window.getMinX() < 0 || window.getMinY() < 0
				|| window.getMaxX() > width || window.getMaxY() > height) {
			throw new TiffException(
					"Window is out of the image bounds. Width: " + width
							+ ", Height: " + height + ", Window: " + window);
		} else if (window.getMinX() > window.getMaxX()
				|| window.getMinY() > window.getMaxY()) {
			throw new TiffException("Invalid window range: " + window);
		}

		int windowWidth = window.getMaxX() - window.getMinX();
		int windowHeight = window.getMaxY() - window.getMinY();
		int numPixels = windowWidth * windowHeight;

		// Set or validate the samples
		int samplesPerPixel = getSamplesPerPixel();
		if (samples == null) {
			samples = new int[samplesPerPixel];
			for (int i = 0; i < samples.length; i++) {
				samples[i] = i;
			}
		} else {
			for (int i = 0; i < samples.length; i++) {
				if (samples[i] >= samplesPerPixel) {
					throw new TiffException("Invalid sample index: "
							+ samples[i]);
				}
			}
		}

		// Create the interleaved result array
		Number[] interleave = null;
		if (interleaveValues) {
			interleave = new Number[numPixels * samples.length];
		}

		// Create the sample indexed result double array
		Number[][] sample = null;
		if (sampleValues) {
			sample = new Number[samples.length][numPixels];
		}

		// Create the rasters results
		Rasters rasters = new Rasters(windowWidth, windowHeight,
				samplesPerPixel, sample, interleave);

		// Read the rasters
		readRaster(window, samples, rasters);

		return rasters;
	}

	/**
	 * Read and populate the rasters
	 * 
	 * @param window
	 *            image window
	 * @param samples
	 *            pixel samples to read
	 * @param rasters
	 *            rasters to populate
	 */
	private void readRaster(ImageWindow window, int[] samples, Rasters rasters) {

		int tileWidth = getTileWidth().intValue();
		int tileHeight = getTileHeight().intValue();

		int minXTile = (int) Math
				.floor(window.getMinX() / ((double) tileWidth));
		int maxXTile = (int) Math.ceil(window.getMaxX() / ((double) tileWidth));
		int minYTile = (int) Math.floor(window.getMinY()
				/ ((double) tileHeight));
		int maxYTile = (int) Math
				.ceil(window.getMaxY() / ((double) tileHeight));

		int windowWidth = window.getMaxX() - window.getMinX();

		int bytesPerPixel = getBytesPerPixel();

		int[] srcSampleOffsets = new int[samples.length];
		FieldType[] sampleFieldTypes = new FieldType[samples.length];
		for (int i = 0; i < samples.length; i++) {
			int sampleOffset = 0;
			if (planarConfiguration == 1) {
				sampleOffset = sum(getBitsPerSample(), 0, samples[i]) / 8;
			}
			srcSampleOffsets[i] = sampleOffset;
			sampleFieldTypes[i] = getFieldTypeForSample(samples[i]);
		}

		for (int yTile = minYTile; yTile < maxYTile; yTile++) {
			for (int xTile = minXTile; xTile < maxXTile; xTile++) {

				int firstLine = yTile * tileHeight;
				int firstCol = xTile * tileWidth;
				int lastLine = (yTile + 1) * tileHeight;
				int lastCol = (xTile + 1) * tileWidth;

				for (int sampleIndex = 0; sampleIndex < samples.length; sampleIndex++) {
					int sample = samples[sampleIndex];
					if (planarConfiguration == 2) {
						bytesPerPixel = getSampleByteSize(sample);
					}

					byte[] block = getTileOrStrip(xTile, yTile, sample);
					ByteReader blockReader = new ByteReader(block);
					blockReader.setByteOrder(reader.getByteOrder());

					for (int y = Math.max(0, window.getMinY() - firstLine); y < Math
							.min(tileHeight,
									tileHeight - (lastLine - window.getMaxY())); y++) {

						for (int x = Math.max(0, window.getMinX() - firstCol); x < Math
								.min(tileWidth,
										tileWidth
												- (lastCol - window.getMaxX())); x++) {

							int pixelOffset = (y * tileWidth + x)
									* bytesPerPixel;
							int valueOffset = pixelOffset
									+ srcSampleOffsets[sampleIndex];
							blockReader.setNextByte(valueOffset);

							// Read the value
							Number value = readValue(blockReader,
									sampleFieldTypes[sampleIndex]);

							if (rasters.hasInterleaveValues()) {
								int windowCoordinate = (y + firstLine - window
										.getMinY())
										* windowWidth
										* samples.length
										+ (x + firstCol - window.getMinX())
										* samples.length + sampleIndex;
								rasters.addToInterleave(windowCoordinate, value);
							}

							if (rasters.hasSampleValues()) {
								int windowCoordinate = (y + firstLine - window
										.getMinY())
										* windowWidth
										+ x
										+ firstCol - window.getMinX();
								rasters.addToSample(sampleIndex,
										windowCoordinate, value);
							}
						}

					}
				}
			}
		}
	}

	/**
	 * Read the value from the reader according to the field type
	 * 
	 * @param reader
	 *            byte reader
	 * @param fieldType
	 *            field type
	 * @return value
	 */
	private Number readValue(ByteReader reader, FieldType fieldType) {

		Number value = null;

		switch (fieldType) {
		case BYTE:
			value = reader.readUnsignedByte();
			break;
		case SHORT:
			value = reader.readUnsignedShort();
			break;
		case LONG:
			value = reader.readUnsignedInt();
			break;
		case SBYTE:
			value = reader.readByte();
			break;
		case SSHORT:
			value = reader.readShort();
			break;
		case SLONG:
			value = reader.readInt();
			break;
		case FLOAT:
			value = reader.readFloat();
			break;
		case DOUBLE:
			value = reader.readDouble();
			break;
		default:
			throw new TiffException("Unsupported raster field type: "
					+ fieldType);
		}

		return value;
	}

	/**
	 * Get the field type for the sample
	 * 
	 * @param sampleIndex
	 *            sample index
	 * @return field type
	 */
	private FieldType getFieldTypeForSample(int sampleIndex) {

		FieldType fieldType = null;

		List<Integer> sampleFormat = getSampleFormat();
		int format = sampleFormat != null ? sampleFormat.get(sampleIndex) : 1;
		int bitsPerSample = getBitsPerSample().get(sampleIndex);
		switch (format) {
		case 1: // unsigned integer data
			switch (bitsPerSample) {
			case 8:
				fieldType = FieldType.BYTE;
				break;
			case 16:
				fieldType = FieldType.SHORT;
				break;
			case 32:
				fieldType = FieldType.LONG;
				break;
			}
			break;
		case 2: // twos complement signed integer data
			switch (bitsPerSample) {
			case 8:
				fieldType = FieldType.SBYTE;
				break;
			case 16:
				fieldType = FieldType.SSHORT;
				break;
			case 32:
				fieldType = FieldType.SLONG;
				break;
			}
			break;
		case 3:
			switch (bitsPerSample) {
			case 32:
				fieldType = FieldType.FLOAT;
				break;
			case 64:
				fieldType = FieldType.DOUBLE;
				break;
			}
			break;
		}

		return fieldType;
	}

	/**
	 * Get the tile or strip for the sample coordinate
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param sample
	 *            sample index
	 * @return bytes
	 */
	private byte[] getTileOrStrip(int x, int y, int sample) {

		byte[] tileOrStrip = null;

		int numTilesPerRow = (int) Math.ceil(getImageWidth().doubleValue()
				/ getTileWidth().doubleValue());
		int numTilesPerCol = (int) Math.ceil(getImageHeight().doubleValue()
				/ getTileHeight().doubleValue());

		int index = 0;
		if (planarConfiguration == 1) {
			index = y * numTilesPerRow + x;
		} else if (planarConfiguration == 2) {
			index = sample * numTilesPerRow * numTilesPerCol + y
					* numTilesPerRow + x;
		}

		// Attempt to pull from the cache
		if (cache != null && cache.containsKey(index)) {
			tileOrStrip = cache.get(index);
		} else {

			// Read and decode the block

			int offset = 0;
			int byteCount = 0;
			if (tiled) {
				offset = getTileOffsets().get(index).intValue();
				byteCount = getTileByteCounts().get(index).intValue();
			} else {
				offset = getStripOffsets().get(index).intValue();
				byteCount = getStripByteCounts().get(index).intValue();
			}

			reader.setNextByte(offset);
			byte[] block = reader.readBytes(byteCount);
			tileOrStrip = decoder.decodeBlock(block);

			// Cache the data
			if (cache != null) {
				cache.put(index, tileOrStrip);
			}
		}

		return tileOrStrip;
	}

	/**
	 * Get the sample byte size
	 * 
	 * @param sampleIndex
	 *            sample index
	 * @return byte size
	 */
	private int getSampleByteSize(int sampleIndex) {
		List<Integer> bitsPerSample = getBitsPerSample();
		if (sampleIndex >= bitsPerSample.size()) {
			throw new TiffException("Sample index " + sampleIndex
					+ " is out of range");
		}
		int bits = bitsPerSample.get(sampleIndex);
		if ((bits % 8) != 0) {
			throw new TiffException("Sample bit-width of " + bits
					+ " is not supported");
		}
		return (bits / 8);
	}

	/**
	 * Calculates the number of bytes for each pixel across all samples. Only
	 * full bytes are supported, an exception is thrown when this is not the
	 * case.
	 * 
	 * @return the bytes per pixel
	 */
	private int getBytesPerPixel() {
		int bitsPerSample = 0;
		List<Integer> bitsPerSamples = getBitsPerSample();
		for (int i = 0; i < bitsPerSamples.size(); i++) {
			int bits = bitsPerSamples.get(i);
			if ((bits % 8) != 0) {
				throw new TiffException("Sample bit-width of " + bits
						+ " is not supported");
			} else if (bits != bitsPerSamples.get(0)) {
				throw new TiffException(
						"Differing size of samples in a pixel are not supported. sample 0 = "
								+ bitsPerSamples.get(0) + ", sample " + i
								+ " = " + bits);
			}
			bitsPerSample += bits;
		}
		return bitsPerSample / 8;
	}

	/**
	 * Get an integer entry value
	 * 
	 * @param fieldTagType
	 *            field tag type
	 * @return integer value
	 */
	private Integer getIntegerEntryValue(FieldTagType fieldTagType) {
		return getEntryValue(fieldTagType);
	}

	/**
	 * Get an number entry value
	 * 
	 * @param fieldTagType
	 *            field tag type
	 * @return number value
	 */
	private Number getNumberEntryValue(FieldTagType fieldTagType) {
		return getEntryValue(fieldTagType);
	}

	/**
	 * Get an integer list entry value
	 * 
	 * @param fieldTagType
	 *            field tag type
	 * @return integer list value
	 */
	private List<Integer> getIntegerListEntryValue(FieldTagType fieldTagType) {
		return getEntryValue(fieldTagType);
	}

	/**
	 * Get the max integer from integer list entry values
	 * 
	 * @param fieldTagType
	 *            field tag type
	 * @return max integer value
	 */
	private Integer getMaxIntegerEntryValue(FieldTagType fieldTagType) {
		Integer maxValue = null;
		List<Integer> values = getIntegerListEntryValue(fieldTagType);
		if (values != null) {
			maxValue = Collections.max(getSampleFormat());
		}
		return maxValue;
	}

	/**
	 * Get a number list entry value
	 * 
	 * @param fieldTagType
	 *            field tag type
	 * @return long list value
	 */
	private List<Number> getNumberListEntryValue(FieldTagType fieldTagType) {
		return getEntryValue(fieldTagType);
	}

	/**
	 * Get a long list entry value
	 * 
	 * @param fieldTagType
	 *            field tag type
	 * @return long list value
	 */
	private List<Long> getLongListEntryValue(FieldTagType fieldTagType) {
		return getEntryValue(fieldTagType);
	}

	/**
	 * Get an entry value
	 * 
	 * @param fieldTagType
	 *            field tag type
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	private <T> T getEntryValue(FieldTagType fieldTagType) {
		T value = null;
		FileDirectoryEntry entry = fieldTagTypeMapping.get(fieldTagType);
		if (entry != null) {
			value = (T) entry.getValues();
		}
		return value;
	}

	/**
	 * Sum the list integer values in the provided range
	 * 
	 * @param values
	 *            integer values
	 * @param start
	 *            inclusive start index
	 * @param end
	 *            exclusive end index
	 * @return sum
	 */
	private int sum(List<Integer> values, int start, int end) {
		int sum = 0;
		for (int i = start; i < end; i++) {
			sum += values.get(i);
		}
		return sum;
	}

}
