//Retrospective Correction using Morphological Filtering
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

public class Retrospective3 {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Retrospective3Demo().run();
	}
}

class Retrospective3Demo {
	public void ShowImage(Mat img, String title, int locs[]) {
		MatOfByte matOfByte = new MatOfByte();

		Highgui.imencode(".jpg", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;

		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			JFrame frame = new JFrame();
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
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
			int msize = 5;
			int msize1 = 5;
			Size ksize = new Size(2 * msize + 1, 2 * msize + 1);
			Size ksize1 = new Size(2 * msize1 + 1, 2 * msize1 + 1);
			Mat kernel = Imgproc.getStructuringElement(2, ksize, new Point(-1,
					-1));

			Mat bth = new Mat(input.size(), CvType.CV_8UC3);
			Imgproc.morphologyEx(input, bth, Imgproc.MORPH_BLACKHAT, kernel);

			Mat closing = new Mat(input.size(), CvType.CV_8UC3);
			Mat kernel1 = Imgproc.getStructuringElement(2, ksize1, new Point(
					msize1, msize1));
			Imgproc.morphologyEx(input, closing, Imgproc.MORPH_CLOSE, kernel1);

			Scalar mean = Core.mean(closing);
			Mat output = new Mat(input.size(), CvType.CV_16UC3);

			Core.add(bth, mean, output);

			Highgui.imwrite("sixoutput0.png", output);
			Psnr.ShowImage(input, output, "retro3", new int[] { 100, 100 });
			Core.normalize(output, output, 0, 255, Core.NORM_MINMAX);
			Core.convertScaleAbs(output, output);
			Psnr.ShowImage(input, output, "retro3", new int[] { 100, 100 });
			System.out.print("PSNR=" + Imgproc.PSNR(input, output));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
