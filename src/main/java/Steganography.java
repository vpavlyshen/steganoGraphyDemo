import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Steganography {
    public static void main(String[] args) {
        String coverImagePath = "cover_image.png";
        String secretMessage = "This is a secret message.";

        BufferedImage coverImage = loadImage(coverImagePath);
        BufferedImage stegoImage = hideMessage(coverImage, secretMessage);

        String stegoImagePath = "stego_image.png";
        saveImage(stegoImage, stegoImagePath);

        System.out.println("Secret message hidden successfully!");
    }

    public static BufferedImage loadImage(String imagePath) {
        try {
            File file = new File(imagePath);
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveImage(BufferedImage image, String imagePath) {
        try {
            File file = new File(imagePath);
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage hideMessage(BufferedImage coverImage, String secretMessage) {
        int width = coverImage.getWidth();
        int height = coverImage.getHeight();

        BufferedImage stegoImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int charIndex = 0;
        int bitIndex = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = coverImage.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                if (charIndex < secretMessage.length()) {
                    char currentChar = secretMessage.charAt(charIndex);
                    int bit = (currentChar >> bitIndex) & 1;
                    red = (red & 0xFE) | bit;
                    bitIndex++;

                    if (bitIndex == 8) {
                        bitIndex = 0;
                        charIndex++;
                    }
                }

                int stegoPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                stegoImage.setRGB(x, y, stegoPixel);
            }
        }

        return stegoImage;
    }
}
