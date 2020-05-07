package mil.nga.geopackage.extension.im.vector_tiles;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.im.ImageMattersExtensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * MVT encoding for vector tiles
 * 
 * https://gitlab.com/imagemattersllc/ogc-vtp2/-/blob/master/extensions/2-mvte.adoc
 * 
 * @author jyutzler
 * @since 4.0.0
 */
public class VectorTilesMapboxExtension extends VectorTilesEncodingExtension {
	private static final String EXTENSION_AUTHOR = ImageMattersExtensions.EXTENSION_AUTHOR;
	private static final String EXTENSION_NAME_NO_AUTHOR = "vector_tiles_mapbox";
	private static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);
	private static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS,
					EXTENSION_NAME_NO_AUTHOR);
	private static final String EXTENSION_TYPE = "application/vnd.mapbox-vector-tile";

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public VectorTilesMapboxExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	@Override
	public String getName() {
		return EXTENSION_NAME;
	}

	@Override
	public String getDefinition() {
		return EXTENSION_DEFINITION;
	}

	@Override
	public String getType() {
		return EXTENSION_TYPE;
	}
}
