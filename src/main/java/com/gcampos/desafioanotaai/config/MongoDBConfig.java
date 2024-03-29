package com.gcampos.desafioanotaai.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoDBConfig {

    @Value("${mongodb-uri}")
    private String mongoDbUri;

    @Bean
    public String mongoDbUri() {
        return mongoDbUri;
    }

    @Bean
    public MongoClient mongoClient(@Qualifier("mongoDbUri") String uri) {
        return MongoClients.create();
    }

}
