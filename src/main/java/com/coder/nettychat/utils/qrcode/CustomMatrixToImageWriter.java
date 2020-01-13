package com.coder.nettychat.utils.qrcode;

import com.coder.nettychat.utils.LogUtil;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author monkJay
 * @description 重写了一个生成二维码的基类
 * @date 2020/1/11 11:04
 */
public final class CustomMatrixToImageWriter {
    /**
     * 用于设置图案的颜色
     */
    private static final int BLACK = 0xFF000000;
    /**
     * 用于背景色
     */
    private static final int WHITE = 0xFFFFFFFF;

    private CustomMatrixToImageWriter() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y,  (matrix.get(x, y) ? BLACK : WHITE));
            }
        }
        return image;
    }

    public static void writeToFile(BitMatrix matrix, String format, File file, String logoUrl) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        //设置logo图标
        LogoUtil logoConfig = new LogoUtil();
        image = logoConfig.logoMatrix(image, logoUrl);

        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }else{
            LogUtil.info("二维码生成成功！");
        }
    }
}