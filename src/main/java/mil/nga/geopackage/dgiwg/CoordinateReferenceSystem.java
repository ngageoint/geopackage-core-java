package mil.nga.geopackage.dgiwg;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import mil.nga.crs.CRSType;
import mil.nga.crs.geo.GeoDatums;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.contents.ContentsDataType;
import mil.nga.geopackage.srs.SpatialReferenceSystem;
import mil.nga.proj.ProjectionConstants;

/**
 * DGIWG (Defence Geospatial Information Working Group) Coordinate Reference
 * Systems
 * 
 * @author osbornb
 * @since 6.5.1
 */
public enum CoordinateReferenceSystem {

	/**
	 * WGS 84 / World Mercator
	 */
	EPSG_3395(3395, "WGS 84 / World Mercator", CRSType.PROJECTED, 2,
			WellKnownText.EPSG_3395,
			"Euro-centric view of world excluding polar areas for very small scale mapping",
			BoundingBox.worldWebMercator(),
			new BoundingBox(-ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH,
					-80.0, ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH,
					84.0),
			DataType.TILES_2D, DataType.TILES_3D),

	/**
	 * WGS 84 / Pseudo-Mercator
	 */
	EPSG_3857(3857, "WGS 84 / Pseudo-Mercator", CRSType.PROJECTED, 2,
			WellKnownText.EPSG_3857,
			"Uses spherical development of ellipsoidal coordinates. This should only be used for visualization purposes.",
			BoundingBox.worldWebMercator(),
			BoundingBox.worldWGS84WithWebMercatorLimits(), DataType.TILES_2D),

	/**
	 * WGS 84 Geographic 2D
	 */
	EPSG_4326(4326, "WGS 84 Geographic 2D", CRSType.GEODETIC, 2,
			WellKnownText.EPSG_4326,
			"Horizontal component of 3D system. Used by the GPS satellite navigation system and for NATO military geodetic surveying.",
			DataType.TILES_3D, DataType.FEATURES_2D),

	/**
	 * WGS 84 Geographic 3D
	 */
	EPSG_4979(4979, "WGS 84 Geographic 3D", CRSType.GEODETIC, 3,
			WellKnownText.EPSG_4979,
			"Used by the GPS satellite navigation system and for NATO military geodetic surveying.",
			DataType.TILES_3D, DataType.FEATURES_3D),

	/**
	 * WGS 84 / UPS North (E,N)
	 */
	EPSG_5041(5041, "WGS 84 / UPS North (E,N)", CRSType.PROJECTED, 2,
			WellKnownText.EPSG_5041, "Military mapping by NATO north of 60° N",
			new BoundingBox(-14440759.350252, -14440759.350252, 18440759.350252,
					18440759.350252),
			new BoundingBox(-ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH,
					60.0, ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH,
					ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT),
			DataType.TILES_2D, DataType.TILES_3D),

	/**
	 * WGS 84 / UPS South (E,N)
	 */
	EPSG_5042(5042, "WGS 84 / UPS South (E,N)", CRSType.PROJECTED, 2,
			WellKnownText.EPSG_5042, "Military mapping by NATO south of 60° S",
			new BoundingBox(-14440759.350252, -14440759.350252, 18440759.350252,
					18440759.350252),
			new BoundingBox(-ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH,
					-ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT,
					ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH, -60.0),
			DataType.TILES_2D, DataType.TILES_3D),

	/**
	 * WGS84 4326 + EGM2008 height 3855
	 */
	EPSG_9518(9518, "WGS84 4326 + EGM2008 height 3855", CRSType.COMPOUND, 3,
			WellKnownText.EPSG_9518,
			"Geodetic position based on the World Geodetic System 1984 (WGS 84), extended by height position based on the Earth Gravity Model 2008 (EGM08).",
			DataType.FEATURES_3D),

	/**
	 * WGS 84 / UTM zone 1N
	 */
	EPSG_32601(32601),

	/**
	 * WGS 84 / UTM zone 2N
	 */
	EPSG_32602(32602),

	/**
	 * WGS 84 / UTM zone 3N
	 */
	EPSG_32603(32603),

	/**
	 * WGS 84 / UTM zone 4N
	 */
	EPSG_32604(32604),

	/**
	 * WGS 84 / UTM zone 5N
	 */
	EPSG_32605(32605),

	/**
	 * WGS 84 / UTM zone 6N
	 */
	EPSG_32606(32606),

	/**
	 * WGS 84 / UTM zone 7N
	 */
	EPSG_32607(32607),

	/**
	 * WGS 84 / UTM zone 8N
	 */
	EPSG_32608(32608),

	/**
	 * WGS 84 / UTM zone 9N
	 */
	EPSG_32609(32609),

	/**
	 * WGS 84 / UTM zone 10N
	 */
	EPSG_32610(32610),

	/**
	 * WGS 84 / UTM zone 11N
	 */
	EPSG_32611(32611),

	/**
	 * WGS 84 / UTM zone 12N
	 */
	EPSG_32612(32612),

	/**
	 * WGS 84 / UTM zone 13N
	 */
	EPSG_32613(32613),

	/**
	 * WGS 84 / UTM zone 14N
	 */
	EPSG_32614(32614),

	/**
	 * WGS 84 / UTM zone 15N
	 */
	EPSG_32615(32615),

	/**
	 * WGS 84 / UTM zone 16N
	 */
	EPSG_32616(32616),

	/**
	 * WGS 84 / UTM zone 17N
	 */
	EPSG_32617(32617),

	/**
	 * WGS 84 / UTM zone 18N
	 */
	EPSG_32618(32618),

	/**
	 * WGS 84 / UTM zone 19N
	 */
	EPSG_32619(32619),

	/**
	 * WGS 84 / UTM zone 20N
	 */
	EPSG_32620(32620),

	/**
	 * WGS 84 / UTM zone 21N
	 */
	EPSG_32621(32621),

	/**
	 * WGS 84 / UTM zone 22N
	 */
	EPSG_32622(32622),

	/**
	 * WGS 84 / UTM zone 23N
	 */
	EPSG_32623(32623),

	/**
	 * WGS 84 / UTM zone 24N
	 */
	EPSG_32624(32624),

	/**
	 * WGS 84 / UTM zone 25N
	 */
	EPSG_32625(32625),

	/**
	 * WGS 84 / UTM zone 26N
	 */
	EPSG_32626(32626),

	/**
	 * WGS 84 / UTM zone 27N
	 */
	EPSG_32627(32627),

	/**
	 * WGS 84 / UTM zone 28N
	 */
	EPSG_32628(32628),

	/**
	 * WGS 84 / UTM zone 29N
	 */
	EPSG_32629(32629),

	/**
	 * WGS 84 / UTM zone 30N
	 */
	EPSG_32630(32630),

	/**
	 * WGS 84 / UTM zone 31N
	 */
	EPSG_32631(32631),

	/**
	 * WGS 84 / UTM zone 32N
	 */
	EPSG_32632(32632),

	/**
	 * WGS 84 / UTM zone 33N
	 */
	EPSG_32633(32633),

	/**
	 * WGS 84 / UTM zone 34N
	 */
	EPSG_32634(32634),

	/**
	 * WGS 84 / UTM zone 35N
	 */
	EPSG_32635(32635),

	/**
	 * WGS 84 / UTM zone 36N
	 */
	EPSG_32636(32636),

	/**
	 * WGS 84 / UTM zone 37N
	 */
	EPSG_32637(32637),

	/**
	 * WGS 84 / UTM zone 38N
	 */
	EPSG_32638(32638),

	/**
	 * WGS 84 / UTM zone 39N
	 */
	EPSG_32639(32639),

	/**
	 * WGS 84 / UTM zone 40N
	 */
	EPSG_32640(32640),

	/**
	 * WGS 84 / UTM zone 41N
	 */
	EPSG_32641(32641),

	/**
	 * WGS 84 / UTM zone 42N
	 */
	EPSG_32642(32642),

	/**
	 * WGS 84 / UTM zone 43N
	 */
	EPSG_32643(32643),

	/**
	 * WGS 84 / UTM zone 44N
	 */
	EPSG_32644(32644),

	/**
	 * WGS 84 / UTM zone 45N
	 */
	EPSG_32645(32645),

	/**
	 * WGS 84 / UTM zone 46N
	 */
	EPSG_32646(32646),

	/**
	 * WGS 84 / UTM zone 47N
	 */
	EPSG_32647(32647),

	/**
	 * WGS 84 / UTM zone 48N
	 */
	EPSG_32648(32648),

	/**
	 * WGS 84 / UTM zone 49N
	 */
	EPSG_32649(32649),

	/**
	 * WGS 84 / UTM zone 50N
	 */
	EPSG_32650(32650),

	/**
	 * WGS 84 / UTM zone 51N
	 */
	EPSG_32651(32651),

	/**
	 * WGS 84 / UTM zone 52N
	 */
	EPSG_32652(32652),

	/**
	 * WGS 84 / UTM zone 53N
	 */
	EPSG_32653(32653),

	/**
	 * WGS 84 / UTM zone 54N
	 */
	EPSG_32654(32654),

	/**
	 * WGS 84 / UTM zone 55N
	 */
	EPSG_32655(32655),

	/**
	 * WGS 84 / UTM zone 56N
	 */
	EPSG_32656(32656),

	/**
	 * WGS 84 / UTM zone 57N
	 */
	EPSG_32657(32657),

	/**
	 * WGS 84 / UTM zone 58N
	 */
	EPSG_32658(32658),

	/**
	 * WGS 84 / UTM zone 59N
	 */
	EPSG_32659(32659),

	/**
	 * WGS 84 / UTM zone 60N
	 */
	EPSG_32660(32660),

	/**
	 * WGS 84 / UTM zone 1S
	 */
	EPSG_32701(32701),

	/**
	 * WGS 84 / UTM zone 2S
	 */
	EPSG_32702(32702),

	/**
	 * WGS 84 / UTM zone 3S
	 */
	EPSG_32703(32703),

	/**
	 * WGS 84 / UTM zone 4S
	 */
	EPSG_32704(32704),

	/**
	 * WGS 84 / UTM zone 5S
	 */
	EPSG_32705(32705),

	/**
	 * WGS 84 / UTM zone 6S
	 */
	EPSG_32706(32706),

	/**
	 * WGS 84 / UTM zone 7S
	 */
	EPSG_32707(32707),

	/**
	 * WGS 84 / UTM zone 8S
	 */
	EPSG_32708(32708),

	/**
	 * WGS 84 / UTM zone 9S
	 */
	EPSG_32709(32709),

	/**
	 * WGS 84 / UTM zone 10S
	 */
	EPSG_32710(32710),

	/**
	 * WGS 84 / UTM zone 11S
	 */
	EPSG_32711(32711),

	/**
	 * WGS 84 / UTM zone 12S
	 */
	EPSG_32712(32712),

	/**
	 * WGS 84 / UTM zone 13S
	 */
	EPSG_32713(32713),

	/**
	 * WGS 84 / UTM zone 14S
	 */
	EPSG_32714(32714),

	/**
	 * WGS 84 / UTM zone 15S
	 */
	EPSG_32715(32715),

	/**
	 * WGS 84 / UTM zone 16S
	 */
	EPSG_32716(32716),

	/**
	 * WGS 84 / UTM zone 17S
	 */
	EPSG_32717(32717),

	/**
	 * WGS 84 / UTM zone 18S
	 */
	EPSG_32718(32718),

	/**
	 * WGS 84 / UTM zone 19S
	 */
	EPSG_32719(32719),

	/**
	 * WGS 84 / UTM zone 20S
	 */
	EPSG_32720(32720),

	/**
	 * WGS 84 / UTM zone 21S
	 */
	EPSG_32721(32721),

	/**
	 * WGS 84 / UTM zone 22S
	 */
	EPSG_32722(32722),

	/**
	 * WGS 84 / UTM zone 23S
	 */
	EPSG_32723(32723),

	/**
	 * WGS 84 / UTM zone 24S
	 */
	EPSG_32724(32724),

	/**
	 * WGS 84 / UTM zone 25S
	 */
	EPSG_32725(32725),

	/**
	 * WGS 84 / UTM zone 26S
	 */
	EPSG_32726(32726),

	/**
	 * WGS 84 / UTM zone 27S
	 */
	EPSG_32727(32727),

	/**
	 * WGS 84 / UTM zone 28S
	 */
	EPSG_32728(32728),

	/**
	 * WGS 84 / UTM zone 29S
	 */
	EPSG_32729(32729),

	/**
	 * WGS 84 / UTM zone 30S
	 */
	EPSG_32730(32730),

	/**
	 * WGS 84 / UTM zone 31S
	 */
	EPSG_32731(32731),

	/**
	 * WGS 84 / UTM zone 32S
	 */
	EPSG_32732(32732),

	/**
	 * WGS 84 / UTM zone 33S
	 */
	EPSG_32733(32733),

	/**
	 * WGS 84 / UTM zone 34S
	 */
	EPSG_32734(32734),

	/**
	 * WGS 84 / UTM zone 35S
	 */
	EPSG_32735(32735),

	/**
	 * WGS 84 / UTM zone 36S
	 */
	EPSG_32736(32736),

	/**
	 * WGS 84 / UTM zone 37S
	 */
	EPSG_32737(32737),

	/**
	 * WGS 84 / UTM zone 38S
	 */
	EPSG_32738(32738),

	/**
	 * WGS 84 / UTM zone 39S
	 */
	EPSG_32739(32739),

	/**
	 * WGS 84 / UTM zone 40S
	 */
	EPSG_32740(32740),

	/**
	 * WGS 84 / UTM zone 41S
	 */
	EPSG_32741(32741),

	/**
	 * WGS 84 / UTM zone 42S
	 */
	EPSG_32742(32742),

	/**
	 * WGS 84 / UTM zone 43S
	 */
	EPSG_32743(32743),

	/**
	 * WGS 84 / UTM zone 44S
	 */
	EPSG_32744(32744),

	/**
	 * WGS 84 / UTM zone 45S
	 */
	EPSG_32745(32745),

	/**
	 * WGS 84 / UTM zone 46S
	 */
	EPSG_32746(32746),

	/**
	 * WGS 84 / UTM zone 47S
	 */
	EPSG_32747(32747),

	/**
	 * WGS 84 / UTM zone 48S
	 */
	EPSG_32748(32748),

	/**
	 * WGS 84 / UTM zone 49S
	 */
	EPSG_32749(32749),

	/**
	 * WGS 84 / UTM zone 50S
	 */
	EPSG_32750(32750),

	/**
	 * WGS 84 / UTM zone 51S
	 */
	EPSG_32751(32751),

	/**
	 * WGS 84 / UTM zone 52S
	 */
	EPSG_32752(32752),

	/**
	 * WGS 84 / UTM zone 53S
	 */
	EPSG_32753(32753),

	/**
	 * WGS 84 / UTM zone 54S
	 */
	EPSG_32754(32754),

	/**
	 * WGS 84 / UTM zone 55S
	 */
	EPSG_32755(32755),

	/**
	 * WGS 84 / UTM zone 56S
	 */
	EPSG_32756(32756),

	/**
	 * WGS 84 / UTM zone 57S
	 */
	EPSG_32757(32757),

	/**
	 * WGS 84 / UTM zone 58S
	 */
	EPSG_32758(32758),

	/**
	 * WGS 84 / UTM zone 59S
	 */
	EPSG_32759(32759),

	/**
	 * WGS 84 / UTM zone 60S
	 */
	EPSG_32760(32760);

	/**
	 * Map between authority and codes to Coordinate Reference Systems
	 */
	private static Map<String, Map<Long, CoordinateReferenceSystem>> authorityCodeCRS;

	/**
	 * Map between data types and supported Coordinate Reference Systems
	 */
	private static Map<DataType, Set<CoordinateReferenceSystem>> dataTypeCRS;

	/**
	 * Authority
	 */
	private final String authority;

	/**
	 * CRS Code
	 */
	private final long code;

	/**
	 * Name
	 */
	private final String crsName;

	/**
	 * CRS Type
	 */
	private final CRSType type;

	/**
	 * Dimension
	 */
	private final int dimension;

	/**
	 * Well-Known Text
	 */
	private final String wkt;

	/**
	 * Description
	 */
	private final String description;

	/**
	 * Bounds
	 */
	private final BoundingBox bounds;

	/**
	 * WGS84 bounds extent
	 */
	private final BoundingBox wgs84Bounds;

	/**
	 * Data Types
	 */
	private final Set<DataType> dataTypes;

	/**
	 * Contents Data Types
	 */
	private final Map<ContentsDataType, Set<DataType>> contentsDataTypes;

	/**
	 * Constructor with EPSG code and world WGS84 bounds
	 * 
	 * @param epsgCode
	 *            EPSG code
	 * @param name
	 *            name
	 * @param type
	 *            CRS type
	 * @param dimension
	 *            1-3 dimensional
	 * @param wkt
	 *            Well-Known Text
	 * @param description
	 *            description
	 * @param dataTypes
	 *            data types
	 */
	private CoordinateReferenceSystem(long epsgCode, String name, CRSType type,
			int dimension, String wkt, String description,
			DataType... dataTypes) {
		this(epsgCode, name, type, dimension, wkt, description,
				BoundingBox.worldWGS84(), dataTypes);
	}

	/**
	 * Constructor with EPSG code and WGS84 bounds
	 * 
	 * @param epsgCode
	 *            EPSG code
	 * @param name
	 *            name
	 * @param type
	 *            CRS type
	 * @param dimension
	 *            1-3 dimensional
	 * @param wkt
	 *            Well-Known Text
	 * @param description
	 *            description
	 * @param wgs84Bounds
	 *            WGS84 bounds
	 * @param dataTypes
	 *            data types
	 */
	private CoordinateReferenceSystem(long epsgCode, String name, CRSType type,
			int dimension, String wkt, String description,
			BoundingBox wgs84Bounds, DataType... dataTypes) {
		this(epsgCode, name, type, dimension, wkt, description, wgs84Bounds,
				wgs84Bounds, dataTypes);
	}

	/**
	 * Constructor with EPSG code, bounds (tile matrix set), and WGS84 bounds
	 * 
	 * @param epsgCode
	 *            EPSG code
	 * @param name
	 *            name
	 * @param type
	 *            CRS type
	 * @param dimension
	 *            1-3 dimensional
	 * @param wkt
	 *            Well-Known Text
	 * @param description
	 *            description
	 * @param bounds
	 *            bounds
	 * @param wgs84Bounds
	 *            WGS84 bounds
	 * @param dataTypes
	 *            data types
	 */
	private CoordinateReferenceSystem(long epsgCode, String name, CRSType type,
			int dimension, String wkt, String description, BoundingBox bounds,
			BoundingBox wgs84Bounds, DataType... dataTypes) {
		this(ProjectionConstants.AUTHORITY_EPSG, epsgCode, name, type,
				dimension, wkt, description, bounds, wgs84Bounds, dataTypes);
	}

	/**
	 * Constructor with authority code and world WGS84 bounds
	 * 
	 * @param authority
	 *            authority
	 * @param code
	 *            code
	 * @param name
	 *            name
	 * @param type
	 *            CRS type
	 * @param dimension
	 *            1-3 dimensional
	 * @param wkt
	 *            Well-Known Text
	 * @param description
	 *            description
	 * @param dataTypes
	 *            data types
	 */
	private CoordinateReferenceSystem(String authority, long code, String name,
			CRSType type, int dimension, String wkt, String description,
			DataType... dataTypes) {
		this(authority, code, name, type, dimension, wkt, description,
				BoundingBox.worldWGS84(), dataTypes);
	}

	/**
	 * Constructor with authority code and WGS84 bounds
	 * 
	 * @param authority
	 *            authority
	 * @param code
	 *            code
	 * @param name
	 *            name
	 * @param type
	 *            CRS type
	 * @param dimension
	 *            1-3 dimensional
	 * @param wkt
	 *            Well-Known Text
	 * @param description
	 *            description
	 * @param wgs84Bounds
	 *            WGS84 bounds
	 * @param dataTypes
	 *            data types
	 */
	private CoordinateReferenceSystem(String authority, long code, String name,
			CRSType type, int dimension, String wkt, String description,
			BoundingBox wgs84Bounds, DataType... dataTypes) {
		this(authority, code, name, type, dimension, wkt, description,
				wgs84Bounds, wgs84Bounds, dataTypes);
	}

	/**
	 * Constructor with authority code, bounds (tile matrix set), and WGS84
	 * bounds
	 * 
	 * @param authority
	 *            authority
	 * @param code
	 *            code
	 * @param name
	 *            name
	 * @param type
	 *            CRS type
	 * @param dimension
	 *            1-3 dimensional
	 * @param wkt
	 *            Well-Known Text
	 * @param description
	 *            description
	 * @param bounds
	 *            bounds
	 * @param wgs84Bounds
	 *            WGS84 bounds
	 * @param dataTypes
	 *            data types
	 */
	private CoordinateReferenceSystem(String authority, long code, String name,
			CRSType type, int dimension, String wkt, String description,
			BoundingBox bounds, BoundingBox wgs84Bounds,
			DataType... dataTypes) {
		this.authority = authority;
		this.code = code;
		this.crsName = name;
		this.type = type;
		this.dimension = dimension;
		this.wkt = wkt;
		this.description = description;
		this.bounds = bounds;
		this.wgs84Bounds = wgs84Bounds;
		this.dataTypes = new HashSet<>(Arrays.asList(dataTypes));
		this.contentsDataTypes = new HashMap<>();
		for (DataType dataType : dataTypes) {
			ContentsDataType contentsDataType = dataType.getDataType();
			Set<DataType> dataTypesSet = this.contentsDataTypes
					.get(contentsDataType);
			if (dataTypesSet == null) {
				dataTypesSet = new HashSet<>();
				this.contentsDataTypes.put(contentsDataType, dataTypesSet);
			}
			dataTypesSet.add(dataType);
		}
		initialize(this);
	}

	/**
	 * Constructor for UTM Zones
	 * 
	 * @param epsgCode
	 *            UTM Zone EPSG
	 */
	private CoordinateReferenceSystem(long epsgCode) {
		this.wkt = WellKnownText.getUTMZone(epsgCode);
		this.authority = ProjectionConstants.AUTHORITY_EPSG;
		this.code = epsgCode;
		long zone = UTMZone.getZone(epsgCode);
		String latDirection = UTMZone.getLatDirection(epsgCode);
		this.crsName = "WGS 84 / UTM zone " + zone + latDirection;
		this.type = CRSType.PROJECTED;
		this.dimension = 2;

		long centralMedian = UTMZone.getCentralMeridian(zone);
		String lonDirection = centralMedian < 0 ? "W" : "S";
		boolean north = UTMZone.isNorth(epsgCode);

		double minLongitude = centralMedian - 3;
		double maxLongitude = centralMedian + 3;
		double minLatitude = 0.0;
		double maxLatitude = 0.0;
		if (north) {
			maxLatitude = 84.0;
		} else {
			minLatitude = -80.0;
		}

		StringBuilder description = new StringBuilder("Between ");
		description.append(Math.abs(minLongitude));
		description.append("°");
		description.append(lonDirection);
		description.append(" and ");
		description.append(Math.abs(maxLongitude));
		description.append("°");
		description.append(lonDirection);
		description.append(", ");
		if (north) {
			description.append("northern");
		} else {
			description.append("southern");
		}
		description.append(" hemisphere between ");
		if (north) {
			description.append("equator and 84°N");
		} else {
			description.append("80°S and equator");
		}
		description.append(", onshore and offshore.");
		this.description = description.toString();

		this.bounds = new BoundingBox(-9501965.72931276, -20003931.4586255,
				10501965.7293128, 20003931.4586255);
		this.wgs84Bounds = new BoundingBox(minLongitude, minLatitude,
				maxLongitude, maxLatitude);
		this.dataTypes = new HashSet<>();
		this.dataTypes.add(DataType.TILES_2D);
		this.contentsDataTypes = new HashMap<>();
		this.contentsDataTypes.put(ContentsDataType.TILES, this.dataTypes);
		initialize(this);
	}

	/**
	 * Get the authority
	 * 
	 * @return authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * Get the code
	 * 
	 * @return code
	 */
	public long getCode() {
		return code;
	}

	/**
	 * Get the authority and code
	 * 
	 * @return authority:code
	 */
	public String getAuthorityAndCode() {
		return authority + ":" + code;
	}

	/**
	 * Get the name
	 * 
	 * @return name
	 */
	public String getName() {
		return crsName;
	}

	/**
	 * Get the CRS type
	 * 
	 * @return CRS type
	 */
	public CRSType getType() {
		return type;
	}

	/**
	 * Get the dimension
	 * 
	 * @return dimension
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * Get the Well-Known Text
	 * 
	 * @return well-known text
	 */
	public String getWkt() {
		return wkt;
	}

	/**
	 * Get the description
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the bounds
	 * 
	 * @return bounding box
	 */
	public BoundingBox getBounds() {
		return bounds;
	}

	/**
	 * Get the WGS84 bounds extent
	 * 
	 * @return bounding box
	 */
	public BoundingBox getWGS84Bounds() {
		return wgs84Bounds;
	}

	/**
	 * Get the data types
	 * 
	 * @return data types
	 */
	public Set<DataType> getDataTypes() {
		return dataTypes;
	}

	/**
	 * Is the CRS Type
	 * 
	 * @param type
	 *            crs type
	 * @return true if type
	 */
	public boolean isType(CRSType type) {
		return this.type == type;
	}

	/**
	 * Is valid for the Data Type
	 * 
	 * @param dataType
	 *            data type
	 * @return true if valid for data type
	 */
	public boolean isDataType(DataType dataType) {
		return this.dataTypes.contains(dataType);
	}

	/**
	 * Get the contents data types
	 * 
	 * @return contents data types
	 */
	public Map<ContentsDataType, Set<DataType>> getContentsDataTypes() {
		return contentsDataTypes;
	}

	/**
	 * Get the tiles data types
	 * 
	 * @return tiles data types
	 */
	public Set<DataType> getTilesDataTypes() {
		return getDataTypes(ContentsDataType.TILES);
	}

	/**
	 * Has tiles data types
	 * 
	 * @return true if has tiles data types
	 */
	public boolean hasTilesDataTypes() {
		Set<DataType> tiles = getTilesDataTypes();
		return tiles != null && !tiles.isEmpty();
	}

	/**
	 * Get the features data types
	 * 
	 * @return features data types
	 */
	public Set<DataType> getFeaturesDataTypes() {
		return getDataTypes(ContentsDataType.FEATURES);
	}

	/**
	 * Has features data types
	 * 
	 * @return true if has features data types
	 */
	public boolean hasFeaturesDataTypes() {
		Set<DataType> features = getFeaturesDataTypes();
		return features != null && !features.isEmpty();
	}

	/**
	 * Get the data types for the contents data type
	 * 
	 * @param contentsDataType
	 *            contents data type
	 * @return data types
	 */
	public Set<DataType> getDataTypes(ContentsDataType contentsDataType) {
		return contentsDataTypes.get(contentsDataType);
	}

	/**
	 * Create a Spatial Reference System
	 * 
	 * @return Spatial Reference System
	 */
	public SpatialReferenceSystem createSpatialReferenceSystem() {

		SpatialReferenceSystem srs = new SpatialReferenceSystem();

		srs.setSrsName(crsName);
		srs.setSrsId(code);
		srs.setOrganization(authority);
		srs.setOrganizationCoordsysId(code);
		String definition = wkt;
		if (type == CRSType.COMPOUND) {
			definition = GeoPackageConstants.UNDEFINED_DEFINITION;
		}
		srs.setDefinition(definition);
		srs.setDescription(description);
		srs.setDefinition_12_063(wkt);

		return srs;
	}

	/**
	 * Validate the CRS for tiles and get the SRS
	 * 
	 * @return srs
	 */
	public SpatialReferenceSystem createTilesSpatialReferenceSystem() {
		if (!hasTilesDataTypes()) {
			throw new GeoPackageException(
					"CRS is not valid for tiles: " + getAuthorityAndCode());
		}
		return createSpatialReferenceSystem();
	}

	/**
	 * Validate the CRS for features and get the SRS
	 * 
	 * @return srs
	 */
	public SpatialReferenceSystem createFeaturesSpatialReferenceSystem() {
		if (!hasFeaturesDataTypes()) {
			throw new GeoPackageException(
					"CRS is not valid for features: " + getAuthorityAndCode());
		}
		return createSpatialReferenceSystem();
	}

	/**
	 * Lambert Conic Conformal 1SP Description
	 */
	public static final String LAMBERT_CONIC_CONFORMAL_1SP_DESCRIPTION = "For a one standard parallel Lambert the natural "
			+ "origin of the projected coordinate system is the "
			+ "intersection of the standard parallel with the "
			+ "longitude of origin (central meridian).";

	/**
	 * Lambert Conic Conformal 2SP Description
	 */
	public static final String LAMBERT_CONIC_CONFORMAL_2SP_DESCRIPTION = "Two standard parallels will usually be made "
			+ "according to the latitudinal extent of the area "
			+ "which it is wished to map, the parallels usually "
			+ "being chosen so that they each lie a proportion "
			+ "inboard of the north and south margins of the " + "mapped area.";

	/**
	 * Create a Lambert Conic Conformal 1SP Spatial Reference System
	 * 
	 * @param epsg
	 *            Lambert Conic Conformal 1SP EPSG
	 * @param name
	 *            CRS name
	 * @param geoDatum
	 *            {@link GeoDatums#WGS84}, {@link GeoDatums#ETRS89}, or
	 *            {@link GeoDatums#NAD83}
	 * @param latitudeOfOrigin
	 *            latitude of origin
	 * @param centralMeridian
	 *            central meridian
	 * @param scaleFactor
	 *            scale factor
	 * @param falseEasting
	 *            false easting
	 * @param falseNorthing
	 *            false northing
	 * @param standardParallel1
	 *            standard parallel 1
	 * @return Spatial Reference System
	 */
	public static SpatialReferenceSystem createLambertConicConformal1SP(
			long epsg, String name, GeoDatums geoDatum, double latitudeOfOrigin,
			double centralMeridian, double scaleFactor, double falseEasting,
			double falseNorthing, double standardParallel1) {

		String definition = WellKnownText.getLambertConicConformal1SP(epsg,
				name, geoDatum, latitudeOfOrigin, centralMeridian, scaleFactor,
				falseEasting, falseNorthing, standardParallel1);

		return createLambertConicConformal(epsg, name, definition,
				LAMBERT_CONIC_CONFORMAL_1SP_DESCRIPTION);
	}

	/**
	 * Create a Lambert Conic Conformal 2SP Spatial Reference System
	 * 
	 * @param epsg
	 *            Lambert Conic Conformal 2SP EPSG
	 * @param name
	 *            CRS name
	 * @param geoDatum
	 *            {@link GeoDatums#WGS84}, {@link GeoDatums#ETRS89}, or
	 *            {@link GeoDatums#NAD83}
	 * @param standardParallel1
	 *            standard parallel 1
	 * @param standardParallel2
	 *            standard parallel 2
	 * @param latitudeOfOrigin
	 *            latitude of origin
	 * @param centralMeridian
	 *            central meridian
	 * @param falseEasting
	 *            false easting
	 * @param falseNorthing
	 *            false northing
	 * @return Spatial Reference System
	 */
	public static SpatialReferenceSystem createLambertConicConformal2SP(
			long epsg, String name, GeoDatums geoDatum,
			double standardParallel1, double standardParallel2,
			double latitudeOfOrigin, double centralMeridian,
			double falseEasting, double falseNorthing) {

		String definition = WellKnownText.getLambertConicConformal2SP(epsg,
				name, geoDatum, standardParallel1, standardParallel2,
				latitudeOfOrigin, centralMeridian, falseEasting, falseNorthing);

		return createLambertConicConformal(epsg, name, definition,
				LAMBERT_CONIC_CONFORMAL_2SP_DESCRIPTION);
	}

	/**
	 * Create a Lambert Conic Conformal Spatial Reference System
	 * 
	 * @param epsg
	 *            Lambert Conic Conformal EPSG
	 * @param name
	 *            CRS name
	 * @param definition
	 *            well-known text definition
	 * @param description
	 *            srs description
	 * @return Spatial Reference System
	 */
	private static SpatialReferenceSystem createLambertConicConformal(long epsg,
			String name, String definition, String description) {

		SpatialReferenceSystem srs = new SpatialReferenceSystem();

		srs.setSrsName(name);
		srs.setSrsId(epsg);
		srs.setOrganization(ProjectionConstants.AUTHORITY_EPSG);
		srs.setOrganizationCoordsysId(epsg);
		srs.setDefinition(definition);
		srs.setDescription(description);
		srs.setDefinition_12_063(definition);

		return srs;
	}

	/**
	 * Get the coordinate reference system for the EPSG code
	 * 
	 * @param epsgCode
	 *            EPSG code
	 * @return crs
	 */
	public static CoordinateReferenceSystem getCoordinateReferenceSystem(
			long epsgCode) {
		return getCoordinateReferenceSystem(ProjectionConstants.AUTHORITY_EPSG,
				epsgCode);
	}

	/**
	 * Get the coordinate reference system for the spatial reference system
	 * 
	 * @param srs
	 *            spatial reference system
	 * @return crs
	 */
	public static CoordinateReferenceSystem getCoordinateReferenceSystem(
			SpatialReferenceSystem srs) {
		return getCoordinateReferenceSystem(srs.getOrganization(),
				srs.getOrganizationCoordsysId());
	}

	/**
	 * Get the coordinate reference system for the authority and code
	 * 
	 * @param authority
	 *            authority
	 * @param code
	 *            code
	 * @return crs
	 */
	public static CoordinateReferenceSystem getCoordinateReferenceSystem(
			String authority, long code) {
		CoordinateReferenceSystem crs = null;
		Map<Long, CoordinateReferenceSystem> codeMap = authorityCodeCRS
				.get(authority);
		if (codeMap != null) {
			crs = codeMap.get(code);
		}
		return crs;
	}

	/**
	 * Get the supported coordinate reference systems for the data type
	 * 
	 * @param dataType
	 *            data type
	 * @return coordinate reference systems
	 */
	public static Set<CoordinateReferenceSystem> getCoordinateReferenceSystems(
			DataType dataType) {

		Set<CoordinateReferenceSystem> crs = dataTypeCRS.get(dataType);
		if (crs != null) {
			crs = Collections.unmodifiableSet(crs);
		}

		return crs;
	}

	/**
	 * Get the supported coordinate reference systems for the contents data type
	 * 
	 * @param dataType
	 *            data type
	 * @return coordinate reference systems
	 */
	public static Set<CoordinateReferenceSystem> getCoordinateReferenceSystems(
			ContentsDataType dataType) {

		Set<CoordinateReferenceSystem> crss = new HashSet<>();

		for (DataType dt : DataType.getDataTypes(dataType)) {

			Set<CoordinateReferenceSystem> crs = dataTypeCRS.get(dt);
			if (crs != null) {
				crss.addAll(crs);
			}

		}

		return crss;
	}

	/**
	 * Initialize static mappings
	 * 
	 * @param crs
	 *            CRS
	 */
	private static void initialize(CoordinateReferenceSystem crs) {

		if (authorityCodeCRS == null) {
			authorityCodeCRS = new HashMap<>();
		}

		Map<Long, CoordinateReferenceSystem> codeMap = authorityCodeCRS
				.get(crs.getAuthority());
		if (codeMap == null) {
			codeMap = new HashMap<>();
			authorityCodeCRS.put(crs.getAuthority(), codeMap);
		}
		codeMap.put(crs.getCode(), crs);

		if (dataTypeCRS == null) {
			dataTypeCRS = new HashMap<>();
		}

		for (DataType dataType : crs.getDataTypes()) {

			Set<CoordinateReferenceSystem> crsSet = dataTypeCRS.get(dataType);
			if (crsSet == null) {
				crsSet = new LinkedHashSet<>();
				dataTypeCRS.put(dataType, crsSet);
			}

			crsSet.add(crs);
		}

	}

}
