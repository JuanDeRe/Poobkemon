package src.presentation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    public static BufferedImage loadImage(String path , int width , int heigth) {
        try {
            BufferedImage bufferedImage=  ImageIO.read(new File(path));
            BufferedImage scaledImage = new BufferedImage(width , heigth, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(bufferedImage.getScaledInstance(width , heigth, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();
            return scaledImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}