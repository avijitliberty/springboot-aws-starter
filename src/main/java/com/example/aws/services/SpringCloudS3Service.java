package com.example.aws.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.aws.exceptions.FileArchiveServiceException;
import com.example.aws.model.UserImage;

@Component
public class SpringCloudS3Service {

	private static final Logger logger = LoggerFactory.getLogger(SpringCloudS3Service.class);

	private static final String S3_BUCKET_NAME = "springboot-aws-starter-s3";

	@Autowired
	private AmazonS3Client amazonS3;

	@Autowired
	SpringCloudS3 springCloudS3;

	/**
	 * Save image to S3 and return CustomerImage containing key and public URL
	 * 
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	public UserImage saveFileToS3(MultipartFile multipartFile) throws FileArchiveServiceException {

		try {
			File fileToUpload = convertFromMultiPart(multipartFile);
			String key = Instant.now().getEpochSecond() + "_" + fileToUpload.getName();

			/* save file */
			springCloudS3.uploadS3Object(fileToUpload, key, S3_BUCKET_NAME);
			logger.info("{} file uploaded to S3", key);

			/* get signed URL (valid for 7 days) */
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(S3_BUCKET_NAME,
					key);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			generatePresignedUrlRequest.setExpiration(DateTime.now().plusDays(7).toDate());

			URL signedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

			return new UserImage(key, signedUrl.toString());
		} catch (Exception ex) {
			throw new FileArchiveServiceException("An error occurred saving file to S3", ex);
		}
	}

	/**
	 * Delete image from S3 using specified key
	 * 
	 * @param customerImage
	 */
	public void deleteImageFromS3(UserImage customerImage) {
		logger.info("Deleting S3 object: {}", customerImage.getKey());
		String resolvedBucketName = springCloudS3.resolveBucketName(S3_BUCKET_NAME);
		amazonS3.deleteObject(new DeleteObjectRequest(resolvedBucketName, customerImage.getKey()));
	}

	/**
	 * Convert MultiPartFile to ordinary File
	 * 
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	private File convertFromMultiPart(MultipartFile multipartFile) throws IOException {

		File file = new File(multipartFile.getOriginalFilename());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(multipartFile.getBytes());
		fos.close();

		return file;
	}

}
