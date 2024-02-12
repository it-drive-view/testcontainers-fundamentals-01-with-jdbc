package com.coussy.testcontainer.crud;

import com.coussy.testcontainer.Post;
import com.coussy.testcontainer.PostRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

// 1- we need to create 'post' table in the postgres database
// 2- we are in jdbc only here, not in jpa, we have no @Entity to generate it automatically. So we have to find another way.
// 3- here we have a script called 'schema.sql' which should generate the table
// 4- but we force the non execution of the script by the property : spring.sql.init.mode=never (below, within the annotation @DataJdbcTest)
// 5- the 'post' table will be created by the 'table-creation.sql' while docker initialization

@Testcontainers
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TableCreatedByDockerScriptTest {

    @Autowired
    PostRepository postRepository;

    Logger logger = LoggerFactory.getLogger(TableCreatedByDockerScriptTest.class);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0")
            .withClasspathResourceMapping("PostgresInit.sql", "/docker-entrypoint-initdb.d/script.sql", BindMode.READ_ONLY);
//            .withClasspathResourceMapping("table-creation.sql", "/docker-entrypoint-initdb.d/script.sql", BindMode.READ_ONLY);

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllPosts() {

        Post e2 = new Post(2, 1, "Hello, World! 1", "This is my first post!", null);
        postRepository.save(e2);

        Post post = postRepository.findByTitle("Hello, World! 1").orElseThrow();
        assertEquals("Hello, World! 1", post.title(), "Post title should be 'Hello, World!'");
    }

}