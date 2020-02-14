package mil.nga.geopackage.extension.portrayal;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortrayalExtension extends BaseExtension {
    public static final String EXTENSION_AUTHOR = "im";
    public static final String EXTENSION_NAME_NO_AUTHOR = "portrayal";
    public static final String EXTENSION_NAME = Extensions.buildExtensionName(EXTENSION_AUTHOR,
            EXTENSION_NAME_NO_AUTHOR);
    public static final String EXTENSION_DEFINITION = GeoPackageProperties
            .getProperty(PropertyConstants.EXTENSIONS,
                    EXTENSION_NAME_NO_AUTHOR);

    private final StylesheetsDao stylesheetsDao;
    private final StylesDao stylesDao;
    private final SymbolContentDao symbolContentDao;
    private final SymbolImagesDao symbolImagesDao;
    private final SymbolsDao symbolsDao;

    /**
     * Constructor
     *
     * @param geoPackage GeoPackage
     */
    public PortrayalExtension(GeoPackageCore geoPackage) {

        super(geoPackage);
        stylesheetsDao = geoPackage.getStylesheetsDao();
        stylesDao = geoPackage.getStylesDao();
        symbolContentDao = geoPackage.getSymbolContentDao();
        symbolImagesDao = geoPackage.getSymbolImagesDao();
        symbolsDao = geoPackage.getSymbolsDao();
    }

    public boolean has() {

        return this.has(EXTENSION_NAME, Styles.TABLE_NAME, null) &&
                geoPackage.isTable(Styles.TABLE_NAME) &&
                geoPackage.isTable(Stylesheets.TABLE_NAME) &&
                geoPackage.isTable(Symbols.TABLE_NAME) &&
                geoPackage.isTable(SymbolContent.TABLE_NAME) &&
                geoPackage.isTable(SymbolImages.TABLE_NAME);
    }

    /**
     * Get or create the extension
     *
     * @return extensions
     */
    public List<Extensions> getOrCreate() {

        geoPackage.createPortrayalTables();

        List<Extensions> extensions = new ArrayList<>();

        extensions.add(getOrCreate(EXTENSION_NAME,
                Styles.TABLE_NAME, null, EXTENSION_DEFINITION,
                ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME,
                Stylesheets.TABLE_NAME, null, EXTENSION_DEFINITION,
                ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME,
                Symbols.TABLE_NAME, null, EXTENSION_DEFINITION,
                ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME,
                SymbolImages.TABLE_NAME, null, EXTENSION_DEFINITION,
                ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME,
                SymbolContent.TABLE_NAME, null, EXTENSION_DEFINITION,
                ExtensionScopeType.READ_WRITE));

        return extensions;
    }

    public void removeExtension() {
        try {
            if (this.stylesDao.isTableExists()) {
                this.geoPackage.dropTable(Styles.TABLE_NAME);
            }
            if (this.stylesheetsDao.isTableExists()) {
                this.geoPackage.dropTable(Stylesheets.TABLE_NAME);
            }
            if (this.symbolsDao.isTableExists()) {
                this.geoPackage.dropTable(Symbols.TABLE_NAME);
            }
            if (this.symbolImagesDao.isTableExists()) {
                this.geoPackage.dropTable(SymbolImages.TABLE_NAME);
            }
            if (this.symbolContentDao.isTableExists()) {
                this.geoPackage.dropTable(SymbolContent.TABLE_NAME);
            }
            this.extensionsDao.deleteByExtension(EXTENSION_NAME);
        } catch (SQLException var4) {
            throw new GeoPackageException("Failed to delete Portrayal extension and table. GeoPackage: " + this.geoPackage.getName(), var4);
        }
    }
}
