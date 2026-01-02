package indi.daniel.esbcommonservice.common.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import indi.daniel.esbcommonservice.common.repository.model.SqlInfo;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseAccessor {

    private Logger logger = LogManager.getLogger(DatabaseAccessor.class);

    @Autowired
    private ObjectMapper objectMapper;

    private Logger traceSqlLogger;

    @Value("${esb.common.log.trace-sql.logger}")
    private String traceSqlLoggerName;

    @Value("${esb.common.log.trace-sql.enabled}")
    private boolean traceSqlEnabled;

    @PostConstruct
    private void init() {
        if (traceSqlEnabled) {
            traceSqlLogger = LogManager.getLogger(traceSqlLoggerName);
        }
    }

    public Map<String, Object> callStoredProcedure(SimpleJdbcCall simpleJdbcCall, SqlParameterSource SqlParameterSource) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = simpleJdbcCall.execute(SqlParameterSource);
        long endTime = System.currentTimeMillis();
        if (traceSqlEnabled) {
            SqlInfo sqlInfo = new SqlInfo();
            sqlInfo.setCallString(simpleJdbcCall.getCallString());
            sqlInfo.setCostTime(endTime - startTime);
            try {
                traceSqlLogger.info(objectMapper.writeValueAsString(sqlInfo));
            } catch (Exception e) {
                logger.error("Fail to log procedure: " + e.getMessage());
            }
        }
        return result;
    }
}
