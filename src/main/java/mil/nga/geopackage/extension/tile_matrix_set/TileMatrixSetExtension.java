package mil.nga.geopackage.extension.tile_matrix_set;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TileMatrixSetExtension extends BaseExtension {
    private static final String EXTENSION_AUTHOR = "ecere";
    private static final String EXTENSION_NAME_NO_AUTHOR = "tms";
    private static final String EXTENSION_NAME = Extensions.buildExtensionName(EXTENSION_AUTHOR,
            EXTENSION_NAME_NO_AUTHOR);
    private static final String EXTENSION_DEFINITION = GeoPackageProperties
            .getProperty(PropertyConstants.EXTENSIONS,
                    EXTENSION_NAME_NO_AUTHOR);

    private final ExtTileMatrixDao tileMatrixDao;
    private final ExtTileMatrixSetDao tileMatrixSetDao;
    private final TileMatrixTablesDao tileMatrixTablesDao;
    private final TileMatrixVariableWidthsDao tileMatrixVariableWidthsDao;

    /**
     * Constructor
     *
     * @param geoPackage GeoPackage
     */
    public TileMatrixSetExtension(GeoPackageCore geoPackage) {

        super(geoPackage);
        tileMatrixDao = geoPackage.getExtTileMatrixDao();
        tileMatrixSetDao = geoPackage.getExtTileMatrixSetDao();
        tileMatrixTablesDao = geoPackage.getTileMatrixTablesDao();
        tileMatrixVariableWidthsDao = geoPackage.getTileMatrixVariableWidthsDao();
    }

    /**
     * @return the extension name
     */
    public static String getName() {
        return EXTENSION_NAME;
    }

    public boolean has() {
        return this.has(EXTENSION_NAME, TileMatrixTable.TABLE_NAME, null) &&
                geoPackage.isTable(TileMatrixTable.TABLE_NAME);

    }

    /**
     * Get or create the extension
     *
     * @return extensions
     */
    public List<Extensions> getOrCreate() {

        geoPackage.createTileMatrixSetExtension();

        List<Extensions> extensions = new ArrayList<>();

        extensions.add(getOrCreate(EXTENSION_NAME, TileMatrixTable.TABLE_NAME,
                null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME, TileMatrixVariableWidths.TABLE_NAME,
                null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME, ExtTileMatrix.TABLE_NAME,
                null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME, ExtTileMatrixSet.TABLE_NAME,
                null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME, TileMatrixSet.TABLE_NAME,
                null, EXTENSION_DEFINITION, ExtensionScopeType.WRITE_ONLY));
        extensions.add(getOrCreate(EXTENSION_NAME, TileMatrix.TABLE_NAME,
                null, EXTENSION_DEFINITION, ExtensionScopeType.WRITE_ONLY));

        return extensions;
    }

    public void removeExtension() {
        try {
            // This is tricky because we have to drop the views and recreate the tables
            if (has()) {
                this.geoPackage.dropView(TileMatrix.TABLE_NAME);
                this.geoPackage.dropView(TileMatrixSet.TABLE_NAME);
                this.geoPackage.createTileMatrixTable();
                this.geoPackage.createTileMatrixSetTable();
                try {
                    // TODO: Run an SQL script that loads these tables back up from what was originally in the views
                } catch (Exception exc) {
                    // No op: if for some reason this doesn't work, we'll give up and just move on
                }
            }
            if (this.tileMatrixVariableWidthsDao.isTableExists()) {
                this.geoPackage.dropTable(TileMatrixVariableWidths.TABLE_NAME);
            }
            if (this.tileMatrixTablesDao.isTableExists()) {
                this.geoPackage.dropTable(TileMatrixTable.TABLE_NAME);
            }
            if (this.tileMatrixDao.isTableExists()) {
                this.geoPackage.dropTable(ExtTileMatrix.TABLE_NAME);
            }
            if (this.tileMatrixSetDao.isTableExists()) {
                this.geoPackage.dropTable(ExtTileMatrixSet.TABLE_NAME);
            }

            if (this.extensionsDao.isTableExists()) {
                this.extensionsDao.deleteByExtension(EXTENSION_NAME);
            }
        } catch (
                SQLException exc) {
            throw new GeoPackageException("Failed to delete Tile Matrix Set extension and/or tables. GeoPackage: " + this.geoPackage.getName(), exc);
        }
    }
}
