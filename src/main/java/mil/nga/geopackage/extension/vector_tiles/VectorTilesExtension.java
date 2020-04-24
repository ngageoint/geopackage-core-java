package mil.nga.geopackage.extension.vector_tiles;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.user.TileTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VectorTilesExtension extends BaseExtension {
    private static final String EXTENSION_AUTHOR = "im";
    private static final String EXTENSION_NAME_NO_AUTHOR = "vector_tiles";
    private static final String EXTENSION_NAME = Extensions.buildExtensionName(EXTENSION_AUTHOR,
            EXTENSION_NAME_NO_AUTHOR);
    private static final String EXTENSION_DEFINITION = GeoPackageProperties
            .getProperty(PropertyConstants.EXTENSIONS,
                    EXTENSION_NAME_NO_AUTHOR);

    private final VectorTilesLayersDao vectorTilesLayersDao;
    private final VectorTilesFieldsDao vectorTilesFieldsDao;

    /**
     * Constructor
     *
     * @param geoPackage GeoPackage
     */
    public VectorTilesExtension(GeoPackageCore geoPackage) {

        super(geoPackage);
        vectorTilesLayersDao = geoPackage.getVectorTilesLayersDao();
        vectorTilesFieldsDao = geoPackage.getVectorTilesFieldsDao();
    }

    /**
     *
     * @return the extension name
     */
    public static String getName() {
        return EXTENSION_NAME;
    }

    public boolean has() {
        return this.has(EXTENSION_NAME, VectorTilesLayers.TABLE_NAME, null) &&
                geoPackage.isTable(VectorTilesLayers.TABLE_NAME);

    }

    /**
     * @param tableName user tiles table name
     * @return true if table was created, false if the table already existed
     */
    public boolean createUserVectorTilesTable(String tableName, VectorTilesEncodingExtension vtee) {

        boolean created = false;

        if (!geoPackage.isTable(tableName)) {

            geoPackage.createTileTable(new TileTable(tableName));

            created = true;
        }

        getOrCreate(tableName, vtee);

        return created;
    }

    /**
     * Get or create the extension
     *
     * @return extensions
     */
    public List<Extensions> getOrCreate() {

        geoPackage.createVectorTilesTables();

        List<Extensions> extensions = new ArrayList<>();

        extensions.add(getOrCreate(EXTENSION_NAME, VectorTilesLayers.TABLE_NAME,
                null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME, VectorTilesFields.TABLE_NAME,
                null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));

        return extensions;
    }

    /**
     * Get or create the extension
     *
     * @param tilesTable tiles table name
     * @return extensions
     */
    public List<Extensions> getOrCreate(String tilesTable, VectorTilesEncodingExtension vtee) {

        List<Extensions> extensions = getOrCreate();

        extensions.add(vtee.getOrCreate(tilesTable));

        return extensions;
    }

    public void removeExtension() {
        try {
            if (this.vectorTilesFieldsDao.isTableExists()) {
                this.geoPackage.dropTable(VectorTilesFields.TABLE_NAME);
            }
            if (this.vectorTilesLayersDao.isTableExists()) {
                List<VectorTilesLayers> vectorTilesLayers = this.vectorTilesLayersDao.queryForAll();
                for (VectorTilesLayers vectorTilesLayer : vectorTilesLayers) {
                    this.geoPackage.deleteTable(vectorTilesLayer.getTableName());
                    // This will pick up both the vector tiles and the encoding
                    this.extensionsDao.deleteByTableName(vectorTilesLayer.getTableName());
                }

                this.geoPackage.dropTable(VectorTilesLayers.TABLE_NAME);
            }

            if (this.extensionsDao.isTableExists()) {
                this.extensionsDao.deleteByExtension(EXTENSION_NAME);
            }

        } catch (SQLException var4) {
            throw new GeoPackageException("Failed to delete Vector Tiles extension and/or tables. GeoPackage: " + this.geoPackage.getName(), var4);
        }
    }
}
