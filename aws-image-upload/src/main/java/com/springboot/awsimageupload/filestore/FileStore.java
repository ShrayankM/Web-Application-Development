package com.springboot.awsimageupload.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    @Autowired
    private final AmazonS3 s3;

    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void saveFile(String path, String fileName,
                         Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {

        ObjectMetadata metaData = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach((key, value) -> metaData.addUserMetadata(key, value));
            }
        });

        try {
            s3.putObject(path, fileName, inputStream, metaData);
        }
        catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload file to S3 bucket", e);
        }
    }
}
