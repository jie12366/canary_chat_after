package com.coder.nettychat.utils.result;

import com.coder.nettychat.entity.Users;
import com.coder.nettychat.entity.vo.UsersVO;
import org.springframework.beans.BeanUtils;

/**
 * @author monkJay
 * @description
 * @date 2020/1/10 15:20
 */
public class ReturnVOUtil {

    public static UsersVO copyToUsersVO(Users users){
        // 将需要的信息封装到VO类中
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users, usersVO);
        return usersVO;
    }
}