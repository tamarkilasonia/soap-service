package ge.tbc.testautomation.mapper;

import ge.tbc.testautomation.model.Employee;
import org.apache.ibatis.annotations.*;

public interface EmployeeMapper {

    @Insert("INSERT INTO employee (firstName, lastName, email, department) " +
            "VALUES (#{firstName}, #{lastName}, #{email}, #{department})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertEmployee(Employee employee);

    @Select("SELECT * FROM employee WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "firstName"),
            @Result(property = "lastName", column = "lastName"),
            @Result(property = "email", column = "email"),
            @Result(property = "department", column = "department")
    })
    Employee getEmployeeById(Long id);

    @Select("SELECT * FROM employee WHERE email = #{email}")
    Employee getEmployeeByEmail(String email);

    @Update("UPDATE employee SET firstName = #{firstName}, lastName = #{lastName}, " +
            "email = #{email}, department = #{department} WHERE id = #{id}")
    int updateEmployee(Employee employee);

    @Delete("DELETE FROM employee WHERE id = #{id}")
    int deleteEmployee(Long id);

    @Select("SELECT COUNT(*) FROM employee WHERE id = #{id}")
    int countEmployeeById(Long id);
}