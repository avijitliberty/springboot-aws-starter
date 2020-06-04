package com.example.aws.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

@Component
public class SpringCloudS3 {
	
    ResourceLoader resourceLoader;
	ResourceIdResolver idResolver;
	
	@Autowired
	public SpringCloudS3(ResourceLoader loader, ResourceIdResolver idResolver) {
		this.resourceLoader = loader;
		this.idResolver = idResolver;
	}
	
	public Resource resolveAndLoad(String bucketName, String resourceName) {
		String resolvedBucketName = this.idResolver.
			resolveToPhysicalResourceId(bucketName);
		return this.resourceLoader.
				getResource("s3://" + resolvedBucketName + "/" + resourceName);
	}
	
	public String resolveBucketName(String bucketName) {
		String resolvedBucketName = this.idResolver.
			resolveToPhysicalResourceId(bucketName);
		return resolvedBucketName;
	}
	
    public void downloadS3Object(String bucketName, String resourceName) throws IOException {
        Resource resource = resolveAndLoad(bucketName, resourceName);
        File downloadedS3Object = new File(resource.getFilename());
        try (InputStream inputStream = resource.getInputStream()) {
            Files.copy(inputStream, downloadedS3Object.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void uploadS3Object(File file, String resourceName, String bucketName) throws IOException {
        WritableResource resource = (WritableResource) resolveAndLoad(bucketName, resourceName);
        try (OutputStream outputStream = resource.getOutputStream()) {
            Files.copy(file.toPath(), outputStream);
        }
    }
}
