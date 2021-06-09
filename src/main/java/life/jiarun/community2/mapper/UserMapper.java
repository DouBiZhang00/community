package life.jiarun.community2.mapper;

import life.jiarun.community2.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

    @Mapper
    public interface UserMapper {
    //插入
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);
    //查询，在方法参数内部使用@Param("name")，将传入方法的参数解析进sql语句
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}

