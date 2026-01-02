package indi.daniel.esbcommonservice.common.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class BaseRepository {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected DataSource dataSource;

    protected <T> T convertValue(Map<String, Object> resultMap, Class<T> valueType) {
        return objectMapper.convertValue(resultMap, valueType);
    }

    protected <T> List<T> convertValue(List<Map<String, Object>> resultList, Class<T> valueType) {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionLikeType collectionLikeType = typeFactory.constructCollectionLikeType(List.class, valueType);
        return objectMapper.convertValue(resultList, collectionLikeType);
    }
}
