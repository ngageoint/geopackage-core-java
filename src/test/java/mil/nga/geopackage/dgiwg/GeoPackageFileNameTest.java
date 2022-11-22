package mil.nga.geopackage.dgiwg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import mil.nga.geopackage.io.GeoPackageIOUtils;

/**
 * DGIWG File Name test (informative)
 * 
 * @author osbornb
 */
public class GeoPackageFileNameTest {

	/**
	 * Test file name example 1
	 */
	@Test
	public void testExample1() {

		String name = "AGC_BUCK_Ft-Bliss_14-20_v1-0_29AUG2016.gpkg";

		GeoPackageFileName fileName = new GeoPackageFileName(name);

		testFileName(name, fileName);

		assertTrue(fileName.isInformative());

		assertEquals("AGC", fileName.getProducer());
		assertEquals("BUCK", fileName.getDataProduct());
		assertEquals("Ft Bliss", fileName.getGeographicCoverageArea());
		assertEquals("14-20", fileName.getZoomLevels());
		assertEquals(14, fileName.getZoomLevel1().intValue());
		assertEquals(20, fileName.getZoomLevel2().intValue());
		assertEquals("1.0", fileName.getVersion());
		assertEquals(1, fileName.getMajorVersion().intValue());
		assertEquals(0, fileName.getMinorVersion().intValue());
		assertEquals("29AUG2016", fileName.getCreationDateText());
		assertEquals(dateValue("29AUG2016", "ddMMMyyyy"),
				fileName.getCreationDate());
		assertNull(fileName.getAdditional());

		assertEquals(GeoPackageIOUtils.getFileNameWithoutExtension(name),
				fileName.getName());

	}

	/**
	 * Test file name example 2
	 */
	@Test
	public void testExample2() {

		String name = "OGL_BOUND_UK_1-10000_v1-0_09APR2020.gpkg";

		GeoPackageFileName fileName = new GeoPackageFileName(name);

		testFileName(name, fileName);

		assertTrue(fileName.isInformative());

		assertEquals("OGL", fileName.getProducer());
		assertEquals("BOUND", fileName.getDataProduct());
		assertEquals("UK", fileName.getGeographicCoverageArea());
		assertEquals("1:10000", fileName.getZoomLevels());
		assertEquals(1, fileName.getZoomLevel1().intValue());
		assertEquals(10000, fileName.getZoomLevel2().intValue());
		assertEquals("1.0", fileName.getVersion());
		assertEquals(1, fileName.getMajorVersion().intValue());
		assertEquals(0, fileName.getMinorVersion().intValue());
		assertEquals("09APR2020", fileName.getCreationDateText());
		assertEquals(dateValue("09APR2020", "ddMMMyyyy"),
				fileName.getCreationDate());
		assertNull(fileName.getAdditional());

		assertEquals(GeoPackageIOUtils.getFileNameWithoutExtension(name),
				fileName.getName());

	}

	/**
	 * Test file name additional elements
	 */
	@Test
	public void testAdditionalElements() {

		String name = "Producer_Data-Product_Geographic-Coverage-Area_1-12_v3-2_2021-11-16_Additional_Additional-Two.gpkg";

		GeoPackageFileName fileName = new GeoPackageFileName(name);

		testFileName(name, fileName);

		assertTrue(fileName.isInformative());

		assertEquals("Producer", fileName.getProducer());
		assertEquals("Data Product", fileName.getDataProduct());
		assertEquals("Geographic Coverage Area",
				fileName.getGeographicCoverageArea());
		assertEquals("1-12", fileName.getZoomLevels());
		assertEquals(1, fileName.getZoomLevel1().intValue());
		assertEquals(12, fileName.getZoomLevel2().intValue());
		assertEquals("3.2", fileName.getVersion());
		assertEquals(3, fileName.getMajorVersion().intValue());
		assertEquals(2, fileName.getMinorVersion().intValue());
		assertEquals("2021-11-16", fileName.getCreationDateText());
		assertEquals(dateValue("2021-11-16", "yyyy-MM-dd"),
				fileName.getCreationDate());
		assertEquals(2, fileName.getAdditional().size());
		assertEquals("Additional", fileName.getAdditional().get(0));
		assertEquals("Additional Two", fileName.getAdditional().get(1));

		assertEquals(GeoPackageIOUtils.getFileNameWithoutExtension(name),
				fileName.getName());

	}

	/**
	 * Test file name unknown elements
	 */
	@Test
	public void testUnknownElements() {

		String name = "Producer_Data-Product_Geographic-Coverage-Area_Zoom-Levels_Version_20211116.gpkg";

		GeoPackageFileName fileName = new GeoPackageFileName(name);

		testFileName(name, fileName);

		assertTrue(fileName.isInformative());

		assertEquals("Producer", fileName.getProducer());
		assertEquals("Data Product", fileName.getDataProduct());
		assertEquals("Geographic Coverage Area",
				fileName.getGeographicCoverageArea());
		assertEquals("Zoom Levels", fileName.getZoomLevels());
		assertNull(fileName.getZoomLevel1());
		assertNull(fileName.getZoomLevel2());
		assertEquals("Version", fileName.getVersion());
		assertNull(fileName.getMajorVersion());
		assertNull(fileName.getMinorVersion());
		assertEquals("20211116", fileName.getCreationDateText());
		assertNull(fileName.getCreationDate());
		assertNull(fileName.getAdditional());

		assertEquals(GeoPackageIOUtils.getFileNameWithoutExtension(name),
				fileName.getName());

	}

	/**
	 * Test invalid file name
	 */
	@Test
	public void testInvalid() {

		String name = "Producer_Data-Product_Geographic-Coverage-Area_1-12_v3-2.gpkg";

		GeoPackageFileName fileName = new GeoPackageFileName(name);

		testFileName(name, fileName);

		assertFalse(fileName.isInformative());

		assertEquals("Producer", fileName.getProducer());
		assertEquals("Data Product", fileName.getDataProduct());
		assertEquals("Geographic Coverage Area",
				fileName.getGeographicCoverageArea());
		assertEquals("1-12", fileName.getZoomLevels());
		assertEquals(1, fileName.getZoomLevel1().intValue());
		assertEquals(12, fileName.getZoomLevel2().intValue());
		assertEquals("3.2", fileName.getVersion());
		assertEquals(3, fileName.getMajorVersion().intValue());
		assertEquals(2, fileName.getMinorVersion().intValue());
		assertNull(fileName.getCreationDateText());
		assertNull(fileName.getCreationDate());
		assertNull(fileName.getAdditional());

		assertEquals(GeoPackageIOUtils.getFileNameWithoutExtension(name),
				fileName.getName());

	}

	/**
	 * Test other file name creation options
	 * 
	 * @param name
	 *            name
	 * @param fileName
	 *            DGIWG file name
	 */
	private void testFileName(String name, GeoPackageFileName fileName) {

		File file = new File(name);

		testFileName(name, file, fileName);

		file = new File("/base/directory", name);
		name = file.getAbsolutePath();

		testFileName(name, file, fileName);

	}

	/**
	 * Test other file name creation options
	 * 
	 * @param name
	 *            name
	 * @param file
	 *            file
	 * @param fileName
	 *            DGIWG file name
	 */
	private void testFileName(String name, File file,
			GeoPackageFileName fileName) {

		assertEquals(fileName, new GeoPackageFileName(file));
		assertEquals(fileName, new GeoPackageFileName(
				GeoPackageIOUtils.getFileNameWithoutExtension(name)));
		assertEquals(fileName, new GeoPackageFileName(
				GeoPackageIOUtils.getFileNameWithoutExtension(file)));

	}

	/**
	 * Get the date value
	 * 
	 * @param value
	 *            date value
	 * @param format
	 *            date format
	 * @return date
	 */
	private Date dateValue(String value, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = null;
		try {
			date = sdf.parse(value);
		} catch (ParseException e) {
			fail(e.toString());
		}
		return date;
	}

}
