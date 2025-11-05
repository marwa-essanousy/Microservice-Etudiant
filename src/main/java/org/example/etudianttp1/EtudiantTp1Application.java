package org.example.etudianttp1;

import org.example.etudianttp1.configuration.RsaKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeys.class)
public class EtudiantTp1Application {

    public static void main(String[] args) {
        SpringApplication.run(EtudiantTp1Application.class, args);
    }

}
