package com.gcampos.desafioanotaai.integration.testcontainer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class MongoTest {

    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0.9"))
            .withExposedPorts(27017);

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void mongoDBProperties(DynamicPropertyRegistry registry) {
        registry.add("mongoDbUri", mongoDBContainer::getConnectionString);
    }

    @Test
    void test() {
        System.out.println("mongoDbUri: " + mongoDBContainer.getConnectionString());
    }

}
