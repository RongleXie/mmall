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
     * @param username 用户名
     * @return int
     * @author ronglexie
     * @version 2018/4/6
     */
    int checkUsername(@Param("username") String username);

    /**
     * 检查用户邮箱是否存在
     * @param email 邮箱
     * @return int
     * @author ronglexie
     * @version 2018/4/6
     */
    int checkEmail(@Param("email") String email);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return com.ronglexie.mmall.domain.User
     * @author ronglexie
     * @version 2018/4/6
     */
    User selectLogin(@Param("username") String username, @Param("password") String password);

	/**
	 * 根据用户名查询用户找回密码问题
	 *
	 * @param username 用户名
	 * @return java.lang.String
	 * @author wxt.xqr
	 * @version 2018/4/7
	 */
	String selectQuestionByusername(@Param("username") String username);

	/**
	 * 校验找回密码问题答案是否正确
	 *
	 * @param username 用户名
	 * @param question 问题
	 * @param answer 答案
	 * @return int
	 * @author wxt.xqr
	 * @version 2018/4/7
	 */
	int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);
}