package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AutolocApiApplicationTests {

    @Test
    void contextLoads() {
        // The context is loaded by @SpringBootTest. If it fails, the test fails.
        assertNotNull(AutolocApiApplication.class);
    }
}