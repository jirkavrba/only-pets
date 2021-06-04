package dev.vrba.onlypets.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageUploadingService {

    private final AmazonS3 client;

    private final String bucketName;

    @Autowired
    public ImageUploadingService(AmazonS3 client, @Value("${aws.bucket}") String bucketName) {
        this.client = client;
        this.bucketName = bucketName;
    }

    public String uploadAvatar(@NotNull MultipartFile file, @NotNull UUID entityId) {
        String contentType = file.getContentType();

        if (!this.isValidAvatarContentType(contentType)) {
            throw new IllegalArgumentException("Invalid content type. Only png/jpg/jpeg images are supported");
        }

        String hash = DigestUtils.md5DigestAsHex(entityId.toString().getBytes(StandardCharsets.UTF_8));
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        String filename = String.format("%s.%s", hash, extension);

        return this.uploadFile("/avatars", filename, file);
    }

    private String uploadFile(@NotNull String directory, @NotNull String filename, @NotNull MultipartFile file) {
        try {
            String path = String.format("%s/%s", directory, filename);
            InputStream stream = file.getInputStream();
            ObjectMetadata meta = new ObjectMetadata();

            this.client.putObject(
                    this.bucketName,
                    path,
                    stream,
                    meta
            );

            return this.client.getUrl(this.bucketName, path).toString();
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to upload file to AWS.", exception);
        }
    }

    private boolean isValidAvatarContentType(@Nullable String contentType) {
        Set<String> valid = Set.of(
                "image/png",
                "image/jpg",
                "image/jpeg"
        );

        return valid.contains(contentType);
    }
}
