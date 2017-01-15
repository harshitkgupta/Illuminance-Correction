//Retrospective Correction using Homomorphic Filtering
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
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Retrospective2 {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Retrospective2Demo().run();
	}
}

class Retrospective2Demo {

	public void run() {
		try {
			Mat input = Highgui.imread("Image-8643.png");
			Mat in = new Mat(input.size(), CvType.CV_32FC3);

			input.convertTo(in, CvType.CV_32FC3);

			Mat log = new Mat(input.size(), CvType.CV_32FC3);
			Mat log1 = new Mat(input.size(), CvType.CV_8UC3);
			Scalar m1 = Core.mean(input);

			Core.log(in, log);
			Core.normalize(log, log1, 0, 255, Core.NORM_MINMAX);
			Core.convertScaleAbs(log1, log1);
			Highgui.imwrite("fifth.png", log1);
			Mat lpf = new Mat(input.size(), CvType.CV_32FC3);
			Mat lpf1 = new Mat(input.size(), CvType.CV_8UC1);
			Mat kernel = new Mat(3, 3, CvType.CV_32FC1);
			float[] data = { 0, -1, 0, -1, 4, -1, 0, -1, 0 };
			float[] data1 = { 0, 1, 0, 1, -4, 1, 0, 1, 0 };
			float[] data2 = { 1, -2, 1, 2, -4, 2, 1, -2, 1 };
			float[] data3 = { -1, -1, -1, -1, 8, -1, -1, -1, -1 };
			float[] data4 = { 0, -1, 0, -1, 4, -1, 0, -1, 0 };
			kernel.put(0, 0, data1);

			Imgproc.filter2D(log, lpf, -1, kernel);
			Core.normalize(lpf, lpf1, 0, 255, Core.NORM_MINMAX);
			Core.convertScaleAbs(lpf1, lpf1);
			// Highgui.imwrite("fifth1.png", lpf1);
			Mat exp = new Mat(input.size(), CvType.CV_32FC3);
			Mat exp1 = new Mat(input.size(), CvType.CV_8UC1);
			Core.exp(lpf, exp);
			Core.normalize(exp, exp1, 0, 255, Core.NORM_MINMAX);
			Core.convertScaleAbs(exp1, exp1);
			// Highgui.imwrite("fifth2.png", exp1);
			Mat div = new Mat(input.size(), CvType.CV_32FC3);
			Core.divide(in, exp, div);
			Scalar m2 = Core.mean(div);
			double n1[] = m1.val;
			double n2[] = m2.val;
			double c[] = new double[n1.length];
			for (int i = 0; i < n1.length; i++)
				c[i] = n1[i] / n2[i];
			Scalar C = new Scalar(c);

			Mat output = new Mat(input.size(), CvType.CV_32FC3);
			Core.multiply(input, C, output);
			
			Core.normalize(output, output, 0, 255, Core.NORM_MINMAX);
			Core.convertScaleAbs(output, output);
			Highgui.imwrite("Image-8643_out1.png", output);
			Psnr.ShowImage(input, output, "retro1", new int[] { 100, 200 });
			System.out.print(Psnr.getPSNR(input, output));
			System.out.print("PSNR=" + Imgproc.PSNR(input, output));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
