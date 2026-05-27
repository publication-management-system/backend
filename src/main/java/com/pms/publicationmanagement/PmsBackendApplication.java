package com.pms.publicationmanagement;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class PmsBackendApplication {

    public static void main(String[] args){
        SpringApplication.run(PmsBackendApplication.class);
    }
}
