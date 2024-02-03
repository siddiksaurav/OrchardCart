package com.farmfresh.marketplace.OrchardCart.service;

import jakarta.annotation.PostConstruct;
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
    @Value("${image.upload.path}")
    String imageUploadPath;
    private static final Logger log = LoggerFactory.getLogger(ImageService.class);
    private Path UPLOAD_PATH;

    @PostConstruct
    public void init() {
        UPLOAD_PATH = Paths.get(imageUploadPath);
    }

    public String saveImage(MultipartFile imageFile, String subDirectory) throws IOException {
        Path subDirectoryPath = UPLOAD_PATH.resolve(subDirectory);

        if (!subDirectoryPath.toFile().exists()) {
            subDirectoryPath.toFile().mkdirs();
        }

        String originalFileName = imageFile.getOriginalFilename();
        String imageUrl = subDirectoryPath + "/" + originalFileName;

        Path destinationPath = Path.of(StringUtils.cleanPath(imageUrl));
        log.info("destinationPAth:" + destinationPath);
        imageFile.transferTo(destinationPath);

        return "/img/" + subDirectory + "/" + originalFileName;
    }

}

