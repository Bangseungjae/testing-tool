package com.bsj.dayonetest.service;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Client s3Client;

  public void putFile(String bucket, String key, File file) {
    s3Client.putObject(
        req -> {
          req.bucket(bucket);
          req.key(key);
        },
        RequestBody.fromFile(file));
  }

  public File getFile(String bucket, String key) {
    var file = new File("build/output/getFile.txt");

    var res =
        s3Client.getObject(
            req -> {
              req.bucket(bucket);
              req.key(key);
            });

    try {
      FileUtils.writeByteArrayToFile(file, res.readAllBytes());
    } catch (Exception e) {
      // ignore
    }
    return file;
  }
}
