/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sakadream.lib.email.gmail.utils;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Profile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Send Email Utility
 * @author Phan Ba Hai
 */
public class SendEmail {

    /**
     * Create a MimeMessage object
     * @param from
     * @param to
     * @param subject
     * @param bodyText
     * @return MimeMessage
     * @throws MessagingException
     */
    public static MimeMessage createEmail(String from,
            String to,
            String subject,
            String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        email.setSentDate(new Date());
        return email;
    }

    /**
     * Create a MimeMessage object with attachment
     * @param from
     * @param to
     * @param subject
     * @param bodyText
     * @param attachment
     * @return MimeMessage
     * @throws MessagingException
     * @throws IOException
     */
    public static MimeMessage createEmailWithAttachment(String from,
            String to,
            String subject,
            String bodyText,
            File attachment)
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setSentDate(new Date());

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachment);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(attachment.getName());

        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);

        return email;
    }

    /**
     * Convert MimeMessage to Gmail Message Model
     * @param emailContent The MimeMessage object
     * @return Message
     * @throws MessagingException
     * @throws IOException
     */
    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /**
     *
     * @param service The Gmail service
     * @param userId The email or keyword "me" (get authorized Google Account)
     * @param emailContent The MimeMessage object
     * @return Message
     * @throws MessagingException
     * @throws IOException
     */
    public static Message sendMessage(Gmail service,
            String userId,
            MimeMessage emailContent)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }
}
