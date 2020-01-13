package com.coder.nettychat.utils.file;

import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author monkJay
 * @description
 * @date 2020/1/9 10:46
 */
public class Base64DecodeMultipartFile implements MultipartFile {

    private byte[] content;
    private String header;

    private Base64DecodeMultipartFile(byte[] content, String header){
        this.content = content;
        this.header = header.split(";")[0];
    }

    @Override
    public String getName() {
        return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + (int) (Math.random() * 9 + 1) * 10000 + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() {
        return content;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(content);
    }

    /**
     * 将base64字符串转换为MultipartFile对象
     * @param str 传入的base64
     * @return 转换后的MultipartFile
     */
    public static MultipartFile base64ToMultipartFile(String str){
        String[] base64 = str.split(",");
        byte[] content = Base64Utils.decodeFromString(base64[1]);
        for (int i = 0; i < content.length; ++i){
            if (content[i] < 0){
                content[i] += 256;
            }
        }
        return new Base64DecodeMultipartFile(content, base64[0]);
    }
}