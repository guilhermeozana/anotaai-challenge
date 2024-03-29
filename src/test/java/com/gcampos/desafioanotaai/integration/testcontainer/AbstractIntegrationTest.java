package com.gcampos.desafioanotaai.integration.testcontainer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");

        private void startContainers() {
            Startables.deepStart(Stream.of(mongoDBContainer)).join();
        }

        private Map<String, String> createConnectionConfiguration() {
            String connectionString = "mongodb://" + mongoDBContainer.getHost() + ":" + mongoDBContainer.getMappedPort(27017);
            return Map.of(
                    "spring.data.mongodb.uri", connectionString
            );
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            MapPropertySource testContainers = new MapPropertySource("testcontainers", (Map) createConnectionConfiguration());

            environment.getPropertySources().addFirst(testContainers);
        }
    }
}

