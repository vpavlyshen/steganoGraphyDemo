import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class SteganographyUnitTests {

    @DataProvider(name = "testMessages")
    public Object[][] testDataMessages() {
        return new Object[][] {
                {"This is a secret message."},
                {"Another secret message."},
        };
    }

    @Test(dataProvider = "testMessages")
    public void testHideAndRetrieveMessage(String secretMessage) {
        // Load cover image
        BufferedImage coverImage = loadImage("cover_image.png");
        assertNotNull(coverImage, "Failed to load cover image.");

        // Hide message
        BufferedImage stegoImage = Steganography.hideMessage(coverImage, secretMessage);
        assertNotNull(stegoImage, "Failed to hide message.");

        // Save stego image
        String stegoImagePath = "stego_image.png";
        saveImage(stegoImage, stegoImagePath);

        // Reload stego image
        BufferedImage reloadedStegoImage = loadImage(stegoImagePath);
        assertNotNull(reloadedStegoImage, "Failed to load stego image.");

        // Retrieve message from reloaded stego image
        String retrievedMessage = SteganographyDecoder.decodeMessage(reloadedStegoImage);
        assertNotNull(retrievedMessage, "Failed to retrieve message.");

        // Assert the retrieved message matches the original secret message
        assertEquals(retrievedMessage, secretMessage);
    }

    @Test
    public void testHideMessage_WithEmptyMessage() {
        // Load cover image
        BufferedImage coverImage = loadImage("cover_image.png");
        assertNotNull(coverImage, "Failed to load cover image.");

        // Hide empty message
        BufferedImage stegoImage = Steganography.hideMessage(coverImage, "");
        assertNotNull(stegoImage, "Failed to hide empty message.");

        // Save stego image
        String stegoImagePath = "stego_image_empty.png";
        saveImage(stegoImage, stegoImagePath);

        // Reload stego image
        BufferedImage reloadedStegoImage = loadImage(stegoImagePath);
        assertNotNull(reloadedStegoImage, "Failed to load stego image with empty message.");

        // Retrieve message from reloaded stego image
        String retrievedMessage = SteganographyDecoder.decodeMessage(reloadedStegoImage);
        assertNotNull(retrievedMessage, "Failed to retrieve message from stego image with empty message.");

        // Assert the retrieved message is empty
        assertTrue(retrievedMessage.isEmpty());
    }

    private BufferedImage loadImage(String imagePath) {
        try {
            File file = new File(imagePath);
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveImage(BufferedImage image, String imagePath) {
        try {
            File file = new File(imagePath);
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
