package com.coder.nettychat.component;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author monkJay
 * @description
 * @date 2020/1/8 23:07
 */
@Component
public class FastdfsClient {

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                getFilenameExtension(file.getOriginalFilename()), null);

        return storePath.getPath();
    }

    public String uploadFile2(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                getFilenameExtension(file.getOriginalFilename()), null);

        return storePath.getPath();
    }

    public String uploadQRCode(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                "png", null);

        return storePath.getPath();
    }

    public String uploadFace(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                "png", null);

        return storePath.getPath();
    }

    public String uploadBase64(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
                "png", null);

        return storePath.getPath();
    }

    /**
     * 将一段字符串生成一个文件上传
     *
     * @param content 文件内容
     * @param fileExtension
     * @return
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
        return storePath.getPath();
    }

    /**
     * 删除文件
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (!StringUtils.hasLength(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            e.getMessage();
        }
    }

    private String getFilenameExtension(String filename) {
        // Commons IO recognizes both separator styles on every platform.
        String extension = StringUtils.getFilenameExtension(filename == null ? null : filename.replace('\\', '/'));
        return extension == null && filename != null ? "" : extension;
    }
}
