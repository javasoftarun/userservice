package com.yatranow.userservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initialize() {
        try {
        	String base64Key = System.getenv("FIREBASE_SERVICE_ACCOUNT");
        	if (base64Key == null) {
        	    throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT env variable not set");
        	}
        	byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        	FirebaseOptions options = new FirebaseOptions.Builder()
        	        .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(decodedKey)))
        	        .build();
        	if (FirebaseApp.getApps().isEmpty()) {
        	    FirebaseApp.initializeApp(options);
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
