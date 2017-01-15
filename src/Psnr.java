import java.awt.FlowLayout;
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
import org.opencv.highgui.Highgui;

public class Psnr {

	public static double log10(double x) {
		return Math.log(x) / Math.log(10);
	}

	static double getPSNR(Mat I1, Mat I2) {
		Mat s1 = new Mat();
		Core.absdiff(I1, I2, s1);
		s1.assignTo(s1, CvType.CV_32F);

		Core.multiply(s1, s1, s1);

		Scalar s = Core.sumElems(s1); // sum elements per channel
		// System.out.print(s);
		double sse = s.val[0] + s.val[1] + s.val[2]; // sum channels

		if (sse <= 1e-10) // for small values return zero
			return 0;
		else {
			double mse = sse / (double) (I1.channels() * I1.total());
			double psnr = 10.0 * log10((255 * 255) / mse);
			return psnr;
		}
	}

	public static void calcPsnr(byte img1[], byte img2[]) {
		int nrows, ncols;
		// int img1[][], img2[][];
		double peak, signal, noise, mse;

		// if (args.length != 4) {
		// System.out.println("Usage: Psnr <nrows> <ncols> <img1> <img2>");
		// return;
		// }
		// nrows = Integer.parseInt(args[0]);
		// ncols = Integer.parseInt(args[1]);
		// img1 = new int[nrows][ncols];
		// img2 = new int[nrows][ncols];
		// ArrayIO.readByteArray(args[2], img1, nrows, ncols);
		// ArrayIO.readByteArray(args[3], img2, nrows, ncols);

		signal = noise = peak = 0;
		for (int i = 0; i < img1.length; i++) {

			signal += img1[i] * img1[i];
			noise += (img1[i] - img2[i]) * (img1[i] - img2[i]);
			if (peak < img1[i])
				peak = img1[i];

		}

		mse = noise / (img1.length); // Mean square error
		System.out.println("MSE: " + mse);
		System.out.println("SNR: " + 10 * log10(signal / noise));
		System.out.println("PSNR(max=255): " + (10 * log10(255 * 255 / mse)));
		System.out.println("PSNR(max=" + peak + "): " + 10
				* log10((peak * peak) / mse));
	}

	public static void ShowImage(Mat img1, Mat img2, String title, int locs[]) {
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
			JLabel label1 = new JLabel(new ImageIcon(bufImage1));

			frame.getContentPane().add(label1);
			InputStream in2 = new ByteArrayInputStream(byteArray2);
			bufImage2 = ImageIO.read(in2);
			// Psnr.calcPsnr(byteArray1, byteArray2);
			JLabel label2 = new JLabel(new ImageIcon(bufImage2));

			frame.getContentPane().add(label2);
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
}