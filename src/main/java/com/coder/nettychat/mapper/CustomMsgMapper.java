package com.coder.nettychat.mapper;

import com.coder.nettychat.entity.vo.FriendRequestVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/12 17:24
 */
@Mapper
public interface CustomMsgMapper {

    /**
     * 对消息进行批量签收
     * @param ids 消息ID集合
     */
    void batchSignMsg(List<String> ids);

    /**
     * 根据账号id查找发送添加请求的好友列表
     * @param userId 我的账号id
     * @return 发送添加请求的好友列表
     */
    List<FriendRequestVO> getRequestByUserId(String userId);
}
