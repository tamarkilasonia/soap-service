package ge.tbc.testautomation.mapper;

import ge.tbc.testautomation.model.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {

    @Select("SELECT * FROM users WHERE email = #{email}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "role", column = "role")
    })
    User getUserByEmail(String email);

    @Insert("INSERT INTO users (email, password, role) VALUES (#{email}, #{password}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Update("UPDATE users SET email = #{email} WHERE id = #{id}")
    int updateUserEmail(@Param("id") Long id, @Param("email") String email);
}