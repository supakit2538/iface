/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innovatrics.iface.verify_sample;

import com.innovatrics.iface.Face;
import com.innovatrics.iface.FaceBasicInfo;
import com.innovatrics.iface.FaceHandler;
import com.innovatrics.iface.IFace;
import com.innovatrics.iface.IFaceException;
import com.innovatrics.iface.Version;
import com.innovatrics.iface.TemplateInfo;
import com.innovatrics.iface.enums.FaceVerificationSpeedAccuracyMode;
import com.innovatrics.iface.enums.FacedetSpeedAccuracyMode;
import com.innovatrics.iface.enums.Parameter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VerifySample {

	public static void main(String[] args) {
		File f = null;
		File[] paths;
		String currentL = "" ;
		String currentR = "" ;
		float currentP = (float) 00.00;
		try {
			
			f = new File("E:\\Innovatrics\\IFace SDK\\samples\\xxx");
			
			paths = f.listFiles();

			for (File path : paths) {

				IFace iface = IFace.getInstance();

				Config cfg = new Config();
				cfg.parseParameters(args, path.toString() );

				long tm = System.currentTimeMillis();
				iface.init();
				long ifaceInitializationTime = System.currentTimeMillis() - tm;
				System.out.println();
				System.out.println("IFace initialization time: " + ifaceInitializationTime + " ms");

				// create face handler entity
				tm = System.currentTimeMillis();
				FaceHandler faceHandler = new FaceHandler();
				long fhCreationTime = System.currentTimeMillis() - tm;
				System.out.println("Face handler creation time: " + fhCreationTime + " ms");

				// set the speed accuracy mode for face detection
				faceHandler.setParam(Parameter.FACEDET_SPEED_ACCURACY_MODE,
						FacedetSpeedAccuracyMode.ACCURATE.toString());

				for (FaceVerificationSpeedAccuracyMode verificationMode : FaceVerificationSpeedAccuracyMode.values()) {
					faceHandler.setParam(Parameter.FACEVERIF_SPEED_ACCURACY_MODE, verificationMode.toString());

					System.out.println();
					System.out.println("Getting face templates in " + verificationMode.toString() + " mode");
					System.out.println();

					byte[] template0 = getFaceTemplate(faceHandler, cfg.image0Path, cfg);
					byte[] template1 = getFaceTemplate(faceHandler, cfg.image1Path, cfg);
					if (template0 == null || template1 == null) {
						System.out.println("Unable to get all templates");
						System.exit(1);
					}

					TemplateInfo info0 = faceHandler.getTemplateInfo(template0);
					TemplateInfo info1 = faceHandler.getTemplateInfo(template1);
					System.out.println("First template version: " + info0.getVersion().major + "."
							+ info0.getVersion().minor + ", quality: " + info0.getQuality());
					System.out.println("Second template version: " + info1.getVersion().major + "."
							+ info1.getVersion().minor + ", quality: " + info1.getQuality());

					tm = System.currentTimeMillis();
					float matchingConfidence = faceHandler.matchTemplate(template0, template1);
					long matchingTime = System.currentTimeMillis() - tm;

					System.out.println("Matching time: " + matchingTime + " ms");
					System.out.println();
					System.out.println(String.format("Matching confidence: %.3f", matchingConfidence));
					
					if (matchingConfidence > currentP) {
						currentL = cfg.image0Path;
						currentR = cfg.image1Path;
						currentP = matchingConfidence;
					} else {

					}

				}
				iface.terminate();

			}
		} catch (IFaceException ex) {
			ex.printStackTrace();
		}

		System.out.println("--------------------------------------------------------------");
		System.out.println(currentL);
		System.out.println(currentP);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		/*Hello hello = new Hello();
		hello.Helloframe(currentL);*/
		ImagePanel panel = new ImagePanel(new ImageIcon(currentL).getImage());
		ImagePanel panel2 = new ImagePanel(new ImageIcon(currentR).getImage());
		
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().add(panel,BorderLayout.WEST);
		
		frame.getContentPane().add(panel2,BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
		

	}

	static byte[] getFaceTemplate(FaceHandler faceHandler, String imagePath, Config cfg) {
		long tm = System.currentTimeMillis();
		Face[] faces = faceHandler.detectFaces(imagePath, cfg.minEyeDistance, cfg.maxEyeDistance, 1);
		long detectionTime = System.currentTimeMillis() - tm;
		System.out.println("Detection time: " + detectionTime + " ms");

		if (faces.length == 0) {
			System.out.println("No faces detected in image: " + imagePath);
			return null;
		}
		if (faces.length > 1) {
			System.out.println("More than one face detected in image: " + imagePath + "\nSelecting the first one");
		}

		FaceBasicInfo bi = faces[0].getBasicInfo();
		System.out.println(String.format("Face confidence : %.1f, right eye : (%.1f,%.1f), left eye : (%.1f,%.1f)",
				bi.getScore(), bi.getRightEye().getX(), bi.getRightEye().getY(), bi.getLeftEye().getX(),
				bi.getLeftEye().getY()));
		tm = System.currentTimeMillis();
		byte[] result = faces[0].createTemplate();
		long extractionTime = System.currentTimeMillis() - tm;
		System.out.println("Extraction time: " + extractionTime + " ms");
		return result;
	}
}

class Config {

	public int minEyeDistance = 30;
	public int maxEyeDistance = 200;
	public String image0Path;
	public String image1Path = "E:\\Innovatrics\\IFace SDK\\samples\\test\\obiwan2.png"; // ..\\..\\xxx\\vvv.png

	public void parseParameters(String[] args, String image ) {
		image0Path = image;
		boolean doShowInfo = false;
		
		
		try {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i].trim().toLowerCase();
				switch (arg) {
				case "-h":
					doShowInfo = true;
					break;
				case "-i0":
					image0Path = args[i + 1];
					break;
				case "-i1":
					image1Path = args[i + 1];
					break;
				case "-min":
					minEyeDistance = Integer.valueOf(args[i + 1]);
					break;
				case "-max":
					maxEyeDistance = Integer.valueOf(args[i + 1]);
					break;
				}

				if (doShowInfo)
					break;
			}

		} catch (Exception ex) {
			doShowInfo = true;
		}

		if (doShowInfo || args.length == 0) {
			// System.out.println(writeHelp());
			// System.exit(1);
		}
		checkImageFile(image0Path, "First");
		checkImageFile(image1Path, "Second");
		if (minEyeDistance < 13) {
			System.out.println("Min eye distance parameter is wrong [-min parameter]");
			System.exit(1);
		}
		if (minEyeDistance < 13 || maxEyeDistance < minEyeDistance) {
			System.out.println("Max eye distance parameter is wrong [-max parameter]");
			System.exit(1);
		}
		Version version = IFace.getInstance().getVersion();
		System.out.println(String.format("IFace version: %d.%d.%d", version.major, version.minor, version.revision));
		System.out.println();
		System.out.println("First image file path: " + image0Path);
		System.out.println("Second image file path: " + image1Path);
		System.out.println();
		System.out.println("IFace parameters:");
		System.out.println("  min eyes distance: " + minEyeDistance);
		System.out.println("  max eyes distance: " + maxEyeDistance);
	}

	static void checkImageFile(String imagePath, String message) {
		if (!new File(imagePath).exists()) {
			System.out.println(message);
			System.exit(1);
		}
	}

	// private static String writeHelp() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("Possible parameters:\n");
	// sb.append(" -i0 first image file path [mandatory]\n");
	// sb.append(" -i1 second image file path [mandatory]\n");
	// sb.append(" -min minimum eye distance [optional - default 30]\n");
	// sb.append(" -max maximum eye distance [optional - default 200]\n");
	// sb.append(" -h print help\n");
	// sb.append("");
	//
	// return sb.toString();
	// }

}



class ImagePanel extends JPanel {

	private Image img;

	public ImagePanel(String img) {
		this(new ImageIcon(img).getImage());
	}

	public ImagePanel(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

}
