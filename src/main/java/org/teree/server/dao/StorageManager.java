package org.teree.server.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.teree.shared.data.UserInfo;
import org.teree.shared.data.storage.ImageInfo;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Stateless
public class StorageManager {
	
	private static final String bucketName = "teree";

    @Inject
    private Logger _log;
	
	private AmazonS3 s3Client;
	
	private AmazonS3 getStorage() {
		if (s3Client == null) {
			try {
				s3Client = new AmazonS3Client(new PropertiesCredentials(StorageManager.class.getResourceAsStream("AwsCredentials.properties")));
			} catch (IOException ex) {
				_log.severe("Cannot get AWS credentials.");
			}
		}
		return s3Client;
	}

	public List<ImageInfo> getImages(String prefix, UserInfo ui) {
		if (ui == null) {
			return null;
		}
		if (prefix == null) {
			prefix = "";
		}
		List<ImageInfo> res = new ArrayList<ImageInfo>();
		
		/**ListObjectsRequest req = new ListObjectsRequest()
			.withBucketName(bucketName)
			.withPrefix(ui.getUserId()+"/"+prefix)
			.withDelimiter("/");

		// TODO: catch exceptions
		ObjectListing list = getStorage().listObjects(req);
		
		for (S3ObjectSummary objectSummary: list.getObjectSummaries()) {
		    String key = objectSummary.getKey();
		    ImageInfo i = new ImageInfo();
		    String url = "http://"+bucketName+".s3.amazonaws.com/"+key;
		    i.setUrl(url);
		    res.add(i);
		}*/
		
		loadTestImages(res);
		
		return res;
	}

	public List<ImageInfo> getPublicImages(String prefix) {
		if (prefix == null) {
			prefix = "";
		}
		List<ImageInfo> res = new ArrayList<ImageInfo>();
		
		/**ListObjectsRequest req = new ListObjectsRequest()
			.withBucketName(bucketName)
			.withPrefix("public/"+prefix)
			.withDelimiter("/");

		// TODO: catch exceptions
		ObjectListing list = getStorage().listObjects(req);
		
		for (S3ObjectSummary objectSummary: list.getObjectSummaries()) {
		    String key = objectSummary.getKey();
		    ImageInfo i = new ImageInfo();
		    String url = "http://"+bucketName+".s3.amazonaws.com/"+key;
		    i.setUrl(url);
		    res.add(i);
		}*/
		
		ImageInfo i1 = new ImageInfo();
		i1.setUrl("http://www.heppnetz.de/ontologies/goodrelations/goodrelations-UML-mini.png");
		res.add(i1);
		ImageInfo i2 = new ImageInfo();
		i2.setUrl("http://rdfs.org/images/back.gif");
		res.add(i2);
		
		return res;
	}
	
	private void loadTestImages(List<ImageInfo> list) {
		ImageInfo i1 = new ImageInfo();
		i1.setUrl("http://www.heppnetz.de/ontologies/goodrelations/goodrelations-UML-mini.png");
		list.add(i1);
		ImageInfo i2 = new ImageInfo();
		i2.setUrl("http://rdfs.org/images/back.gif");
		list.add(i2);
		ImageInfo i3 = new ImageInfo();
		i3.setUrl("http://rdfs.org/ontologies/sioc.png");
		list.add(i3);
		ImageInfo i4 = new ImageInfo();
		i4.setUrl("http://rdfs.org/ontologies/resume-rdf.png");
		list.add(i4);
	}
	
	public void uploadImage(String path, byte[] data, UserInfo ui) throws Exception {
		if (ui == null) {
			return;
		}

	    Long contentLength = Long.valueOf(data.length);
	    Long memUsed = ui.getMemUsed() + contentLength;
	    
	    if (memUsed > ui.getUserPackage().getMemLimit()) {
	    	throw new Exception("exceeds memory limit for user");
	    }

	    ObjectMetadata metadata = new ObjectMetadata();
	    metadata.setContentLength(contentLength);
	    
	    /**
		// TODO: catch exceptions
		PutObjectResult result = getStorage().putObject(new PutObjectRequest(bucketName, ui.getUserId()+"/"+path, new ByteArrayInputStream(data), metadata));
		*/
	    
    	ui.setMemUsed(memUsed);
	}
	
	public void deleteImage(String path, UserInfo ui) {
		if (ui == null) {
			return;
		}

		/**GetObjectRequest getReq = new GetObjectRequest(bucketName, path);
		S3Object obj = getStorage().getObject(getReq);
		Long contentLength = obj.getObjectMetadata().getContentLength();
		
		// TODO: catch exceptions
		getStorage().deleteObject(new DeleteObjectRequest(bucketName, ui.getUserId()+"/"+path));
		
	    ui.setMemUsed(ui.getMemUsed() - contentLength);*/
		
	}
    
}
