package indi.daniel.esbcommonservice.kafka;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class KafkaFilter implements Filter {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostConstruct
    private void init() {
        kafkaTemplate.getDefaultTopic();
        System.out.println("kafkaTemplate");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }
}
