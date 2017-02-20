/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sakadream.lib.email.gmail.utils;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Profile;
import java.io.IOException;

/**
 * The Miscellaneous Utilities
 * @author Phan Ba Hai
 */
public class Misc {
    /**
     * Get Authorized Google Account email address
     * @param service The Gmail service
     * @param userId The email or keyword "me" (get authorized Google Account)
     * @return String
     * @throws IOException
     */
    public static String getUserEmailAddress(Gmail service, String userId) throws IOException {
        Profile profile = service.users().getProfile(userId).execute();
        return profile.getEmailAddress();
    }
}
