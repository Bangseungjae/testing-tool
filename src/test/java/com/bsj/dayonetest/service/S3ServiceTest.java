package com.bsj.dayonetest.service;

import static org.assertj.core.api.Assertions.*;

import com.bsj.dayonetest.annotation.SlowTest;
import com.bsj.dayonetest.testbase.IntegrationTest;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

class S3ServiceTest extends IntegrationTest {

  @Autowired private S3Service s3Service;

  @SlowTest
  void s3PutAndGetTest() throws IOException {
    // given
    var bucket = "test-bucket";
    var key = "sampleObject.xt";
    var sampleFile = new ClassPathResource("static/sample.txt").getFile();

    // when
    s3Service.putFile(bucket, key, sampleFile);

    // then
    var resultFile = s3Service.getFile(bucket, key);

    var sampleFileLines = FileUtils.readLines(sampleFile);
    var resultFileLines = FileUtils.readLines(resultFile);

    assertThatIterable(sampleFileLines).isEqualTo(resultFileLines);
  }
}
