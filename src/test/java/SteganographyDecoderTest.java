import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.awt.image.BufferedImage;

public class SteganographyDecoderTest {

    String testImagePath = "cover_image.bmp";
    BufferedImage testImage;

    @BeforeClass
    public void setUp() {
        testImage = Steganography.loadImage(testImagePath);
    }

    @Test(dataProvider = "testDataStrings")
    public void testDecodeMessage(String secretMessage) {
        BufferedImage stegoImage = Steganography.hideMessage(testImage, secretMessage);
        String decodedMessage = SteganographyDecoder.decodeMessage(stegoImage).substring(0,secretMessage.length());
        assertEquals(decodedMessage, secretMessage);
    }

    @Test(dataProvider = "imagePathProvider")
    public void testDecodeMessageDifferentImageSize(String imagePath) {
        String tempSecretMessage = "1234567";
        BufferedImage testImageThisTest;
        testImageThisTest = Steganography.loadImage(imagePath);
        BufferedImage stegoImage = Steganography.hideMessage(testImageThisTest, tempSecretMessage);
        String decodedMessage = SteganographyDecoder.decodeMessage(stegoImage).substring(0,tempSecretMessage.length());
        assertEquals(decodedMessage, tempSecretMessage);
    }

    @Test
    public void testDecodeMessage_EmptyMessage() {
        String secretMessage = "";
        BufferedImage stegoImage = Steganography.hideMessage(testImage, secretMessage);
        String decodedMessage = SteganographyDecoder.decodeMessage(stegoImage);
        assertEquals(decodedMessage, secretMessage);
    }

    @DataProvider
    private Object[][] testDataStrings() {
        return new Object[][]{
                new Object[]{"Another secret message@#test"},
                new Object[]{"Another secret message@##test Another secret message. testAnother secret message.long test t....."},
                new Object[]{"Another secret message. test Another secret message. testAnother secret message.long test t@##!@#%%Another secret message. test Another secret message. testAnother secret message.long test t......Another secret message. test Another secret message. testAnother secret message.long test t......Another secret message. test Another secret message. testAnother secret message.long test "},
                new Object[]{"Very long test stringAVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringVery long test stringnother secret message. test Another secret message. testAnother secret message.long test t......Another secret message. test Another secret message. testAnother secret message.long test t@#$$....Another secret message. test Another secret message. testAnother secret message.long test t@#$@#@$@$Another secret message. test Another secret message. testAnother secret message.long test "},

        };
    }


    @DataProvider
    private Object[][] imagePathProvider() {
        return new Object[][]{
                new Object[]{"640_426.jpeg"},
                new Object[]{"1920_1280.jpeg"},
                new Object[]{"2400_1600.jpeg"},
        };
    }
}
