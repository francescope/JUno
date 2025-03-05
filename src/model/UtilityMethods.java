package model;

import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * class with utility methods for working on images
 * @author MP
 *
 */
public class UtilityMethods {

	/**
	 * static method to load images from a filepath
	 * @param filepath string of the image path
	 * @return image in the form of BufferedImage object
	 */
	public static BufferedImage loadImg(String filepath) {
		
		try {
			return ImageIO.read(new File(filepath));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * it makes a JButton background transparent and it adds to it an hovering image
	 * @param b JButton that has to be made transparent
	 * @param imgHov hovering image
	 */
	public static void backgroundTrasparent(JButton b, ImageIcon imgHov) {
		
		b.setBorderPainted(false);
		b.setBorder(null);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setContentAreaFilled(false);
		b.setFocusPainted(false);
		b.setRolloverIcon(imgHov);
		b.setPressedIcon(imgHov);
	}
	
	
	
	/** it resizes an BufferedImage
	 * 
	 * @param originalImage original BufferedImage
	 * @param targetWidth width target
	 * @param targetHeight height target
	 * @return the resized BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
		
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}
}