package com.coder.nettychat.utils.qrcode;

import com.alibaba.fastjson.JSONObject;
import com.coder.nettychat.utils.LogUtil;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author monkJay
 * @description 二维码生成工具类
 * @date 2020/1/9 22:57
 */
public class QRCodeUtil {

    /**
     * 生成二维码
     * @param text 内容，可以是链接或者文本
     * @param path 生成的二维码位置
     */
    public static void encodeQRCode(String text, String path, String logoUrl) {
        encodeQRCode(text, path, 300, 300, "png", logoUrl);
    }

    /**
     * 生成二维码
     * @param text 内容，可以是链接或者文本
     * @param path 生成的二维码位置
     * @param width 宽度，默认300
     * @param height 高度，默认300
     * @param format 生成的二维码格式，默认png
     */
    public static void encodeQRCode(String text, String path, Integer width, Integer height, String format, String logoUrl) {
        try {
            // 得到文件对象
            File file = new File(path);
            // 判断目标文件所在的目录是否存在
            if(!file.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建父目录
                LogUtil.info("目标文件所在目录不存在，准备创建它！");
                if(!file.getParentFile().mkdirs()) {
                    LogUtil.info("创建目标文件所在目录失败！");
                    return;
                }
            }
            // 自定义二维码的设置
            Map<EncodeHintType, Object> hints = new HashMap<>();
            // 设置字符集编码
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 为了容易识别，设置容错级别为最高
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 设置空白边距的宽度,默认为4
            hints.put(EncodeHintType.MARGIN, 1);
            // 生成二维码矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            // 使用自定义的类来生成带logo的二维码
            CustomMatrixToImageWriter.writeToFile(bitMatrix, format, new File(path), logoUrl);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    /**
     * 对二维码图片进行解码
     * @param filePath 二维码路径
     * @return 解码后对内容
     */
    public static JSONObject decodeQRCode(String filePath) {

        try {
            // 读取图片
            BufferedImage image = ImageIO.read(new File(filePath));

            // 多步解析
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            // 设置字符集编码
            Map<DecodeHintType, String> hints = new HashMap<>(16);
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            // 对图像进行解码
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            // 解码内容
            JSONObject content = JSONObject.parseObject(result.getText());
            LogUtil.info("图片内容:[{}] ", content.toJSONString());
            return content;
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            return null;
        }
    }
}

