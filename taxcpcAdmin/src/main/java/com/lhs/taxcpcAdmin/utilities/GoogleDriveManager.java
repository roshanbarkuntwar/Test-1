package com.lhs.taxcpcAdmin.utilities;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public class GoogleDriveManager{
	
	private static final String APPLICATION_NAME = "Google Drive API";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	private static final String CREDENTIALS_FILE_PATH = "credentials.json";
	
	public Drive getInstance() throws GeneralSecurityException, IOException {
	   // Build a new authorized API client service.
	   final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	   Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	         .setApplicationName(APPLICATION_NAME)
	         .build();
	   return service;
	}
	/**
	 * Creates an authorized Credential object.
	 *
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
	   // Load client secrets.
	 java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FILE_PATH);
	 
        if (!clientSecretFilePath.exists()) {
        	throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);      
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
      
	   // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	         HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	         .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
	         .setAccessType("offline")
	         .build();
      
      return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        
//	   LocalServerReceiver receiver = new LocalServerReceiver.Builder().build();
//	   return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}
	
	public List<File> listEverything() throws IOException, GeneralSecurityException {
		  // Print the names and IDs for up to 10 files.
		  FileList result = getInstance().files().list()
	        .setPageSize(10)
	        .setFields("nextPageToken, files(id, name)")
	        .execute();
	  return result.getFiles();
	}
	
	public String uploadFile(MultipartFile file, String filePath) {
	  try {
	     String folderId = getFolderId(filePath);
	     if (null != file) {
	        File fileMetadata = new File();
	        fileMetadata.setParents(Collections.singletonList(folderId));
	        fileMetadata.setName(file.getOriginalFilename());
	        File uploadFile = getInstance()
	              .files()
	              .create(fileMetadata, new InputStreamContent(
	                    file.getContentType(),
	                    new ByteArrayInputStream(file.getBytes()))
	              )
	              .setFields("id").execute();
	        return uploadFile.getId();
	     }
	  } catch (Exception e) {
	     e.printStackTrace();
	  }
	  return null;
	}
	
	public String getFolderId(String path) throws Exception {
	  String parentId = null;
	  String[] folderNames = path.split("/");
	  Drive driveInstance = getInstance();
	  for (String name : folderNames) {
	     parentId = findOrCreateFolder(parentId, name, driveInstance);
	  }
	  return parentId;
	}
	
	private String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
		  String folderId = searchFolderId(parentId, folderName, driveInstance);
		  // Folder already exists, so return id
		  if (folderId != null) {
		     return folderId;
		  }
		  //Folder dont exists, create it and return folderId
		  File fileMetadata = new File();
		  fileMetadata.setMimeType("application/vnd.google-apps.folder");
		  fileMetadata.setName(folderName);
		 
		  if (parentId != null) {
		     fileMetadata.setParents(Collections.singletonList(parentId));
		  }
		  return driveInstance.files().create(fileMetadata)
		        .setFields("id")
		        .execute()
		        .getId();
		}
	
	private String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
		  String folderId = null;
		  String pageToken = null;
		  FileList result = null;
		  File fileMetadata = new File();
		  fileMetadata.setMimeType("application/vnd.google-apps.folder");
		  fileMetadata.setName(folderName);
		  do {
		     String query = " mimeType = 'application/vnd.google-apps.folder' ";
		     if (parentId == null) {
		        query = query + " and 'root' in parents";
		     } else {
		        query = query + " and '" + parentId + "' in parents";
		     }
		     result = service.files().list().setQ(query)
		           .setSpaces("drive")
		           .setFields("nextPageToken, files(id, name)")
		           .setPageToken(pageToken)
		           .execute();
		     for (File file : result.getFiles()) {
		        if (file.getName().equalsIgnoreCase(folderName)) {
		           folderId = file.getId();
		        }
		     }
		     pageToken = result.getNextPageToken();
		  } while (pageToken != null && folderId == null);
		  return folderId;
		}
}


//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.FileList;
// 
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.List;
// 
//public class GoogleDriveManager {
// 
//    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
// 
//    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
// 
//    // Directory to store user credentials for this application.
//    private static final java.io.File CREDENTIALS_FOLDER //
//            = new java.io.File(System.getProperty("user.home"), "credentials");
// 
//    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";
// 
//    //
//    // Global instance of the scopes required by this quickstart. If modifying these
//    // scopes, delete your previously saved credentials/ folder.
//    //
//    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
// 
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
// 
//        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);
// 
//        if (!clientSecretFilePath.exists()) {
//            throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME //
//                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
//        }
// 
//        // Load client secrets.
//        InputStream in = new FileInputStream(clientSecretFilePath);
// 
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
// 
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
//                clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
//                        .setAccessType("offline").build();
// 
//        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//    }
// 
//    public void getDriveFiles() throws IOException, GeneralSecurityException {
// 
//        System.out.println("CREDENTIALS_FOLDER: " + CREDENTIALS_FOLDER.getAbsolutePath());
// 
//        // 1: Create CREDENTIALS_FOLDER
//        if (!CREDENTIALS_FOLDER.exists()) {
//            CREDENTIALS_FOLDER.mkdirs();
// 
//            System.out.println("Created Folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
//            System.out.println("Copy file " + CLIENT_SECRET_FILE_NAME + " into folder above.. and rerun this class!!");
//            return;
//        }
// 
//        // 2: Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
// 
//        // 3: Read client_secret.json file & create Credential object.
//        Credential credential = getCredentials(HTTP_TRANSPORT);
// 
//        // 5: Create Google Drive Service.
//        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
//                .setApplicationName(APPLICATION_NAME).build();
// 
//        // Print the names and IDs for up to 10 files.
//        FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
//        List<File> files = result.getFiles();
//        if (files == null || files.isEmpty()) {
//            System.out.println("No files found.");
//        } else {
//            System.out.println("Files:");
//            for (File file : files) {
//                System.out.printf("%s (%s)\n", file.getName(), file.getId());
//            }
//        }
//    }
//}