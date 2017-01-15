//Correction from a Bright Image
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

public class Prospective2 {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Prospective2Demo().run();
	}
}

class Prospective2Demo {

	public void run() {

		try {
			Mat input = Highgui.imread("serous.png");
			Mat background = Highgui.imread("bright.png");
			Mat diff = new Mat(input.size(), CvType.CV_8UC3);
			Core.subtract(input, background, diff);
			Mat div = new Mat(input.size(), CvType.CV_32FC3);
			Core.divide(input, background, div);

			Scalar c1 = Core.mean(input);
			Mat mul = new Mat(input.size(), CvType.CV_32FC3);
			Core.multiply(div, c1, mul);

			Scalar c2 = Core.mean(div);
			double n1[] = c1.val;
			double n2[] = c2.val;
			double c[] = new double[n1.length];
			for (int i = 0; i < n1.length; i++)
				c[i] = 1 / (n2[i]);
			for (int i = 0; i < n1.length; i++)
				c[i] *= n1[i];
			Scalar C = new Scalar(c);

			Mat output = new Mat(input.size(), CvType.CV_8UC3);
			Core.add(mul, c2, output);

			// Core.add(output, input, output);
			Highgui.imwrite("secondoutput.png", output);

			Psnr.ShowImage(input, output, "Pros2", new int[] { 100, 200 });
			System.out.print("PSNR=" + Imgproc.PSNR(input, output));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
