package com.ronglexie.mmall.dao;

import com.ronglexie.mmall.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Dao层
 *
 * @author ronglexie
 * @version 2018/4/6
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 检查用户名是否存在
     * @param username
     * @return int
     * @author ronglexie
     * @version 2018/4/6
     */
    int checkUsername(String username);

    /**
     * 检查用户邮箱是否存在
     * @param email
     * @return int
     * @author ronglexie
     * @version 2018/4/6
     */
    int checkEmail(String email);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return com.ronglexie.mmall.domain.User
     * @author ronglexie
     * @version 2018/4/6
     */
    User selectLogin(@Param("username") String username, @Param("password") String password);


}