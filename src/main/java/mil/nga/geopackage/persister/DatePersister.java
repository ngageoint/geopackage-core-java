package mil.nga.geopackage.persister;

import java.sql.SQLException;
import java.util.Date;

import mil.nga.geopackage.db.DateConverter;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

/**
 * Date Persister for OrmLite to translate between a Java Date and a UTC String
 * 
 * @author osbornb
 * @since 1.2.1
 */
public class DatePersister extends StringType {

	/**
	 * Persister singleton
	 */
	private static final DatePersister singleTon = new DatePersister();

	/**
	 * Date Converter
	 */
	private static final DateConverter dateConverter = DateConverter
			.dateTimeConverter();

	/**
	 * Constructor
	 */
	private DatePersister() {
		super(SqlType.STRING, new Class<?>[] { Date.class });
	}

	/**
	 * Get the singleton instance
	 * 
	 * @return instance
	 */
	public static DatePersister getSingleton() {
		return singleTon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object parseDefaultString(FieldType fieldType, String defaultStr) {
		Object defaultValue = null;
		if (DateConverter.DATETIME_FORMAT.equals(defaultStr)) {
			defaultValue = javaToSqlArg(null, new Date());
		}
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
		Object stringDate = null;
		if (javaObject != null && javaObject instanceof Date) {
			stringDate = dateConverter.stringValue((Date) javaObject);
		}
		return stringDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
			throws SQLException {
		Object javaDate = null;
		if (sqlArg != null && sqlArg instanceof String) {
			try {
				javaDate = dateConverter.dateValue((String) sqlArg);
			} catch (Exception e) {
				throw new SQLException(
						"Failed to parse date string: " + sqlArg, e);
			}
		}
		return javaDate;
	}

}
