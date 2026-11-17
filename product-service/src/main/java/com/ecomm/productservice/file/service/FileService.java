package com.ecomm.productservice.file.service;

import com.ecomm.productservice.file.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileResponse uploadImage(String path, MultipartFile file);

    void deleteImage(String fileId);
}
