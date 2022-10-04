package mil.nga.geopackage.dgiwg;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import mil.nga.geopackage.db.DateConverter;
import mil.nga.geopackage.io.GeoPackageIOUtils;

/**
 * DGIWG (Defence Geospatial Information Working Group) GeoPackage File Name
 * 
 * @author osbornb
 * @since 6.5.1
 */
public class GeoPackageFileName {

	/**
	 * Delimiter between elements
	 */
	public static final String DELIMITER_ELEMENTS = "_";

	/**
	 * Delimiter between words
	 */
	public static final String DELIMITER_WORDS = "-";

	/**
	 * Version prefix
	 */
	public static final String VERSION_PREFIX = "v";

	/**
	 * Date format
	 */
	public static final String DATE_FORMAT = "ddMMMyyyy";

	/**
	 * GeoPackage producer
	 */
	private String producer;

	/**
	 * Data Product(s)
	 */
	private String dataProduct;

	/**
	 * Geographic Coverage Area
	 */
	private String geographicCoverageArea;

	/**
	 * Zoom Levels
	 */
	private String zoomLevels;

	/**
	 * Zoom Level Part 1, min zoom or scale map units
	 */
	private Integer zoomLevel1;

	/**
	 * Zoom Level Part 2, max zoom or scale surface units
	 */
	private Integer zoomLevel2;

	/**
	 * Version
	 */
	private String version;

	/**
	 * Major version
	 */
	private Integer majorVersion;

	/**
	 * Minor version
	 */
	private Integer minorVersion;

	/**
	 * GeoPackage Creation Date
	 */
	private String creationDateText;

	/**
	 * GeoPackage Creation Date
	 */
	private Date creationDate;

	/**
	 * Optional additional elements, for mission or agency specific use
	 */
	private List<String> additional;

	/**
	 * Constructor
	 * 
	 * @param file
	 *            GeoPackage file
	 */
	public GeoPackageFileName(File file) {
		this(file.getName());
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            GeoPackage file name
	 */
	public GeoPackageFileName(String name) {

		name = GeoPackageIOUtils.getPathFileNameWithoutExtension(name);

		String[] elements = name.split(DELIMITER_ELEMENTS);

		for (int i = 0; i < elements.length; i++) {

			String value = elements[i];

			switch (i) {

			case 0:
				producer = delimitersToSpaces(value);
				break;

			case 1:
				dataProduct = delimitersToSpaces(value);
				break;

			case 2:
				geographicCoverageArea = delimitersToSpaces(value);
				break;

			case 3:
				setZoomLevels(value);
				break;

			case 4:
				setVersion(value);
				break;

			case 5:
				setCreationDateText(value);
				break;

			case 6:
				additional = new ArrayList<>();

			default:
				additional.add(delimitersToSpaces(value));
				break;
			}

		}

	}

	/**
	 * Get the GeoPackage producer
	 * 
	 * @return producer
	 */
	public String getProducer() {
		return producer;
	}

	/**
	 * Set the GeoPackage producer
	 * 
	 * @param producer
	 *            producer
	 */
	public void setProducer(String producer) {
		this.producer = producer;
	}

	/**
	 * Get the data product
	 * 
	 * @return data product
	 */
	public String getDataProduct() {
		return dataProduct;
	}

	/**
	 * Set the data product
	 * 
	 * @param dataProduct
	 *            data product
	 */
	public void setDataProduct(String dataProduct) {
		this.dataProduct = dataProduct;
	}

	/**
	 * Get the geographic coverage area
	 * 
	 * @return geographic coverage area
	 */
	public String getGeographicCoverageArea() {
		return geographicCoverageArea;
	}

	/**
	 * Set the geographic coverage area
	 * 
	 * @param geographicCoverageArea
	 *            geographic coverage area
	 */
	public void setGeographicCoverageArea(String geographicCoverageArea) {
		this.geographicCoverageArea = geographicCoverageArea;
	}

	/**
	 * Get the zoom levels
	 * 
	 * @return zoom levels
	 */
	public String getZoomLevels() {
		return zoomLevels;
	}

	/**
	 * Set the zoom levels
	 * 
	 * @param zoomLevels
	 *            zoom levels
	 */
	public void setZoomLevels(String zoomLevels) {
		this.zoomLevels = zoomLevels;

		String[] parts = zoomLevels.split(":|" + DELIMITER_WORDS);
		if (parts.length == 2) {

			String zoom1 = parts[0];
			String zoom2 = parts[1];

			Integer zoomLevel1 = toInteger(zoom1);
			Integer zoomLevel2 = toInteger(zoom2);

			if (zoomLevel1 != null && zoomLevel2 != null) {
				this.zoomLevel1 = zoomLevel1;
				this.zoomLevel2 = zoomLevel2;
				String delimiter = ":";
				if (zoomLevel1 >= 0 && zoomLevel2 <= 28) {
					delimiter = DELIMITER_WORDS;
				}
				this.zoomLevels = zoomLevel1 + delimiter + zoomLevel2;
			} else {
				this.zoomLevels = delimitersToSpaces(zoomLevels);
			}

		} else {
			this.zoomLevels = delimitersToSpaces(zoomLevels);
		}

	}

	/**
	 * Get the zoom level part 1, max zoom or scale map units
	 * 
	 * @return zoom level part 1
	 */
	public Integer getZoomLevel1() {
		return zoomLevel1;
	}

	/**
	 * Has a zoom level part 1, max zoom or scale map units
	 * 
	 * @return true if has zoom level part 1
	 */
	public boolean hasZoomLevel1() {
		return zoomLevel1 != null;
	}

	/**
	 * Get the zoom level part 2, max zoom or scale surface units
	 * 
	 * @return zoom level part 2
	 */
	public Integer getZoomLevel2() {
		return zoomLevel2;
	}

	/**
	 * Has a zoom level part 2, max zoom or scale surface units
	 * 
	 * @return true if has zoom level part 2
	 */
	public boolean hasZoomLevel2() {
		return zoomLevel2 != null;
	}

	/**
	 * Set the zoom level range
	 * 
	 * @param minZoom
	 *            min zoom level
	 * @param maxZoom
	 *            max zoom level
	 */
	public void setZoomLevelRange(int minZoom, int maxZoom) {
		this.zoomLevel1 = minZoom;
		this.zoomLevel2 = maxZoom;
		this.zoomLevels = minZoom + DELIMITER_WORDS + maxZoom;
	}

	/**
	 * Set the zoom level map scale
	 * 
	 * @param mapUnits
	 *            scale map units
	 * @param surfaceUnits
	 *            scale surface units
	 */
	public void setZoomLevelScale(int mapUnits, int surfaceUnits) {
		this.zoomLevel1 = mapUnits;
		this.zoomLevel2 = surfaceUnits;
		this.zoomLevels = zoomLevel1 + ":" + zoomLevel2;
	}

	/**
	 * Get the version
	 * 
	 * @return version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the version
	 * 
	 * @param version
	 *            version
	 */
	public void setVersion(String version) {

		String[] parts = version.split("\\.|" + DELIMITER_WORDS);
		if (parts.length == 2) {

			String major = parts[0];
			if (major.startsWith(VERSION_PREFIX)) {
				major = major.substring(1);
			}
			String minor = parts[1];

			Integer majorVersion = toInteger(major);
			Integer minorVersion = toInteger(minor);

			if (majorVersion != null && minorVersion != null) {
				this.majorVersion = majorVersion;
				this.minorVersion = minorVersion;
				this.version = majorVersion + "." + minorVersion;
			} else {
				this.version = delimitersToSpaces(version);
			}

		} else {
			this.version = delimitersToSpaces(version);
		}

	}

	/**
	 * Get the major version
	 * 
	 * @return major version
	 */
	public Integer getMajorVersion() {
		return majorVersion;
	}

	/**
	 * Has a major version
	 * 
	 * @return true if has major version
	 */
	public boolean hasMajorVersion() {
		return majorVersion != null;
	}

	/**
	 * Get the minor version
	 * 
	 * @return minor version
	 */
	public Integer getMinorVersion() {
		return minorVersion;
	}

	/**
	 * Has a minor version
	 * 
	 * @return true if has minor version
	 */
	public boolean hasMinorVersion() {
		return minorVersion != null;
	}

	/**
	 * Set the version
	 * 
	 * @param majorVersion
	 *            major version
	 * @param minorVersion
	 *            minor version
	 */
	public void setVersion(int majorVersion, int minorVersion) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.version = this.majorVersion + "." + this.minorVersion;
	}

	/**
	 * Get the creation date text
	 * 
	 * @return creation date text
	 */
	public String getCreationDateText() {
		return creationDateText;
	}

	/**
	 * Set the creation date text
	 * 
	 * @param creationDateText
	 *            creation date text
	 */
	public void setCreationDateText(String creationDateText) {
		this.creationDateText = creationDateText;
		DateConverter converter = getDateConverter();
		creationDate = converter.dateValue(creationDateText);
	}

	/**
	 * Get the creation date
	 * 
	 * @return creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Set the creation date
	 * 
	 * @param creationDate
	 *            creation date
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		DateConverter converter = getDateConverter();
		this.creationDateText = converter.stringValue(creationDate);
	}

	/**
	 * Get additional elements
	 * 
	 * @return additional elements
	 */
	public List<String> getAdditional() {
		return additional;
	}

	/**
	 * Has additional elements
	 * 
	 * @return true if has additional elements
	 */
	public boolean hasAdditional() {
		return additional != null && additional.isEmpty();
	}

	/**
	 * Set additional elements
	 * 
	 * @param additional
	 *            additional elements
	 */
	public void setAdditional(List<String> additional) {
		this.additional = additional;
	}

	/**
	 * Add an additional element
	 * 
	 * @param additional
	 *            additional element
	 */
	public void addAdditional(String additional) {
		if (this.additional == null) {
			this.additional = new ArrayList<>();
		}
		this.additional.add(additional);
	}

	/**
	 * Determine if valid
	 * 
	 * @return true if valid
	 */
	public boolean isValid() {
		return producer != null && dataProduct != null
				&& geographicCoverageArea != null && zoomLevels != null
				&& version != null && creationDateText != null;
	}

	/**
	 * Replace word delimiters with spaces
	 * 
	 * @param value
	 *            delimited value
	 * @return space replaced value
	 */
	public String delimitersToSpaces(String value) {
		return value.replaceAll(DELIMITER_WORDS, " ").trim();
	}

	/**
	 * Attempt to convert a value to an integer
	 * 
	 * @param value
	 *            string value
	 * @return integer or null
	 */
	private Integer toInteger(String value) {
		Integer integer = null;
		if (value != null) {
			Scanner scanner = new Scanner(value);
			if (scanner.hasNextInt()) {
				scanner.next();
				if (!scanner.hasNext()) {
					integer = Integer.parseInt(value);
				}
			}
			scanner.close();
		}
		return integer;
	}

	/**
	 * Get a date converter
	 * 
	 * @return date converter
	 */
	private DateConverter getDateConverter() {
		DateConverter converter = new DateConverter(DATE_FORMAT,
				DateConverter.DATE_FORMAT, DateConverter.DATE_FORMAT2);
		converter.setExpected(false);
		return converter;
	}

	/**
	 * Add a value to the string builder
	 * 
	 * @param builder
	 *            string builder
	 * @param value
	 *            string value
	 */
	private void addValue(StringBuilder builder, String value) {
		if (value != null) {
			addDelimiter(builder);
			builder.append(value.trim().replaceAll(" ", DELIMITER_WORDS));
		}
	}

	/**
	 * Add an element delimiter
	 * 
	 * @param builder
	 *            string builder
	 */
	private void addDelimiter(StringBuilder builder) {
		if (builder.length() > 0) {
			builder.append(DELIMITER_ELEMENTS);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		addValue(builder, producer);
		addValue(builder, dataProduct);
		addValue(builder, geographicCoverageArea);
		if (zoomLevel1 != null) {
			addDelimiter(builder);
			builder.append(zoomLevel1);
			if (zoomLevel2 != null) {
				builder.append(DELIMITER_WORDS);
				builder.append(zoomLevel2);
			}
		} else {
			addValue(builder, zoomLevels);
		}
		if (majorVersion != null) {
			addDelimiter(builder);
			builder.append(VERSION_PREFIX);
			builder.append(majorVersion);
			if (minorVersion != null) {
				builder.append(DELIMITER_WORDS);
				builder.append(minorVersion);
			}
		} else {
			addValue(builder, version);
		}
		addValue(builder, creationDateText);
		if (additional != null) {
			for (String value : additional) {
				addValue(builder, value);
			}
		}
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + toString().hashCode();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoPackageFileName other = (GeoPackageFileName) obj;
		if (!toString().equals(other.toString()))
			return false;
		return true;
	}

}
