package com.sparkle.util;


import com.sparkle.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @author Smartisan
 */
@Slf4j
public class FileUploadUtil {

    public static boolean upload(MultipartFile multipartFile, String location) {
        if (multipartFile.isEmpty()) {
            return false;
        }

        // 判断存储的文件夹是否存在
        File file = new File(location);
        if (!file.exists()) {
            boolean mkdir = file.mkdirs();
            if (!mkdir) {
                return false;
            }
        }

        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains("/")) {
            return false;
        }
        String fileName = originalFilename.substring(originalFilename.lastIndexOf('/') + 1);

        try (
                // 读取文件
                BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream());
                // 指定存储的路径
                FileOutputStream fos = new FileOutputStream(location + fileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
        ) {
            int len;
            byte[] buffer = new byte[10240];
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            // 刷新此缓冲的输出流，保证数据全部都能写出
            bos.flush();
            return true;
        } catch (IOException e) {
            log.error("fileUpload ERROR:", e);
            return false;
        }
    }

    public static Response uploadFolder(List<MultipartFile> files, String location) {

        BufferedOutputStream bos;
        BufferedInputStream bis;
        FileOutputStream fos;

        // 判断存储的文件夹是否存在
        File file = new File(location);
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
                    fos = new FileOutputStream(location + fileName);
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

            return Response.success("上传成功");
        } catch (IOException e) {
            log.error("fileUpload ERROR:", e);
            return Response.fail("上传失败");
        }
    }

}