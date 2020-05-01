package mil.nga.geopackage.extension.vector_tiles;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * GeoJSON encoding for vector tiles
 * @author jyutzler
 */
public class VectorTilesGeoJSONExtension extends VectorTilesEncodingExtension {
    private static final String EXTENSION_AUTHOR = "im";
    private static final String EXTENSION_NAME_NO_AUTHOR = "vector_tiles_geojson";
    private static final String EXTENSION_NAME = Extensions.buildExtensionName(EXTENSION_AUTHOR,
            EXTENSION_NAME_NO_AUTHOR);
    private static final String EXTENSION_DEFINITION = GeoPackageProperties
            .getProperty(PropertyConstants.EXTENSIONS,
                    EXTENSION_NAME_NO_AUTHOR);
    private static final String EXTENSION_TYPE = "application/json;type=geojson";

    /**
     * Constructor
     *
     * @param geoPackage GeoPackage
     */
    public VectorTilesGeoJSONExtension(GeoPackageCore geoPackage) {
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
    public String getType() { return EXTENSION_TYPE; }
}
