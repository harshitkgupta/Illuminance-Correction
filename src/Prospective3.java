//Correction from a Dark Image
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
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Prospective3 {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Prospective3Demo().run();
	}
}

class Prospective3Demo {

	public void run() {
		try {
			Mat input = Highgui.imread("serous.png");
			Mat dark = Highgui.imread("dark.png");

			Scalar C = Core.mean(dark);

			Mat diff = new Mat(input.size(), CvType.CV_8UC3);
			Core.subtract(input, dark, diff);
			Mat output = new Mat(input.size(), CvType.CV_8UC3);
			Core.add(diff, C, output);
			System.out.print("PSNR=" + Imgproc.PSNR(input, output));
			Highgui.imwrite("thirdoutput.png", output);
			Psnr.ShowImage(input, output, "pros3", new int[] { 100, 200 });
			System.out.print("PSNR=" + Imgproc.PSNR(input, output));

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
