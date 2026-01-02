package indi.daniel.esbcommonservice.common.configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import indi.daniel.esbcommonservice.common.configuration.factory.YamlPropertySourceFactory;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;

@ConditionalOnClass(PooledPBEStringEncryptor.class)
@ConditionalOnProperty(value = "jasypt.enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableEncryptableProperties
@PropertySource(name = "EncryptedProperties", value = "classpath:salt.yaml", factory = YamlPropertySourceFactory.class)
public class JasyptInitiationConfiguration {

    private final Logger logger = LogManager.getLogger(JasyptInitiationConfiguration.class);

    @PostConstruct
    private void init() {
        logger.info("Started Jasypt");
    }
}
