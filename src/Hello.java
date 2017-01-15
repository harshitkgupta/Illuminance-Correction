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
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.*;

public class Hello {
	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new Demo().run();
	}
}

class Demo {
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

			/*
			 * File input = new File("digital_image_processing.jpg");
			 * BufferedImage image = ImageIO.read(input);
			 * 
			 * byte[] data = ((DataBufferByte)
			 * image.getRaster().getDataBuffer()) .getData(); Mat mat = new
			 * Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
			 * mat.put(0, 0, data);
			 */
			Mat mat = Highgui.imread("digital_image_processing.jpg");

			Mat mat1 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1);
			Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

			byte[] data1 = new byte[mat1.rows() * mat1.cols()
					* (int) (mat1.elemSize())];
			mat1.get(0, 0, data1);
			BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(),
					BufferedImage.TYPE_BYTE_GRAY);
			image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(),
					data1);

			File ouptut = new File("grayscale.jpg");
			ImageIO.write(image1, "jpg", ouptut);
			int[] loc = { 100, 200 };
			ShowImage(mat, "Image", loc);
			int[] loc1 = { 800, 200 };
			ShowImage(mat1, "Image", loc1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}