package mil.nga.geopackage.dgiwg;

import mil.nga.geopackage.contents.ContentsDataType;

/**
 * DGIWG (Defence Geospatial Information Working Group) Data Types
 * 
 * @author osbornb
 * @since 6.5.1
 */
public enum DataType {

	/**
	 * Features 2D
	 */
	FEATURES_2D(ContentsDataType.FEATURES, 2),

	/**
	 * Features 3D
	 */
	FEATURES_3D(ContentsDataType.FEATURES, 3),

	/**
	 * Tiles 2D
	 */
	TILES_2D(ContentsDataType.TILES, 2),

	/**
	 * Tiles 3D
	 */
	TILES_3D(ContentsDataType.TILES, 3);

	/**
	 * Contents Data Type
	 */
	private final ContentsDataType dataType;

	/**
	 * Dimension
	 */
	private final int dimension;

	/**
	 * Constructor
	 * 
	 * @param dataType
	 *            data type
	 * @param dimension
	 *            dimension
	 */
	private DataType(ContentsDataType dataType, int dimension) {
		this.dataType = dataType;
		this.dimension = dimension;
	}

	/**
	 * Get the contents data type
	 * 
	 * @return contents data type
	 */
	public ContentsDataType getDataType() {
		return dataType;
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
	 * Is a features data type
	 * 
	 * @return true if features
	 */
	public boolean isFeatures() {
		return dataType == ContentsDataType.FEATURES;
	}

	/**
	 * Is a tiles data type
	 * 
	 * @return true if tiles
	 */
	public boolean isTiles() {
		return dataType == ContentsDataType.TILES;
	}

	/**
	 * Is a 2D data type
	 * 
	 * @return true if 2D
	 */
	public boolean is2D() {
		return dimension == 2;
	}

	/**
	 * Is a 3D data type
	 * 
	 * @return true if 3D
	 */
	public boolean is3D() {
		return dimension == 3;
	}

	/**
	 * Get a geometry columns z value, 0 for prohibited and 1 for mandatory
	 * 
	 * @return z value
	 */
	public byte getZ() {
		return (byte) (dimension - 2);
	}

}
