import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SteganographyDecoder {
    public static void main(String[] args) {
        String stegoImagePath = "stego_image.png";

        BufferedImage stegoImage = loadImage(stegoImagePath);
        String decodedMessage = decodeMessage(stegoImage);

        System.out.println("Decoded message: " + decodedMessage);
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

    public static String decodeMessage(BufferedImage stegoImage) {
        int width = stegoImage.getWidth();
        int height = stegoImage.getHeight();

        StringBuilder messageBuilder = new StringBuilder();
        int charIndex = 0;
        int bitIndex = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = stegoImage.getRGB(x, y);
                int red = (pixel >> 16) & 0xFF;

                int bit = (red & 1) << bitIndex;
                charIndex |= bit;

                bitIndex++;
                if (bitIndex == 8) {
                    if (charIndex == 0)
                        return messageBuilder.toString();
                    char currentChar = (char) charIndex;
                    messageBuilder.append(currentChar);

                    charIndex = 0;
                    bitIndex = 0;
                }
            }
        }
        if (!messageBuilder.toString().isEmpty())
            return messageBuilder.toString();
        else {
            return "Provided image does not include any message. Sorry! =)";
        }
    }
}
