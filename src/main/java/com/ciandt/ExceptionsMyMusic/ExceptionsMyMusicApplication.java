package com.ciandt.ExceptionsMyMusic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebMvc
@SpringBootApplication
@EnableFeignClients
public class ExceptionsMyMusicApplication {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ExceptionsMyMusicApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Iniciando aplicação Exceptions-MyMusic");
        SpringApplication.run(ExceptionsMyMusicApplication.class, args);
        LOGGER.info("Exceptions-MyMusic iniciado com sucesso!");
    }
}