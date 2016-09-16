package mil.nga.tiff.io;

import java.io.File;

public class TempTest {

	public static void main(String[] args) throws Exception {

		File file
		// = new File("/Users/osbornb/Downloads/denver.tiff");
		// = new File("/Users/osbornb/Desktop/elevation.tiff");
		// = new File("/Users/osbornb/Downloads/tiled.tiff");
		= new File(
				"/Users/osbornb/Documents/geotiff.js-master/test/data/packbits.tiff");

		FileDirectories fileDirectories = TiffReader.readTiff(file, true);
		FileDirectory fileDirectory = fileDirectories.getFileDirectory();
		Rasters rasters = fileDirectory.readRasters();
		Rasters rasters2 = fileDirectory.readInterleavedRasters();

		if (rasters.getWidth() != fileDirectory.getImageWidth().intValue()) {
			System.out.println("WIDTH");
		}
		if (rasters.getHeight() != fileDirectory.getImageHeight().intValue()) {
			System.out.println("HEIGHT");
		}

		for (int y = 0; y < rasters.getHeight(); y++) {
			for (int x = 0; x < rasters.getWidth(); x++) {
				Number[] pixel = rasters.getPixel(x, y);
				Number[] pixel2 = rasters2.getPixel(x, y);

				ImageWindow window = new ImageWindow(x, y);
				Rasters rasters3 = fileDirectory.readRasters(window);
				Rasters rasters4 = fileDirectory.readInterleavedRasters(window);
				if (rasters3.getNumPixels() != 1
						|| rasters4.getNumPixels() != 1) {
					System.out.println("Num pixels");
				}

				for (int i = 0; i < rasters.getSamplesPerPixel(); i++) {
					Number sample = rasters.getPixelSample(i, x, y);
					Number sample2 = rasters2.getPixelSample(i, x, y);
					if (sample.intValue() != sample2.intValue()) {
						System.out.println("SAMPLE VS INTERLEAVE");
					}
					if (pixel[i].intValue() != sample.intValue()) {
						System.out.println("PIXEL SAMPLE");
					}
					if (pixel[i].intValue() != pixel2[i].intValue()) {
						System.out.println("PIXEL SAMPLE VS INTERLEAVE");
					}

					Number sample3 = rasters3.getPixelSample(i, 0, 0);
					Number sample4 = rasters4.getPixelSample(i, 0, 0);
					if (pixel[i].intValue() != sample3.intValue()
							|| pixel[i].intValue() != sample4.intValue()) {
						System.out.println("SINGLE PIXEL VALUE");
					}

				}
			}
		}

		System.out.println("DONE");
	}
}
