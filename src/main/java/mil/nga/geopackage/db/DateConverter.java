package mil.nga.geopackage.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import mil.nga.geopackage.GeoPackageException;

/**
 * Date converter between database date formats and date objects
 * 
 * @author osbornb
 * @since 1.3.0
 */
public class DateConverter {

	/**
	 * Date format
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Date Time format
	 */
	public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	/**
	 * Secondary Date Time format w/o milliseconds for parsing string dates
	 */
	public static final String DATETIME_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/**
	 * Get a date converter for the data type
	 * 
	 * @param type
	 *            data type
	 * @return date converter
	 */
	public static DateConverter converter(GeoPackageDataType type) {

		DateConverter converter = null;

		switch (type) {
		case DATE:
			converter = dateConverter();
			break;
		case DATETIME:
			converter = dateTimeConverter();
			break;
		default:
			throw new GeoPackageException("Not a date data type: " + type);
		}

		return converter;
	}

	/**
	 * Get a date converter
	 * 
	 * @return date converter
	 */
	public static DateConverter dateConverter() {
		return new DateConverter(DATE_FORMAT);
	}

	/**
	 * Get a date time converter
	 * 
	 * @return date converter
	 */
	public static DateConverter dateTimeConverter() {
		return new DateConverter(DATETIME_FORMAT, DATETIME_FORMAT2);
	}

	/**
	 * Simple date formatters
	 */
	private final List<SimpleDateFormat> formatters = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param format
	 *            date format
	 */
	private DateConverter(String... formats) {
		for (String format : formats) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatters.add(sdf);
		}
	}

	/**
	 * Get the formatted string date value of the date
	 * 
	 * @param date
	 *            date
	 * @return formatted string date
	 */
	public String stringValue(Date date) {
		String value = null;
		if (date != null) {
			SimpleDateFormat sdf = formatters.get(0);
			synchronized (sdf) {
				value = sdf.format(date);
			}
		}
		return value;
	}

	/**
	 * Get the date value of the formatted string date
	 * 
	 * @param date
	 *            formatted string date
	 * @return date
	 */
	public Date dateValue(String date) {
		Date value = null;
		if (date != null) {

			// Try each simple date formatter in order
			ParseException exception = null;
			for (SimpleDateFormat sdf : formatters) {
				try {
					synchronized (sdf) {
						value = sdf.parse(date);
					}
					break;
				} catch (ParseException e) {
					if (exception == null) {
						exception = e;
					}
				}
			}

			// If no value could be parsed throw the first expected parse
			// format exception
			if (value == null) {
				throw new GeoPackageException("Failed to parse date string: "
						+ date, exception);
			}

		}
		return value;
	}

}
