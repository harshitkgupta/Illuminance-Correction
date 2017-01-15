//Correction from a Dark Image and a Bright Image

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Prospective1 {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Prospective1Demo().run();
	}
}

class Prospective1Demo {

	public void run() {
		try {
			Mat input = Highgui.imread("serous.png");

			Mat dark = Highgui.imread("dark.png");
			// Imgproc.resize(input, input, dark.size(), 0, 0,
			// Imgproc.INTER_CUBIC);
			Mat bright = Highgui.imread("bright.png");
			Mat diff1 = new Mat(input.size(), CvType.CV_8UC3);
			System.out.print(input.type() + "  " + CvType.CV_8UC3);
			Core.absdiff(input, dark, diff1);

			Mat diff2 = new Mat(input.size(), CvType.CV_8UC3);
			Core.absdiff(bright, dark, diff2);
			Mat div = new Mat(input.size(), CvType.CV_32FC3);
			Core.divide(diff1, diff2, div);

			Scalar m1 = Core.mean(input);
			Scalar m2 = Core.mean(div);

			double n1[] = m1.val;
			double n2[] = m2.val;
			double c[] = new double[n1.length];
			for (int i = 0; i < n1.length; i++)
				c[i] = n1[i] / (n2[i] * 255);
			Scalar C = new Scalar(c);
			// System.out.print(m1 + " " + m2 + " " + C);
			Mat output = new Mat(input.size(), CvType.CV_8UC3);
			Core.add(input, C, output);
			// Imgproc.compareHist(input, output, 1);
			Highgui.imwrite("firstoutput.png", output);
			int locs[] = { 100, 200 };
			Psnr.ShowImage(input, output, "Pros1", locs);
			System.out.print("PSNR=" + Imgproc.PSNR(input, output));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
