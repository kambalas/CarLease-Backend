package com.example.sick.service;

import com.example.sick.api.model.request.MailRequest;
import com.example.sick.api.model.response.MailResponse;
import com.example.sick.domain.MailDAORequest;
import com.example.sick.domain.MailDAOResponse;
import com.example.sick.repository.MailRepository;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailService implements EmailServiceInterface {

    private static final String TEST_EMAIL = "tlizingas@gmail.com";
    private final Gmail service;
    private final MailRepository mailRepository;

    @Autowired
    public EmailService(MailRepository mailRepository) throws Exception {
        this.mailRepository = mailRepository;
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("Test Mailer")
                .build();
    }

    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(EmailService.class.getResourceAsStream("/client_secret_865329521321-gslgitmmdj1jvmof4piq44ehb1je16l8.apps.googleusercontent.com.json")));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void sendMail(String recipient, String subject, String message) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(TEST_EMAIL));
        email.addRecipient(TO, new InternetAddress(recipient));
        email.setSubject(subject);
        email.setText(message);

        String htmlContent = message.replace("\n", "<br>");
        email.setContent(htmlContent, "text/html; charset=UTF-8");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
         service.users().messages().send("me", msg).execute();
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }


    public void saveMailHistory(MailRequest mailRequest) throws Exception{
        if (mailRequest == null || mailRequest.mailText() == null || mailRequest.mailText().isEmpty()) {
            throw new IllegalArgumentException("Mail request must not be null");
        }
        try {
            sendMail(mailRequest.mailRecipient(), mailRequest.mailSubject(), mailRequest.mailText());
            mailRepository.createMail(convertMailRequestIntoMailDAORequest(mailRequest));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MailResponse> getMailByApplicationId(long applicationId){
        return mailRepository.selectMailByApplicationId(applicationId).stream()
                .map(this::convertMailDAOResponseIntoMailResponse)
                .toList();
    }

    private MailDAORequest convertMailRequestIntoMailDAORequest(MailRequest mailRequest) {
        return new MailDAORequest(
                mailRequest.applicationId(),
                mailRequest.mailText()
        );
    }

    private MailResponse convertMailDAOResponseIntoMailResponse(MailDAOResponse mail) {
        return new MailResponse(
                mail.mailText()
        );
    }

}