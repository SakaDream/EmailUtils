/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sakadream.lib.email.gmail.utils;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Receive Email Utility
 * @author Phan Ba Hai
 */
public class ReceiveEmail {

    /**
     * Get all emails in Mailbox
     * @param service The Gmail service
     * @param userId The email or keyword "me" (get authorized Google Account)
     * @return List 
     * @throws IOException
     */
    public static List<Message> getListOfEmails(Gmail service, String userId) throws IOException {
        return service.users().messages().list(userId).execute().getMessages();
    }
    
    /**
     * Get all emails in especially lableId
     * @param service The Gmail service
     * @param userId The email or keyword "me" (get authorized Google Account)
     * @param lable
     * @return List
     * @throws IOException
     */
    public static List<Message> getEmailsByLable(Gmail service, String userId, String lable) throws IOException {
        List<String> labelsId = new ArrayList<>();
        labelsId.add(lable);
        return service.users().messages().list(userId).set("LabelIds", lable).execute().getMessages();
    }
    
    /**
     * Get all emails by searching 
     * @param service The Gmail service
     * @param userId The email or keyword "me" (get authorized Google Account)
     * @param query The Gmail API search query
     * @return List
     * @throws IOException
     */
    public static List<Message> searchEmail(Gmail service, String userId, String query) throws IOException {
        return service.users().messages().list(userId).set("q", query).execute().getMessages();
    }
}
