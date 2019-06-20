package mil.nga.geopackage.features;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.DateConverter;
import mil.nga.geopackage.io.GeoPackageIOUtils;
import mil.nga.sf.Geometry;
import mil.nga.sf.geojson.Feature;
import mil.nga.sf.geojson.wfs.Link;
import mil.nga.sf.geojson.wfs.WfsFeatureCollection;
import mil.nga.sf.geojson.wfs.WfsFeaturesConverter;

/**
 * WFS 3 Feature Generator
 * 
 * @author osbornb
 */
public abstract class WfsFeatureCoreGenerator extends FeatureCoreGenerator {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger
			.getLogger(WfsFeatureCoreGenerator.class.getName());

	/**
	 * Format pattern
	 */
	private static final Pattern FORMAT_PATTERN = Pattern.compile("f=json");

	/**
	 * Limit pattern
	 */
	private static final Pattern LIMIT_PATTERN = Pattern.compile("limit=\\d+");

	/**
	 * Base server url
	 */
	private final String server;

	/**
	 * Identifier (name) of a specific collection
	 */
	private final String name;

	/**
	 * The optional limit parameter limits the number of items that are
	 * presented in the response document.
	 */
	private Integer limit = null;

	/**
	 * Either a date-time or a period string that adheres to RFC 3339
	 */
	private String time = null;

	/**
	 * Time period string that adheres to RFC 3339
	 */
	private String period = null;

	/**
	 * Total limit of number of items to request
	 */
	private Integer totalLimit = null;

	/**
	 * Download attempts per tile
	 */
	private int downloadAttempts = 1;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tableName
	 *            table name
	 * @param server
	 *            server url
	 * @param name
	 *            collection identifier
	 */
	public WfsFeatureCoreGenerator(GeoPackageCore geoPackage, String tableName,
			String server, String name) {
		super(geoPackage, tableName);
		this.server = server;
		this.name = name;
	}

	/**
	 * Get the server
	 * 
	 * @return server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * Get the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the limit
	 * 
	 * @return limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * Set the limit
	 * 
	 * @param limit
	 *            limit
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * Get the time
	 * 
	 * @return time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Set the time
	 * 
	 * @param time
	 *            time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Set the time
	 * 
	 * @param time
	 *            time
	 */
	public void setTime(Date time) {
		if (time != null) {
			DateConverter dateConverter = DateConverter
					.dateConverter(DateConverter.DATETIME_FORMAT2);
			this.time = dateConverter.stringValue(time);
		} else {
			this.time = null;
		}
	}

	/**
	 * Get the time period
	 * 
	 * @return period
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * Set the time period
	 * 
	 * @param period
	 *            period
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * Set the time period
	 * 
	 * @param period
	 *            period
	 */
	public void setPeriod(Date period) {
		if (period != null) {
			DateConverter dateConverter = DateConverter
					.dateConverter(DateConverter.DATETIME_FORMAT2);
			this.period = dateConverter.stringValue(period);
		} else {
			this.period = null;
		}
	}

	/**
	 * Get the total limit
	 * 
	 * @return total limit
	 */
	public Integer getTotalLimit() {
		return totalLimit;
	}

	/**
	 * Set the total limit
	 * 
	 * @param totalLimit
	 *            total limit
	 */
	public void setTotalLimit(Integer totalLimit) {
		this.totalLimit = totalLimit;
	}

	/**
	 * Get the number of download attempts
	 * 
	 * @return download attempts
	 */
	public int getDownloadAttempts() {
		return downloadAttempts;
	}

	/**
	 * Set the number of download attempts
	 * 
	 * @param downloadAttempts
	 *            download attempts
	 */
	public void setDownloadAttempts(int downloadAttempts) {
		this.downloadAttempts = downloadAttempts;
	}

	/**
	 * Create the feature
	 * 
	 * @param feature
	 *            feature
	 * @throws SQLException
	 *             upon error
	 */
	protected void createFeature(Feature feature) throws SQLException {
		createFeature(feature.getSimpleGeometry(), feature.getProperties());
	}

	/**
	 * Create the feature
	 *
	 * @param geometry
	 *            geometry
	 * @param properties
	 *            properties
	 * @throws SQLException
	 *             upon error
	 */
	protected abstract void createFeature(Geometry geometry,
			Map<String, Object> properties) throws SQLException;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int generateFeatures() throws SQLException {

		StringBuilder urlBuilder = new StringBuilder(server);

		if (!server.endsWith("/")) {
			urlBuilder.append("/");
		}

		urlBuilder.append("collections/");
		urlBuilder.append(name);
		urlBuilder.append("/items");

		boolean params = false;

		if (time != null) {
			if (params) {
				urlBuilder.append("&");
			} else {
				urlBuilder.append("?");
				params = true;
			}
			urlBuilder.append("time=");
			urlBuilder.append(time);
			if (period != null) {
				urlBuilder.append("/");
				urlBuilder.append(period);
			}
		}

		if (boundingBox != null) {
			if (params) {
				urlBuilder.append("&");
			} else {
				urlBuilder.append("?");
				params = true;
			}
			urlBuilder.append("bbox=");
			urlBuilder.append(boundingBox.getMinLongitude());
			urlBuilder.append(",");
			urlBuilder.append(boundingBox.getMinLatitude());
			urlBuilder.append(",");
			urlBuilder.append(boundingBox.getMaxLongitude());
			urlBuilder.append(",");
			urlBuilder.append(boundingBox.getMaxLatitude());
		}

		String urlValue = urlBuilder.toString();

		return generateFeatures(urlValue, 0);
	}

	/**
	 * Generate features
	 * 
	 * @param urlString
	 *            URL
	 * @param currentCount
	 *            current count
	 * @return current result count
	 * @throws SQLException
	 *             upon failure
	 */
	public int generateFeatures(String urlString, int currentCount)
			throws SQLException {

		StringBuilder urlBuilder = new StringBuilder(urlString);

		int paramIndex = urlString.lastIndexOf("?");
		boolean params = paramIndex >= 0 && paramIndex + 1 < urlString.length();

		Integer requestLimit = limit;
		if (totalLimit != null && totalLimit
				- currentCount < (requestLimit != null ? requestLimit
						: WfsFeatureCollection.LIMIT_DEFAULT)) {
			requestLimit = totalLimit - currentCount;
		}
		if (requestLimit != null) {
			Matcher matcher = LIMIT_PATTERN.matcher(urlBuilder.toString());
			if (matcher.find()) {
				urlBuilder = new StringBuilder(
						matcher.replaceFirst("limit=" + requestLimit));
			} else {
				if (params) {
					urlBuilder.append("&");
				} else {
					urlBuilder.append("?");
					params = true;
				}
				urlBuilder.append("limit=");
				urlBuilder.append(requestLimit);
			}
		}

		if (!FORMAT_PATTERN.matcher(urlString).find()) {
			if (params) {
				urlBuilder.append("&");
			} else {
				urlBuilder.append("?");
				params = true;
			}
			urlBuilder.append("f=json");
		}

		urlString = urlBuilder.toString();

		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			throw new GeoPackageException(
					"Failed to download features. URL: " + urlString, e);
		}

		String features = null;

		int attempt = 1;
		while (true) {
			try {
				features = downloadFeatures(urlString, url);
				break;
			} catch (Exception e) {
				if (attempt < downloadAttempts) {
					LOGGER.log(Level.WARNING,
							"Failed to download features after attempt "
									+ attempt + " of " + downloadAttempts
									+ ". URL: " + urlString,
							e);
					attempt++;
				} else {
					throw new GeoPackageException(
							"Failed to download features after "
									+ downloadAttempts + " attempts. URL: "
									+ urlString,
							e);
				}
			}
		}

		if (features != null) {
			WfsFeatureCollection featureCollection = createFeatures(features);

			Integer numberReturned = featureCollection.getNumberReturned();
			if (numberReturned != null) {
				currentCount += numberReturned;
			}

			List<Link> nextLinks = featureCollection.getRelationLinks()
					.get(WfsFeatureCollection.LINK_RELATION_NEXT);
			if (nextLinks != null) {
				for (Link nextLink : nextLinks) {
					if (totalLimit != null && totalLimit <= currentCount) {
						break;
					}
					currentCount = generateFeatures(nextLink.getHref(),
							currentCount);
				}
			}
		}

		return currentCount;
	}

	/**
	 * Download the features
	 * 
	 * @param urlValue
	 *            URL string value
	 * @param url
	 *            URL
	 * @return features response
	 */
	private String downloadFeatures(String urlValue, URL url) {

		String features = null;

		HttpURLConnection connection = null;
		try {
			LOGGER.log(Level.INFO, urlValue);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_MOVED_PERM
					|| responseCode == HttpURLConnection.HTTP_MOVED_TEMP
					|| responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
				String redirect = connection.getHeaderField("Location");
				connection.disconnect();
				url = new URL(redirect);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
			}

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new GeoPackageException(
						"Failed to download features. URL: " + urlValue
								+ ", Response Code: "
								+ connection.getResponseCode()
								+ ", Response Message: "
								+ connection.getResponseMessage());
			}

			InputStream responseStream = connection.getInputStream();
			features = GeoPackageIOUtils.streamString(responseStream);

		} catch (IOException e) {
			throw new GeoPackageException(
					"Failed to download features. URL: " + urlValue, e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return features;
	}

	/**
	 * Create features in the feature DAO from the features value
	 * 
	 * @param features
	 *            features json
	 * @return next links
	 * @throws SQLException
	 *             upon error
	 */
	private WfsFeatureCollection createFeatures(String features)
			throws SQLException {

		WfsFeatureCollection featureCollection = WfsFeaturesConverter
				.toFeatureCollection(features);
		for (Feature feature : featureCollection.getFeatureCollection()
				.getFeatures()) {
			try {
				createFeature(feature);
			} catch (Exception e) {
				LOGGER.log(Level.WARNING,
						"Failed to create feature: " + feature.getId(), e);
			}
		}

		return featureCollection;
	}

}
