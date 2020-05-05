package mil.nga.geopackage.extension.im.portrayal;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.db.GeoPackageCoreConnection;
import mil.nga.geopackage.db.GeoPackageTableCreator;

/**
 * Portrayal Extension Table Creator
 * 
 * @author jyutzler
 * @author osbornb
 * @since 4.0.0
 */
public class PortrayalTableCreator extends GeoPackageTableCreator {

	/**
	 * Styles property
	 */
	public static final String STYLES = "styles";

	/**
	 * Stylesheets property
	 */
	public static final String STYLESHEETS = "stylesheets";

	/**
	 * Symbol Content property
	 */
	public static final String SYMBOL_CONTENT = "symbol_content";

	/**
	 * Symbol Images property
	 */
	public static final String SYMBOL_IMAGES = "symbol_images";

	/**
	 * Symbols property
	 */
	public static final String SYMBOLS = "symbols";

	/**
	 * Constructor
	 *
	 * @param db
	 *            db connection
	 */
	public PortrayalTableCreator(GeoPackageCoreConnection db) {
		super(db);
	}

	/**
	 * Constructor
	 *
	 * @param geoPackage
	 *            GeoPackage
	 */
	public PortrayalTableCreator(GeoPackageCore geoPackage) {
		super(geoPackage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthor() {
		return PortrayalExtension.EXTENSION_AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return PortrayalExtension.EXTENSION_NAME_NO_AUTHOR;
	}

	/**
	 * Create the Styles table
	 *
	 * @return executed statements
	 */
	public int createStyles() {
		return execScript(STYLES);
	}

	/**
	 * Create the Stylesheets table
	 *
	 * @return executed statements
	 */
	public int createStylesheets() {
		return execScript(STYLESHEETS);
	}

	/**
	 * Create the Symbols table
	 *
	 * @return executed statements
	 */
	public int createSymbols() {
		return execScript(SYMBOLS);
	}

	/**
	 * Create the Symbol Images table
	 *
	 * @return executed statements
	 */
	public int createSymbolImages() {
		return execScript(SYMBOL_IMAGES);
	}

	/**
	 * Create the Symbol Content table
	 *
	 * @return executed statements
	 */
	public int createSymbolContent() {
		return execScript(SYMBOL_CONTENT);
	}

}
