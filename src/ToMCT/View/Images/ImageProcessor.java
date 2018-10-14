package ToMCT.View.Images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {

    public static String FILE_FOLDER = "ToMCT/View/Images/Files/";

    public static BufferedImage getImage(String name){

        String filename = FILE_FOLDER + name;
        BufferedImage img = null;

        File imgFile = new File(filename);

        try {

            img = ImageIO.read(imgFile);

        } catch (IOException e) {

            System.err.println("Could not load image " + filename);
        }

        return img;
    }

}
