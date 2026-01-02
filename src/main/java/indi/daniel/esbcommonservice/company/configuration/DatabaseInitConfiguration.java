package indi.daniel.esbcommonservice.company.configuration;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@ConditionalOnProperty(name = "spring.datasource.check.enabled", havingValue = "true", matchIfMissing = false)
public class DatabaseInitConfiguration {

    private static final Logger logger = LogManager.getLogger(DatabaseInitConfiguration.class);

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() throws SQLException {
        logger.info("Checking database connection on startup");
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) { // Check if the connection is valid within 1 second
                logger.info("Successfully connected to the database.");
            } else {
                logger.error("Database connection is not valid.");
                throw new RuntimeException("Database connection is not valid.");
            }
        } catch (SQLException e) {
            logger.error("Failed to connect to the database on startup: " + e.getMessage(), e);
            throw new RuntimeException("Failed to connect to the database on startup", e);
        }
    }
}
