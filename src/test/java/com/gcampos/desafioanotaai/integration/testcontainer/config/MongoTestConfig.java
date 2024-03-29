package com.gcampos.desafioanotaai.integration.testcontainer.config;

import com.gcampos.desafioanotaai.integration.testcontainer.MongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MongoTestConfig {

    @Bean
    public String mongoDbUri() {
        return MongoTest.mongoDBContainer.getConnectionString();
    }
}
