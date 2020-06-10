package mil.nga.geopackage.extension.im.portrayal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.im.ImageMattersExtensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

/**
 * Portrayal Extension
 * 
 * https://gitlab.com/imagemattersllc/ogc-vtp2/-/blob/master/extensions/5-portrayal.adoc
 * 
 * @author jyutzler
 * @since 4.0.0
 */
public class PortrayalExtension extends BaseExtension {
	public static final String EXTENSION_AUTHOR = ImageMattersExtensions.EXTENSION_AUTHOR;;
	public static final String EXTENSION_NAME_NO_AUTHOR = "portrayal";
	public static final String EXTENSION_NAME = Extensions
			.buildExtensionName(EXTENSION_AUTHOR, EXTENSION_NAME_NO_AUTHOR);
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
	 * @param geoPackage
	 *            GeoPackage
	 */
	public PortrayalExtension(GeoPackageCore geoPackage) {

		super(geoPackage);
		stylesheetsDao = getStylesheetsDao();
		stylesDao = getStylesDao();
		symbolContentDao = getSymbolContentDao();
		symbolImagesDao = getSymbolImagesDao();
		symbolsDao = getSymbolsDao();
	}

	public boolean has() {

		return this.has(EXTENSION_NAME, Styles.TABLE_NAME, null)
				&& geoPackage.isTable(Styles.TABLE_NAME)
				&& geoPackage.isTable(Stylesheets.TABLE_NAME)
				&& geoPackage.isTable(Symbols.TABLE_NAME)
				&& geoPackage.isTable(SymbolContent.TABLE_NAME)
				&& geoPackage.isTable(SymbolImages.TABLE_NAME);
	}

	/**
	 * Get or create the extension
	 *
	 * @return extensions
	 */
	public List<Extensions> getOrCreate() {

		createPortrayalTables();

		List<Extensions> extensions = new ArrayList<>();

		extensions.add(getOrCreate(EXTENSION_NAME, Styles.TABLE_NAME, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, Stylesheets.TABLE_NAME, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, Symbols.TABLE_NAME, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, SymbolImages.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));
		extensions.add(getOrCreate(EXTENSION_NAME, SymbolContent.TABLE_NAME,
				null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE));

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
			throw new GeoPackageException(
					"Failed to delete Portrayal extension and table. GeoPackage: "
							+ this.geoPackage.getName(),
					var4);
		}
	}

	/**
	 * Get the Styles DAO
	 * 
	 * @return styles dao
	 */
	public StylesDao getStylesDao() {
		return getStylesDao(geoPackage);
	}

	/**
	 * Get the Styles DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return styles dao
	 */
	public static StylesDao getStylesDao(GeoPackageCore geoPackage) {
		return StylesDao.create(geoPackage);
	}

	/**
	 * Get the Styles DAO
	 * 
	 * @param db
	 *            database connection
	 * @return styles dao
	 */
	public static StylesDao getStylesDao(GeoPackageCoreConnection db) {
		return StylesDao.create(db);
	}

	/**
	 * Get the Stylesheets DAO
	 * 
	 * @return stylesheets dao
	 */
	public StylesheetsDao getStylesheetsDao() {
		return getStylesheetsDao(geoPackage);
	}

	/**
	 * Get the Stylesheets DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return stylesheets dao
	 */
	public static StylesheetsDao getStylesheetsDao(GeoPackageCore geoPackage) {
		return StylesheetsDao.create(geoPackage);
	}

	/**
	 * Get the Stylesheets DAO
	 * 
	 * @param db
	 *            database connection
	 * @return stylesheets dao
	 */
	public static StylesheetsDao getStylesheetsDao(
			GeoPackageCoreConnection db) {
		return StylesheetsDao.create(db);
	}

	/**
	 * Get the Symbol Content DAO
	 * 
	 * @return symbol content dao
	 */
	public SymbolContentDao getSymbolContentDao() {
		return getSymbolContentDao(geoPackage);
	}

	/**
	 * Get the Symbol Content DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return symbol content dao
	 */
	public static SymbolContentDao getSymbolContentDao(
			GeoPackageCore geoPackage) {
		return SymbolContentDao.create(geoPackage);
	}

	/**
	 * Get the Symbol Content DAO
	 * 
	 * @param db
	 *            database connection
	 * @return symbol content dao
	 */
	public static SymbolContentDao getSymbolContentDao(
			GeoPackageCoreConnection db) {
		return SymbolContentDao.create(db);
	}

	/**
	 * Get the Symbol Images DAO
	 * 
	 * @return symbol images dao
	 */
	public SymbolImagesDao getSymbolImagesDao() {
		return getSymbolImagesDao(geoPackage);
	}

	/**
	 * Get the Symbol Images DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return symbol images dao
	 */
	public static SymbolImagesDao getSymbolImagesDao(
			GeoPackageCore geoPackage) {
		return SymbolImagesDao.create(geoPackage);
	}

	/**
	 * Get the Symbol Images DAO
	 * 
	 * @param db
	 *            database connection
	 * @return symbol images dao
	 */
	public static SymbolImagesDao getSymbolImagesDao(
			GeoPackageCoreConnection db) {
		return SymbolImagesDao.create(db);
	}

	/**
	 * Get the Symbols DAO
	 * 
	 * @return symbols dao
	 */
	public SymbolsDao getSymbolsDao() {
		return getSymbolsDao(geoPackage);
	}

	/**
	 * Get the Symbols DAO
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * @return symbols dao
	 */
	public static SymbolsDao getSymbolsDao(GeoPackageCore geoPackage) {
		return SymbolsDao.create(geoPackage);
	}

	/**
	 * Get the Symbols DAO
	 * 
	 * @param db
	 *            database connection
	 * @return symbols dao
	 */
	public static SymbolsDao getSymbolsDao(GeoPackageCoreConnection db) {
		return SymbolsDao.create(db);
	}

	/**
	 * Create the Portrayal Extension tables if they do not exist
	 *
	 * @return true if any table is created
	 */
	public boolean createPortrayalTables() {
		verifyWritable();

		boolean created = false;

		PortrayalTableCreator tableCreator = new PortrayalTableCreator(
				geoPackage);

		try {
			if (!stylesDao.isTableExists()) {
				created = tableCreator.createStyles() > 0;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ Styles.TABLE_NAME + " table exists and create it", e);
		}

		try {
			if (!stylesheetsDao.isTableExists()) {
				created = (tableCreator.createStylesheets() > 0) || created;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ Stylesheets.TABLE_NAME + " table exists and create it",
					e);
		}

		try {
			if (!symbolsDao.isTableExists()) {
				created = (tableCreator.createSymbols() > 0) || created;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ Stylesheets.TABLE_NAME + " table exists and create it",
					e);
		}

		try {
			if (!symbolContentDao.isTableExists()) {
				created = (tableCreator.createSymbolContent() > 0) || created;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ SymbolContent.TABLE_NAME + " table exists and create it",
					e);
		}

		try {
			if (!symbolImagesDao.isTableExists()) {
				created = (tableCreator.createSymbolImages() > 0) || created;
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to check if "
					+ SymbolImages.TABLE_NAME + " table exists and create it",
					e);
		}

		return created;
	}

}
