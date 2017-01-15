import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Retrospective6 {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Retrospective6Demo().run();
	}
}

class Retrospective6Demo {

	public void run() {
		try {
			Mat input = Highgui.imread("page.png");
			// Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HLS);
			int msize = 5;
			int msize1 = 5;
			Size ksize = new Size(2 * msize + 1, 2 * msize + 1);
			Size ksize1 = new Size(2 * msize1 + 1, 2 * msize1 + 1);
			Mat kernel = Imgproc.getStructuringElement(2, ksize, new Point(-1,
					-1));

			Mat dil = new Mat(input.size(), CvType.CV_8UC3);
			Imgproc.morphologyEx(input, dil, Imgproc.MORPH_DILATE, kernel);

			Mat ero = new Mat(input.size(), CvType.CV_8UC3);
			Mat kernel1 = Imgproc.getStructuringElement(2, ksize1, new Point(
					-1, -1));
			Imgproc.morphologyEx(input, ero, Imgproc.MORPH_ERODE, kernel1);
			Mat sub = new Mat(input.size(), CvType.CV_8UC3);
			Core.absdiff(input, ero, sub);

			Scalar mean = Core.mean(ero);
			Mat output = new Mat(input.size(), CvType.CV_8UC3);

			Core.add(sub, mean, output);

			Highgui.imwrite("nineoutput.png", output);
			Psnr.ShowImage(input, output, "retro3", new int[] { 100, 100 });
			System.out.print("PSNR=" + Imgproc.PSNR(input, output));

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
