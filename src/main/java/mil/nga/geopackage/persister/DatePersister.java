package mil.nga.geopackage.persister;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	 * Date format
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(DatePersister.class
			.getName());

	/**
	 * Persister singleton
	 */
	private static final DatePersister singleTon = new DatePersister();

	/**
	 * Simple date format
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			DATE_FORMAT);
	static {
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

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
	public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
		Object stringDate = null;
		if (javaObject != null && javaObject instanceof Date) {
			stringDate = sdf.format((Date) javaObject);
		}
		return stringDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
		Object javaDate = null;
		if (sqlArg != null && sqlArg instanceof String) {
			try {
				javaDate = sdf.parse((String) sqlArg);
			} catch (ParseException e) {
				logger.log(Level.SEVERE, "Failed to parse date string: "
						+ sqlArg + ", expected format: " + DATE_FORMAT, e);
			}
		}
		return javaDate;
	}

}
