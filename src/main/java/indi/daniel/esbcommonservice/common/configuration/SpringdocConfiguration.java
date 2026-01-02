package indi.daniel.esbcommonservice.common.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "springdoc")
@Configuration
@Data
public class SpringdocConfiguration {

    private Info info;

    private List<Server> servers;

    private Map<String, SecurityScheme> securitySchemas;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        if (info != null) {
            openAPI.setInfo(info);
        }

        if (servers != null) {
            openAPI.setServers(servers);
        }

        if (securitySchemas != null && !securitySchemas.isEmpty()) {
            Components components = new Components();
            securitySchemas.forEach(components::addSecuritySchemes);
            openAPI.components(components);
            securitySchemas.keySet().forEach(key -> openAPI.addSecurityItem(new SecurityRequirement().addList(key)));
        }

        return openAPI;
    }
}
