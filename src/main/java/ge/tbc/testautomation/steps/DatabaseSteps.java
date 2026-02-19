package ge.tbc.testautomation.steps;

import ge.tbc.testautomation.config.DatabaseConfig;
import ge.tbc.testautomation.mapper.EmployeeMapper;
import ge.tbc.testautomation.mapper.UserMapper;
import ge.tbc.testautomation.model.Employee;
import ge.tbc.testautomation.model.User;
import io.qameta.allure.Step;
import org.apache.ibatis.session.SqlSession;

public class DatabaseSteps {

    @Step("Insert employee into database: {employee.email}")
    public void insertEmployee(Employee employee) {
        try (SqlSession session = DatabaseConfig.getSqlSession()) {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            mapper.insertEmployee(employee);
            session.commit();
        }
    }

    @Step("Get employee by ID: {id}")
    public Employee getEmployeeById(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSession()) {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            return mapper.getEmployeeById(id);
        }
    }

    @Step("Get employee by email: {email}")
    public Employee getEmployeeByEmail(String email) {
        try (SqlSession session = DatabaseConfig.getSqlSession()) {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            return mapper.getEmployeeByEmail(email);
        }
    }

    @Step("Update employee: {employee.email}")
    public int updateEmployee(Employee employee) {
        try (SqlSession session = DatabaseConfig.getSqlSession()) {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            int result = mapper.updateEmployee(employee);
            session.commit();
            return result;
        }
    }

    @Step("Delete employee by ID: {id}")
    public int deleteEmployee(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSession()) {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            int result = mapper.deleteEmployee(id);
            session.commit();
            return result;
        }
    }

    @Step("Count employee by ID: {id}")
    public int countEmployeeById(Long id) {
        try (SqlSession session = DatabaseConfig.getSqlSession()) {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            return mapper.countEmployeeById(id);
        }
    }

    @Step("Get user by email: {email}")
    public User getUserByEmail(String email) {
        try (SqlSession session = DatabaseConfig.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.getUserByEmail(email);
        }
    }

    @Step("Update user email - ID: {id}, New Email: {email}")
    public int updateUserEmail(Long id, String email) {
        try (SqlSession session = DatabaseConfig.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            int result = mapper.updateUserEmail(id, email);
            session.commit();
            return result;
        }
    }
}