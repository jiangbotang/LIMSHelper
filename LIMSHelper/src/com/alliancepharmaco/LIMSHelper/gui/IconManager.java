package com.alliancepharmaco.LIMSHelper.gui;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class IconManager {
	protected static BufferedImage readBufferedImage (String imagePath) {
	    try {
	        InputStream is = IconManager.class.getClassLoader().getResourceAsStream(imagePath);
	        BufferedImage bimage = ImageIO.read(is);
	        is.close();
	        return bimage;
	    } catch (Exception e) {
	        return null;
	    }
	}

}
