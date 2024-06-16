package org.software.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NucleicAcidsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NucleicAcidsApplication.class, args);
    }
}
