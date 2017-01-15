//Retrospective Correction using Low-pass Filtering
import java.awt.FlowLayout;
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

public class Retrospective1 {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Retrospective1Demo().run();
	}
}

class Retrospective1Demo {
	public void ShowImage(Mat img1, Mat img2, String title, int locs[]) {
		MatOfByte matOfByte1 = new MatOfByte();

		Highgui.imencode(".jpg", img1, matOfByte1);
		byte[] byteArray1 = matOfByte1.toArray();
		BufferedImage bufImage1 = null;
		MatOfByte matOfByte2 = new MatOfByte();

		Highgui.imencode(".jpg", img2, matOfByte2);
		byte[] byteArray2 = matOfByte2.toArray();
		BufferedImage bufImage2 = null;

		try {
			InputStream in = new ByteArrayInputStream(byteArray1);
			bufImage1 = ImageIO.read(in);
			JFrame frame = new JFrame();
			frame.setLayout(new FlowLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage1)));
			InputStream in2 = new ByteArrayInputStream(byteArray2);
			bufImage2 = ImageIO.read(in2);
			Psnr.calcPsnr(byteArray1, byteArray2);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage2)));
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle(title);

			if ((locs != null) && (locs.length > 0)) {
				frame.setLocation(locs[0], locs[1]);
			} else {
				frame.setLocation(locs[0], locs[1]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			Mat input = Highgui.imread("page.png");
			// Imgproc.resize(input, input, new Size(512, 512), 0,
			// 0,Imgproc.INTER_LANCZOS4);
			Mat lpf = new Mat(input.size(), CvType.CV_8UC3);

			int ksize = 5;
			double sigma = 0.3 * ((ksize - 1) * 0.5 - 1) + 0.8;
			/*
			 * Mat kernel = Imgproc.getGaussianKernel(ksize, sigma); MatOfDouble
			 * mean = new MatOfDouble(); MatOfDouble stddev = new MatOfDouble();
			 * Core.meanStdDev(kernel, mean, stddev);
			 * Imgproc.GaussianBlur(input, lpf, kernel.size(),
			 * stddev.toArray()[0]); Imgproc.medianBlur(input, lpf, 13);
			 */
			/*
			 * Mat kernel = input.clone();// cloning image
			 */

			// Imgproc.GaussianBlur(input, lpf, new Size(ksize, ksize), 1.5, 0);
			// Highgui.imwrite("forth1output.png", lpf);
			Mat kernel = new Mat(3, 3, CvType.CV_32FC1);
			float[] data = { 0, -1, 0, -1, 4, -1, 0, -1, 0 };
			float[] data1 = { 0, 1, 0, 1, -4, 1, 0, 1, 0 };
			float[] data2 = { 1, -2, 1, 2, -4, 2, 1, -2, 1 };
			float[] data3 = { -1, -1, -1, -1, 8, -1, -1, -1, -1 };
			float[] data4 = { 0, -1, 0, -1, 4, -1, 0, -1, 0 };
			kernel.put(0, 0, data1);

			Imgproc.filter2D(input, lpf, -1, kernel);

			Scalar C = Core.mean(lpf);
			Mat diff = new Mat(input.size(), CvType.CV_8UC3);
			Core.subtract(input, lpf, diff);
			Mat output = new Mat(input.size(), CvType.CV_8UC3);
			Core.add(diff, C, output);
			Highgui.imwrite("Image-8643_out.png", output);
			Mat output1 = new Mat(input.size(), CvType.CV_8UC1);
			Imgproc.cvtColor(output, output1, Imgproc.COLOR_RGB2GRAY);
			Imgproc.equalizeHist(output1, output1);
			Highgui.imwrite("forthoutput1.png", output1);
			Psnr.ShowImage(input, output, "retro1", new int[] { 100, 200 });
			// System.out.print(Psnr.getPSNR(input, output));
			System.out.print("PSNR=" + Imgproc.PSNR(input, output));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
