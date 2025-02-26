package scraping.gookmin.util;

import scraping.gookmin.dto.RequestDto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSlicer {
    public static void saveImage(byte[] imageBytes, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String slice(byte[] imageBytes, Key keypad, RequestDto requestDto) {
        StringBuilder hash = new StringBuilder();

        saveImage(imageBytes, "image.png");

        try {
            // 원본 이미지 읽기
            File inputFile = new File("image.png");
            BufferedImage originalImage = ImageIO.read(inputFile);

            BufferedImage croppedImage = cropWhitespace(originalImage);
            ImageIO.write(croppedImage, "PNG", new File("sliced.png"));

            // 이미지 크기 얻기
            int imageWidth = croppedImage.getWidth();
            int imageHeight = croppedImage.getHeight();

            // 가로로 3등분, 세로로 4등분
            int cols = 3;
            int rows = 4;

            // 각 구간의 크기 계산
            int sectionWidth = imageWidth / cols;
            int sectionHeight = imageHeight / rows;

            // 각 구간을 잘라서 저장
            int count = 1;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // 자를 영역의 시작 x, y 좌표
                    int x = col * sectionWidth;
                    int y = row * sectionHeight;

                    // 잘라낸 이미지 얻기
                    BufferedImage slicedImage = croppedImage.getSubimage(x, y, sectionWidth, sectionHeight);

                    // 잘라낸 이미지를 저장
                    // 픽셀 데이터를 추출하여 키로 사용
                    int[] pixels = getPixels(slicedImage);
                    StringBuilder pixelHexString = new StringBuilder();
                    for (int pixel : pixels) {
                        pixelHexString.append(Integer.toHexString(pixel));
                    }
                    String pixel = pixelHexString.toString().trim().replace("0", "");
                    if(pixel.equals("") && pixel != null && !pixel.isBlank()) {

                        keypad.savePosition(pixel, count);
                        count++;
                    }
                }
            }

            for(char pw : requestDto.getPassword().toCharArray()) {
                hash.append(keypad.getHash(pw - '0'));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    public static BufferedImage cropWhitespace(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // 상단, 하단, 왼쪽, 오른쪽 여백을 찾기 위한 변수
        int top = 0, bottom = height - 1, left = width - 1, right = 0;

        // 투명 배경을 찾기 위한 색상 (여기서는 투명한 배경을 찾음)
        Color transparentColor = new Color(0, 0, 0, 0); // 완전 투명

        // 여백 찾기: 상단 여백 찾기
        outerloop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) != transparentColor.getRGB()) {
                    top = y;
                    break outerloop;
                }
            }
        }

        // 하단 여백 찾기
        outerloop:
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) != transparentColor.getRGB()) {
                    bottom = y;
                    break outerloop;
                }
            }
        }

        // 왼쪽 여백 찾기
        outerloop:
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (image.getRGB(x, y) != transparentColor.getRGB()) {
                    left = x;
                    break outerloop;
                }
            }
        }

        // 오른쪽 여백 찾기
        outerloop:
        for (int x = width - 1; x >= 0; x--) {
            for (int y = 0; y < height; y++) {
                if (image.getRGB(x, y) != transparentColor.getRGB()) {
                    right = x;
                    break outerloop;
                }
            }
        }

        // 여백을 제거한 이미지 생성
        BufferedImage croppedImage = image.getSubimage(left, top, right - left + 1, bottom - top + 1);
        return croppedImage;
    }

    // BufferedImage에서 픽셀 배열을 추출
    private static int[] getPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        return pixels;
    }
}
