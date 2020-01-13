package com.coder.nettychat.mapper;

import com.coder.nettychat.entity.FriendsRequest;
import com.coder.nettychat.entity.vo.FriendRequestVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/7 19:51
 */
@Mapper
public interface FriendsRequestMapper extends tk.mybatis.mapper.common.Mapper<FriendsRequest> {

    /**
     * 根据账号id查找发送添加请求的好友列表
     * @param userId 我的账号id
     * @return 发送添加请求的好友列表
     */
    @Select("select sender.id as sendUserId, sender.username as sendUsername, " +
            "sender.nickname as sendNickname, sender.face_image as sendFaceImage, " +
            "fr.request_message as verifyMessage, fr.request_status as requestStatus " +
            "from friends_request fr left join users sender " +
            "on fr.send_user_id = sender.id where fr.accept_user_id = #{userId} " +
            "order by fr.request_data_time desc")
    List<FriendRequestVO> getRequestByUserId(String userId);
}