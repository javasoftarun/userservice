package com.yatranow.userservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {
	@PostConstruct
	public void initialize() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-service-account.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}
	}
}
