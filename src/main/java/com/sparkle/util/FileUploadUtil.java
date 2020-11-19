package com.sparkle.util;


import com.sparkle.entity.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @author Smartisan
 */
@Slf4j
public class FileUploadUtil {

    public static ResponseBean upload(List<MultipartFile> files, String photoLocation) {

        BufferedOutputStream bos;
        BufferedInputStream bis;
        FileOutputStream fos;

        // 判断存储的文件夹是否存在
        File file = new File(photoLocation);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            // 遍历文件夹
            for (MultipartFile mf : files) {
                if (!mf.isEmpty()) {
                    String originalFilename = mf.getOriginalFilename();
                    String fileName = originalFilename.substring(originalFilename.lastIndexOf('/') + 1);

                    // 读取文件
                    bis = new BufferedInputStream(mf.getInputStream());
                    // 指定存储的路径
                    fos = new FileOutputStream(photoLocation + fileName);
                    bos = new BufferedOutputStream(fos);

                    int len;
                    byte[] buffer = new byte[10240];
                    while ((len = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }

                    // 刷新此缓冲的输出流，保证数据全部都能写出
                    bos.flush();
                    bos.close();
                    fos.close();
                    bis.close();
                }
            }

            return ResponseBean.success("上传成功");
        } catch (IOException e) {
            log.error("fileUpload ERROR:", e);
            return ResponseBean.fail("上传失败");
        }
    }

}