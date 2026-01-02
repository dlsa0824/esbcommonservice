package indi.daniel.esbcommonservice.company.repository;

import indi.daniel.esbcommonservice.common.repository.BaseRepository;
import indi.daniel.esbcommonservice.common.repository.DatabaseAccessor;
import indi.daniel.esbcommonservice.company.repository.model.Employee;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class EmployeeRepository extends BaseRepository {

    @Autowired
    DataSource dataSource;

    @Autowired
    DatabaseAccessor databaseAccessor;

    @Value("${esb.company.datatbase.schema}")
    protected String schema;

    private SimpleJdbcCall getEmployeeByEmployeeNumberCall;

    @PostConstruct
    private void init() {
        getEmployeeByEmployeeNumberCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(schema)
                .withProcedureName("sp_get_employee_by_employee_number")
                .declareParameters(
                        new SqlParameter("i_employee_number", Types.VARCHAR),
                        new SqlOutParameter("o_refcursor", Types.REF_CURSOR, new BeanPropertyRowMapper<>(Employee.class))
//                        new SqlOutParameter("o_refcursor", Types.REF_CURSOR)
                );
    }

    public List<Employee> findByEmployeeNumber(String employeeNumber) {
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("i_employee_number", employeeNumber);

        Map<String, Object> result = databaseAccessor.callStoredProcedure(getEmployeeByEmployeeNumberCall, in);

        @SuppressWarnings("unchecked")
        List<Employee> employees = (List<Employee>) result.get("o_refcursor");
        return employees;

//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> cursorMap = (List<Map<String, Object>>) result.get("o_refcursor");
//        if (cursorMap != null) {
//            return convertValue(cursorMap, Employee.class);
//        } else {
//            return Collections.emptyList();
//        }
    }
}
