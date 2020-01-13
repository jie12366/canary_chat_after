package com.coder.nettychat.service;

import com.coder.nettychat.entity.Users;

/**
 * @author monkJay
 * @description
 * @date 2020/1/7 23:39
 */
public interface UserService {

    /**
     * 查询账号是否存在
     * @param username 账号
     * @return 是否存在
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 查询账号密码是否合法,存在则返回该账号
     * @param username 账号
     * @param pwd 密码
     * @return Users
     */
    Users queryUserForLogin(String username, String pwd);

    /**
     * 用户注册
     * @param users Users
     * @return Users
     */
    Users saveUser(Users users);

    /**
     * 更新账号信息
     * @param users Users
     * @return 更新后的账号信息
     */
    Users updateUserInfo(Users users);

    /**
     * 根据账号id查找用户
     * @param userId 账号id
     * @return Users
     */
    Users queryUsersById(String userId);

    /**
     * 根据用户账号查找用户信息
     * @param username 账号
     * @return 用户信息
     */
    Users queryUsersByUsername(String username);
}
