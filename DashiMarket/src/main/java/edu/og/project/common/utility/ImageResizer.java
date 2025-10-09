package edu.og.project.common.utility;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails; // Thumbnailator 라이브러리 사용


public class ImageResizer {

    private static final int TARGET_SIZE = 500; // 목표 너비와 높이 (500px)
    
    
    public static void resizeAndSave500x500(MultipartFile file, String filePath, String rename) throws IOException {

        File saveFile = new File(filePath + rename);

        Thumbnails.of(file.getInputStream())
                  .forceSize(TARGET_SIZE, TARGET_SIZE) 
                  .toFile(saveFile);
    }
}
