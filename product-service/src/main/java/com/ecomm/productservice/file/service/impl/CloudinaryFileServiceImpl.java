package com.ecomm.productservice.file.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecomm.productservice.file.dto.response.FileResponse;
import com.ecomm.productservice.file.exception.InvalidFileException;
import com.ecomm.productservice.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@ConditionalOnProperty(name = "file.storage.provider", havingValue = "cloudinary")
@RequiredArgsConstructor
public class CloudinaryFileServiceImpl implements FileService {

    private final Cloudinary cloudinary;

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    @Override
    public FileResponse uploadImage(String path, MultipartFile file) {
        validateFile(file);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", path,
                            "resource_type", "image"
                    ));

            return new FileResponse(
                    (String) result.get("secure_url"),
                    (String) result.get("public_id")
            );

        } catch (IOException e) {
            throw new InvalidFileException("Cloudinary-ə yüklənərkən xəta baş verdi: " + e.getMessage());
        }
    }

    @Override
    public void deleteImage(String fileId) {
        if (fileId == null || fileId.isBlank()) return;
        try {
            cloudinary.uploader().destroy(fileId, ObjectUtils.emptyMap());
            log.info("Fayl Cloudinary-dən silindi: {}", fileId);
        } catch (IOException e) {
            log.warn("Cloudinary-dən fayl silinə bilmədi: {}", e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("Fayl boşdur");
        }
        if (file.getContentType() == null || !ALLOWED_TYPES.contains(file.getContentType())) {
            throw new InvalidFileException("Yalnız JPEG, PNG və WebP formatları qəbul edilir");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new InvalidFileException("Faylın ölçüsü 5MB-dan çox ola bilməz");
        }
    }
}
