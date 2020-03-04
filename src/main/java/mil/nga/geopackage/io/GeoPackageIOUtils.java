package mil.nga.geopackage.io;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Input / Output utility methods
 * 
 * @author osbornb
 */
public class GeoPackageIOUtils {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger(GeoPackageIOUtils.class.getName());

	/**
	 * Copy stream buffer chunk size in bytes
	 * 
	 * @since 3.3.0
	 */
	public static int COPY_BUFFER_SIZE = 8192;

	/**
	 * Get the file extension
	 * 
	 * @param file
	 *            file
	 * @return extension
	 */
	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}

	/**
	 * Get the file extension
	 * 
	 * @param name
	 *            name
	 * @return extension
	 * @since 3.5.0
	 */
	public static String getFileExtension(String name) {

		String extension = null;

		int extensionIndex = name.lastIndexOf(".");
		if (extensionIndex > -1) {
			extension = name.substring(extensionIndex + 1);
		}

		return extension;
	}

	/**
	 * Check if the file has an extension
	 * 
	 * @param file
	 *            file
	 * @return true if has extension
	 * @since 3.0.2
	 */
	public static boolean hasFileExtension(File file) {
		return getFileExtension(file) != null;
	}

	/**
	 * Check if the name has an extension
	 * 
	 * @param name
	 *            name
	 * @return true if has extension
	 * @since 3.5.0
	 */
	public static boolean hasFileExtension(String name) {
		return getFileExtension(name) != null;
	}

	/**
	 * Add a the file extension to the file
	 * 
	 * @param file
	 *            file
	 * @param extension
	 *            file extension
	 * @return new file with extension
	 * @since 3.0.2
	 */
	public static File addFileExtension(File file, String extension) {
		return new File(addFileExtension(file.getAbsolutePath(), extension));
	}

	/**
	 * Add a the file extension to the name
	 * 
	 * @param name
	 *            name
	 * @param extension
	 *            file extension
	 * @return new name with extension
	 * @since 3.5.0
	 */
	public static String addFileExtension(String name, String extension) {
		return name + "." + extension;
	}

	/**
	 * Get the file name with the extension removed
	 * 
	 * @param file
	 *            file
	 * @return file name
	 */
	public static String getFileNameWithoutExtension(File file) {
		return getFileNameWithoutExtension(file.getName());
	}

	/**
	 * Get the file name with the extension removed
	 * 
	 * @param name
	 *            name
	 * @return file name
	 * @since 3.5.0
	 */
	public static String getFileNameWithoutExtension(String name) {

		int extensionIndex = name.lastIndexOf(".");
		if (extensionIndex > -1) {
			name = name.substring(0, extensionIndex);
		}

		return name;
	}

	/**
	 * Copy a file to a file location
	 * 
	 * @param copyFrom
	 *            from file
	 * @param copyTo
	 *            to file
	 * @throws IOException
	 *             upon failure
	 */
	public static void copyFile(File copyFrom, File copyTo) throws IOException {

		InputStream from = new FileInputStream(copyFrom);
		OutputStream to = new FileOutputStream(copyTo);

		copyStream(from, to);
	}

	/**
	 * Copy an input stream to a file location
	 * 
	 * @param copyFrom
	 *            from file
	 * @param copyTo
	 *            to file
	 * @throws IOException
	 *             upon failure
	 */
	public static void copyStream(InputStream copyFrom, File copyTo)
			throws IOException {
		copyStream(copyFrom, copyTo, null);
	}

	/**
	 * Copy an input stream to a file location
	 * 
	 * @param copyFrom
	 *            from file
	 * @param copyTo
	 *            to file
	 * @param progress
	 *            progress tracker
	 * @throws IOException
	 *             upon failure
	 */
	public static void copyStream(InputStream copyFrom, File copyTo,
			GeoPackageProgress progress) throws IOException {

		OutputStream to = new FileOutputStream(copyTo);

		copyStream(copyFrom, to, progress);

		// Try to delete the file if progress was cancelled
		if (progress != null && !progress.isActive()
				&& progress.cleanupOnCancel()) {
			copyTo.delete();
		}
	}

	/**
	 * Get the file bytes
	 * 
	 * @param file
	 *            input file
	 * @return file bytes
	 * @throws IOException
	 *             upon failure
	 */
	public static byte[] fileBytes(File file) throws IOException {

		FileInputStream fis = new FileInputStream(file);

		return streamBytes(fis);
	}

	/**
	 * Get the stream bytes
	 * 
	 * @param stream
	 *            input stream
	 * @return stream bytes
	 * @throws IOException
	 *             upon failure
	 */
	public static byte[] streamBytes(InputStream stream) throws IOException {

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		copyStream(stream, bytes);

		return bytes.toByteArray();
	}

	/**
	 * Get the stream string in UTF-8
	 * 
	 * @param stream
	 *            input stream
	 * @return stream string
	 * @throws IOException
	 *             upon failure
	 * @since 3.3.0
	 */
	public static String streamString(InputStream stream) throws IOException {
		return streamString(stream, "UTF-8");
	}

	/**
	 * Get the stream string
	 * 
	 * @param stream
	 *            input stream
	 * @param charsetName
	 *            character set name
	 * @return stream string
	 * @throws IOException
	 *             upon failure
	 * @since 3.3.0
	 */
	public static String streamString(InputStream stream, String charsetName)
			throws IOException {

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		copyStream(stream, bytes);

		return bytes.toString(charsetName);
	}

	/**
	 * Copy an input stream to an output stream
	 * 
	 * @param copyFrom
	 *            from stream
	 * @param copyTo
	 *            to stream
	 * @throws IOException
	 *             upon failure
	 */
	public static void copyStream(InputStream copyFrom, OutputStream copyTo)
			throws IOException {
		copyStream(copyFrom, copyTo, null);
	}

	/**
	 * Copy an input stream to an output stream
	 * 
	 * @param copyFrom
	 *            from stream
	 * @param copyTo
	 *            to stream
	 * @param progress
	 *            progress tracker
	 * @throws IOException
	 *             upon failure
	 */
	public static void copyStream(InputStream copyFrom, OutputStream copyTo,
			GeoPackageProgress progress) throws IOException {

		try {
			byte[] buffer = new byte[COPY_BUFFER_SIZE];
			int length;
			while ((progress == null || progress.isActive())
					&& (length = copyFrom.read(buffer)) > 0) {
				copyTo.write(buffer, 0, length);
				if (progress != null) {
					progress.addProgress(length);
				}
			}

			copyTo.flush();
		} finally {
			closeQuietly(copyTo);
			closeQuietly(copyFrom);
		}

	}

	/**
	 * Format the bytes into readable text
	 * 
	 * @param bytes
	 *            bytes
	 * @return bytes text
	 */
	public static String formatBytes(long bytes) {

		double value = bytes;
		String unit = "B";

		if (bytes >= 1024) {
			int exponent = (int) (Math.log(bytes) / Math.log(1024));
			exponent = Math.min(exponent, 4);
			switch (exponent) {
			case 1:
				unit = "KB";
				break;
			case 2:
				unit = "MB";
				break;
			case 3:
				unit = "GB";
				break;
			case 4:
				unit = "TB";
				break;
			}
			value = bytes / Math.pow(1024, exponent);
		}

		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(value) + " " + unit;
	}

	/**
	 * Close the closeable quietly
	 * 
	 * @param closeable
	 *            closeable (stream, etc)
	 * @since 2.0.2
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to close closeable", e);
		}
	}

}
