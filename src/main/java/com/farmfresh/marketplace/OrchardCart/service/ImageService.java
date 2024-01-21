package com.farmfresh.marketplace.OrchardCart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class ImageService {
    //@Value("${file.upload.path}")
    //public String fileUploadPath;
    private  static final Logger log = LoggerFactory.getLogger(ImageService.class);
    private final Path UPLOAD_PATH = Paths.get("src/main/resources/static/img/");


    public String saveImage(MultipartFile imageFile, String subDirectory) throws IOException {
        Path subDirectoryPath = UPLOAD_PATH.resolve(subDirectory);

        if (!subDirectoryPath.toFile().exists()) {
            subDirectoryPath.toFile().mkdirs();
        }

        String originalFileName = imageFile.getOriginalFilename();
        String imageUrl = subDirectoryPath + "/" + originalFileName;

        Path destinationPath = UPLOAD_PATH.resolve(StringUtils.cleanPath(originalFileName));
        imageFile.transferTo(destinationPath);

        return "/img/" + subDirectory + "/" + originalFileName;
    }

}

