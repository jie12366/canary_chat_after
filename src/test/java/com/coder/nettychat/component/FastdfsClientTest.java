package com.coder.nettychat.component;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FastdfsClientTest {
    private static final String[][] FILENAMES = {
            {null, null}, {"", ""}, {"file", ""}, {"file.", ""},
            {".hidden", "hidden"}, {"archive.tar.gz", "gz"},
            {"dir.ext/file", ""}, {"dir.ext/", ""},
            {"dir.ext\\file", ""}, {"dir.ext\\", ""},
            {"C:\\dir.ext\\image.JPG", "JPG"}, {"\\\\server.ext\\share\\file", ""},
            {"mixed.ext\\dir/file", ""}, {"mixed.ext/dir\\file", ""},
            {"image.jpg:stream", "jpg:stream"}, {"a.\u0000", "\u0000"}
    };

    @Test
    void preservesUploadExtensions() throws Exception {
        checkUploads(false);
    }

    @Test
    void preservesThumbnailUploadExtensions() throws Exception {
        checkUploads(true);
    }

    private void checkUploads(boolean thumbnail) throws Exception {
        for (String[] example : FILENAMES) {
            FastFileStorageClient storage = mock(FastFileStorageClient.class);
            FastdfsClient client = new FastdfsClient();
            ReflectionTestUtils.setField(client, "storageClient", storage);
            MultipartFile file = mock(MultipartFile.class);
            ByteArrayInputStream input = new ByteArrayInputStream(new byte[] {1});
            when(file.getOriginalFilename()).thenReturn(example[0]);
            when(file.getInputStream()).thenReturn(input);
            when(file.getSize()).thenReturn(1L);
            StorePath path = mock(StorePath.class);
            when(path.getPath()).thenReturn("stored/path");
            if (thumbnail) {
                when(storage.uploadImageAndCrtThumbImage(any(), anyLong(), nullable(String.class), isNull()))
                        .thenReturn(path);
                assertEquals("stored/path", client.uploadFile2(file));
                verify(storage).uploadImageAndCrtThumbImage(input, 1L, example[1], null);
            } else {
                when(storage.uploadFile(any(), anyLong(), nullable(String.class), isNull())).thenReturn(path);
                assertEquals("stored/path", client.uploadFile(file));
                verify(storage).uploadFile(input, 1L, example[1], null);
            }
        }
    }

    @Test
    void ignoresNullAndEmptyDeletePaths() {
        FastFileStorageClient storage = mock(FastFileStorageClient.class);
        FastdfsClient client = new FastdfsClient();
        ReflectionTestUtils.setField(client, "storageClient", storage);
        client.deleteFile(null);
        client.deleteFile("");
        verifyZeroInteractions(storage);
    }
}
