/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sakadream.lib.email.gmail.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.sakadream.lib.security.Security;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * GMail Authorize Utility
 * @author Phan Ba Hai
 */
public class GmailAuthorize {
    private static final String APPLICATION_NAME = "Gmail API";
    
    private static final File DATA_STORE_DIR = new File(".credenials/gmail-java");
    
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    
    private static final JsonFactory JSON_FACTORY = 
            JacksonFactory.getDefaultInstance();
    
    private static HttpTransport HTTP_TRANSPORT;
    
    private static final List<String> SCOPES = 
            Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);
    
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch(Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    private static Credential authorize() throws Exception {
        InputStream inFile =
            GmailAuthorize.class.getResourceAsStream("data");
        String dataString = IOUtils.toString(inFile, Charset.defaultCharset());
        String decryptedString = Security.decrypt(dataString);
        InputStream inString = 
                new ByteArrayInputStream(decryptedString.getBytes());
        
        GoogleClientSecrets clientSecrets = 
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inString));
        
        GoogleAuthorizationCodeFlow flow = 
                new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = 
                new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }
    
    /**
     * Get Gmail service for execute Gmail API calls
     * @return Gmail
     * @throws Exception
     */
    public static Gmail getGmailService() throws Exception {
        Credential credential = authorize();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
