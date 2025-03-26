package ru.hogwarts.school.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class InfoService {
    @Value("${server.port}")
    private String port;

    Logger logger = LoggerFactory.getLogger(InfoService.class);

    public String getPort() {
        logger.info("getPort: {}", port);

        return port;
    }
}
