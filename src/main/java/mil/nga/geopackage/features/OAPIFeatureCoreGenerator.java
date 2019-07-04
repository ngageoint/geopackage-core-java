package mil.nga.geopackage.features;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.DateConverter;
import mil.nga.geopackage.io.GeoPackageIOUtils;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.oapi.features.json.Collection;
import mil.nga.oapi.features.json.Crs;
import mil.nga.oapi.features.json.FeatureCollection;
import mil.nga.oapi.features.json.FeaturesConverter;
import mil.nga.oapi.features.json.Link;
import mil.nga.sf.geojson.Feature;
import mil.nga.sf.proj.Projection;
import mil.nga.sf.proj.ProjectionConstants;
import mil.nga.sf.proj.ProjectionFactory;
import mil.nga.sf.proj.Projections;

/**
 * OGC API Features Generator
 * 
 * @author osbornb
 */
public abstract class OAPIFeatureCoreGenerator extends FeatureCoreGenerator {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger
			.getLogger(OAPIFeatureCoreGenerator.class.getName());

	/**
	 * Limit pattern
	 */
	protected static final Pattern LIMIT_PATTERN = Pattern
			.compile("limit=\\d+");

	/**
	 * OGC CRS84 Projection
	 */
	protected static final Projection OGC_CRS84 = ProjectionFactory
			.getProjection(ProjectionConstants.AUTHORITY_OGC,
					ProjectionConstants.OGC_CRS84);

	/**
	 * OGC CRS Version
	 */
	protected static final String OGC_VERSION = "1.3";

	/**
	 * EPSG CRS Version
	 */
	protected static final String EPSG_VERSION = "0";

	/**
	 * Default projections
	 */
	protected static final Projections DEFAULT_PROJECTIONS = new Projections();
	static {
		DEFAULT_PROJECTIONS.addProjection(OGC_CRS84);
		DEFAULT_PROJECTIONS.addProjection(EPSG_WGS84);
	}

	/**
	 * Base server url
	 */
	protected final String server;

	/**
	 * Identifier (name) of a specific collection
	 */
	protected final String id;

	/**
	 * The optional limit parameter limits the number of items that are
	 * presented in the response document.
	 */
	protected Integer limit = null;

	/**
	 * Either a date-time or a period string that adheres to RFC 3339
	 */
	protected String time = null;

	/**
	 * Time period string that adheres to RFC 3339
	 */
	protected String period = null;

	/**
	 * Total limit of number of items to request
	 */
	protected Integer totalLimit = null;

	/**
	 * Download attempts per feature request
	 */
	protected int downloadAttempts = GeoPackageProperties.getIntegerProperty(
			PropertyConstants.FEATURE_GENERATOR,
			PropertyConstants.FEATURE_GENERATOR_DOWNLOAD_ATTEMPTS);

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @param tableName
	 *            table name
	 * @param server
	 *            server url
	 * @param id
	 *            collection identifier
	 */
	public OAPIFeatureCoreGenerator(GeoPackageCore geoPackage, String tableName,
			String server, String id) {
		super(geoPackage, tableName);
		this.server = server;
		this.id = id;
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
	 * Get the collection id
	 * 
	 * @return collection id
	 */
	public String getId() {
		return id;
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
	 * {@inheritDoc}
	 */
	@Override
	protected Projection getSrsProjection() {
		Projection srsProjection = null;
		if (OGC_CRS84.equals(projection)) {
			srsProjection = EPSG_WGS84;
		} else {
			srsProjection = super.getSrsProjection();
		}
		return srsProjection;
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
	 * {@inheritDoc}
	 */
	@Override
	public int generateFeatures() throws SQLException {

		String url = buildCollectionRequestUrl();

		Collection collection = collectionRequest(url);

		Projections projections = getProjections(collection);
		if (projection != null && !projections.hasProjection(projection)) {
			LOGGER.log(Level.WARNING,
					"The projection is not advertised by the server. Authority: "
							+ projection.getAuthority() + ", Code: "
							+ projection.getCode());
		}

		StringBuilder urlBuilder = new StringBuilder(url);

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

			if (requestProjection(boundingBoxProjection)) {
				urlBuilder.append("&bbox-crs=");
				urlBuilder.append(getCrs(boundingBoxProjection).toString());
			}
		}

		if (requestProjection(projection)) {
			if (params) {
				urlBuilder.append("&");
			} else {
				urlBuilder.append("?");
				params = true;
			}
			urlBuilder.append("crs=");
			urlBuilder.append(getCrs(projection).toString());
		}

		String urlValue = urlBuilder.toString();

		int count = generateFeatures(urlValue, 0);

		if (progress != null && !progress.isActive()
				&& progress.cleanupOnCancel()) {
			geoPackage.deleteTableQuietly(tableName);
			count = 0;
		}

		return count;
	}

	/**
	 * Build the collection request URL
	 * 
	 * @return url
	 */
	protected String buildCollectionRequestUrl() {

		StringBuilder urlBuilder = new StringBuilder(server);

		if (!server.endsWith("/")) {
			urlBuilder.append("/");
		}

		urlBuilder.append("collections/");
		urlBuilder.append(id);

		return urlBuilder.toString();
	}

	/**
	 * Get the supported projections
	 * 
	 * @return projections
	 */
	public Projections getProjections() {
		return getProjections(buildCollectionRequestUrl());
	}

	/**
	 * Get the supported projections
	 * 
	 * @param url
	 *            URL
	 * @return projections
	 */
	public Projections getProjections(String url) {
		return getProjections(collectionRequest(url));
	}

	/**
	 * Get the supported projections
	 * 
	 * @param collection
	 *            collection
	 * @return projections
	 */
	public Projections getProjections(Collection collection) {

		Projections projections = new Projections();

		if (collection != null) {

			for (String crs : collection.getCrs()) {

				Crs crsValue = new Crs(crs);
				if (crsValue.isValid()) {
					addProjection(projections, crsValue.getAuthority(),
							crsValue.getCode());
				}

			}

		}

		if (projections.isEmpty()) {
			projections = DEFAULT_PROJECTIONS;
		} else if (projections.hasProjection(OGC_CRS84)) {
			projections.addProjection(EPSG_WGS84);
		}

		return projections;
	}

	/**
	 * Determine if the projection should be requested from the server
	 * 
	 * @param projection
	 *            projection
	 * @return true to request the projection (non null and non default)
	 */
	public boolean requestProjection(Projection projection) {
		return projection != null && !isDefaultProjection(projection);
	}

	/**
	 * Check if the projection is a default projection
	 * 
	 * @param projection
	 *            projection
	 * @return true if default projection
	 */
	public boolean isDefaultProjection(Projection projection) {
		return DEFAULT_PROJECTIONS.hasProjection(projection);
	}

	/**
	 * Collection request
	 * 
	 * @return collection
	 */
	public Collection collectionRequest() {
		return collectionRequest(buildCollectionRequestUrl());
	}

	/**
	 * Collection request for the provided URL
	 * 
	 * @param url
	 *            url value
	 * @return collection
	 */
	protected Collection collectionRequest(String url) {

		Collection collection = null;

		String collectionValue = null;
		try {
			collectionValue = urlRequest(url);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Failed to request the collection. url: " + url, e);
		}

		if (collectionValue != null) {

			try {
				collection = FeaturesConverter.toCollection(collectionValue);
			} catch (Exception e) {
				LOGGER.log(Level.WARNING,
						"Failed to translate collection. url: " + url, e);
			}

		}

		return collection;
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
						: FeatureCollection.LIMIT_DEFAULT)) {
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

		String features = null;
		if (isActive()) {
			features = urlRequest(urlBuilder.toString());
		}

		if (features != null && isActive()) {

			FeatureCollection featureCollection = FeaturesConverter
					.toFeatureCollection(features);

			if (currentCount == 0 && progress != null) {
				Integer max = totalLimit;
				Integer numberMatched = featureCollection.getNumberMatched();
				if (numberMatched != null) {
					if (max == null) {
						max = numberMatched;
					} else {
						max = Math.min(max, numberMatched);
					}
				}
				if (max != null) {
					progress.setMax(max);
				}
			}

			createFeatures(featureCollection);

			Integer numberReturned = featureCollection.getNumberReturned();
			if (numberReturned != null) {
				currentCount += numberReturned;
			}

			List<Link> nextLinks = featureCollection.getRelationLinks()
					.get(FeatureCollection.LINK_RELATION_NEXT);
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
	 * URL request
	 * 
	 * @param urlValue
	 *            URL value
	 * @return response string
	 */
	protected String urlRequest(String urlValue) {

		String response = null;

		URL url;
		try {
			url = new URL(urlValue);
		} catch (MalformedURLException e) {
			throw new GeoPackageException("Failed request. URL: " + urlValue,
					e);
		}

		int attempt = 1;
		while (true) {
			try {
				response = urlRequest(urlValue, url);
				break;
			} catch (Exception e) {
				if (attempt < downloadAttempts) {
					LOGGER.log(Level.WARNING,
							"Failed to download features after attempt "
									+ attempt + " of " + downloadAttempts
									+ ". URL: " + urlValue,
							e);
					attempt++;
				} else {
					throw new GeoPackageException(
							"Failed to download features after "
									+ downloadAttempts + " attempts. URL: "
									+ urlValue,
							e);
				}
			}
		}

		return response;
	}

	/**
	 * Perform a URL request
	 * 
	 * @param urlValue
	 *            URL string value
	 * @param url
	 *            URL
	 * @return features response
	 */
	protected String urlRequest(String urlValue, URL url) {

		String response = null;

		HttpURLConnection connection = null;
		try {
			LOGGER.log(Level.INFO, urlValue);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Accept",
					"application/json,application/geo+json");
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
				throw new GeoPackageException("Failed request. URL: " + urlValue
						+ ", Response Code: " + connection.getResponseCode()
						+ ", Response Message: "
						+ connection.getResponseMessage());
			}

			InputStream responseStream = connection.getInputStream();
			response = GeoPackageIOUtils.streamString(responseStream);

		} catch (IOException e) {
			throw new GeoPackageException("Failed request. URL: " + urlValue,
					e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return response;
	}

	/**
	 * Create features in the feature DAO from the features value
	 * 
	 * @param features
	 *            features json
	 * @return feature collection
	 * @throws SQLException
	 *             upon error
	 */
	protected FeatureCollection createFeatures(String features)
			throws SQLException {

		FeatureCollection featureCollection = FeaturesConverter
				.toFeatureCollection(features);

		createFeatures(featureCollection);

		return featureCollection;
	}

	/**
	 * Create features from the feature collection
	 * 
	 * @param featureCollection
	 *            feature collection
	 * @return features created
	 */
	protected int createFeatures(FeatureCollection featureCollection) {

		int count = 0;

		geoPackage.beginTransaction();
		try {

			for (Feature feature : featureCollection.getFeatureCollection()
					.getFeatures()) {

				if (!isActive()) {
					break;
				}

				try {
					createFeature(feature);
					count++;

					if (progress != null) {
						progress.addProgress(1);
					}
				} catch (Exception e) {
					LOGGER.log(Level.WARNING,
							"Failed to create feature: " + feature.getId(), e);
				}

				if (count > 0 && count % transactionLimit == 0) {
					geoPackage.commit();
				}

			}

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to create features", e);
			geoPackage.failTransaction();
		} finally {
			geoPackage.endTransaction();
		}

		Integer numberReturned = featureCollection.getNumberReturned();
		if (numberReturned != null && numberReturned != count) {
			LOGGER.log(Level.WARNING,
					"Feature Collection number returned does not match number of features created. Number Returned: "
							+ numberReturned + ", Created: " + count);
		}
		featureCollection.setNumberReturned(count);

		return count;
	}

	/**
	 * Get the CRS from the projection
	 * 
	 * @param projection
	 *            projection
	 * @return crs
	 */
	protected Crs getCrs(Projection projection) {
		String version = null;
		switch (projection.getAuthority()) {
		case ProjectionConstants.AUTHORITY_OGC:
			version = OGC_VERSION;
			break;
		default:
			version = EPSG_VERSION;
		}
		return new Crs(projection.getAuthority(), version,
				projection.getCode());
	}

}
