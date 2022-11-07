package mil.nga.geopackage.dgiwg;

/**
 * DGIWG (Defence Geospatial Information Working Group) Constants
 * 
 * @author osbornb
 * @since 6.6.0
 */
public class DGIWGConstants {

	/**
	 * Tile Width
	 */
	public static final int TILE_WIDTH = 256;

	/**
	 * Tile Height
	 */
	public static final int TILE_HEIGHT = 256;

	/**
	 * Minimum Zoom Level
	 */
	public static final int MIN_ZOOM_LEVEL = 0;

	/**
	 * Maximum Zoom Level
	 */
	public static final int MAX_ZOOM_LEVEL = 24;

	/**
	 * Invalid unknown description
	 */
	public static final String DESCRIPTION_UNKNOWN = "unknown";

	/**
	 * Invalid tbd description
	 */
	public static final String DESCRIPTION_TBD = "tbd";

	/**
	 * MIME encoding of metadata
	 */
	public static final String METADATA_MIME_TYPE = "text/xml";

	/**
	 * DGIWG Metadata Foundation (DMF) base URI
	 */
	public static final String DMF_BASE_URI = "https://dgiwg.org/std/dmf/";

	/**
	 * DGIWG Metadata Foundation (DMF) 2.0 URI
	 */
	public static final String DMF_2_0_URI = DMF_BASE_URI + "2.0";

	/**
	 * DGIWG Metadata Foundation (DMF) Default URI
	 */
	public static final String DMF_DEFAULT_URI = DMF_2_0_URI;

	/**
	 * NMIS base URI
	 */
	public static final String NMIS_BASE_URI = "https://nsgreg-api.nga.mil/schema/nas/";

	/**
	 * NMIS 8.0 URI
	 */
	public static final String NMIS_8_0_URI = NMIS_BASE_URI + "8.0";

	/**
	 * NMIS Default URI
	 */
	public static final String NMIS_DEFAULT_URI = NMIS_8_0_URI;

	/**
	 * NSG Metadata Foundation (NMF) NSG Application Schema (NAS) base URI
	 */
	public static final String NMF_NAS_BASE_URI = "https://nsgregapi.nga.mil/schema/nas/";

	/**
	 * NSG Metadata Foundation (NMF) NSG Application Schema (NAS) 8.0 URI
	 */
	public static final String NMF_NAS_8_0_URI = NMF_NAS_BASE_URI + "8.0";

}
