package indi.daniel.esbcommonservice.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class HeaderUtils {

    // yyyyMMddHHmmss
    public String getTxnTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

    // 000 ~ 999
    public String getRandomNumber() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000));
    }
}
