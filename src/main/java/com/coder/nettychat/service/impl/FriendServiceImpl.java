package com.coder.nettychat.service.impl;

import com.coder.nettychat.entity.MyFriends;
import com.coder.nettychat.entity.Users;
import com.coder.nettychat.entity.vo.UsersVO;
import com.coder.nettychat.enums.MsgAction;
import com.coder.nettychat.enums.SearchFriendsStatus;
import com.coder.nettychat.mapper.MyFriendsMapper;
import com.coder.nettychat.netty.MsgContent;
import com.coder.nettychat.netty.UserChannelRel;
import com.coder.nettychat.service.FriendService;
import com.coder.nettychat.utils.JsonUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/10 14:46
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    MyFriendsMapper myFriendsMapper;

    @Resource
    FriendRequestServiceImpl friendRequestService;

    @Resource
    UserServiceImpl userService;

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        Users users = userService.queryUsersByUsername(friendUsername);
        // 用户不存在
        if (users == null){
            return SearchFriendsStatus.USER_NOT_EXIST.status;
        }
        // 搜素的账号是自己
        if (users.getId().equals(myUserId)){
            return SearchFriendsStatus.NOT_YOURSELF.status;
        }
        Example example = new Example(MyFriends.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("myUserId", myUserId);
        criteria.andEqualTo("myFriendUserId", users.getId());
        MyFriends myFriends = myFriendsMapper.selectOneByExample(example);
        // 该账号已经是我的好友
        if (myFriends != null){
            return SearchFriendsStatus.ALREADY_FRIEND.status;
        }
        return SearchFriendsStatus.SUCCESS.status;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void passFriendRequest(String userId, String friendId, Integer type) {
        // 互相保存为好友
        saveFriend(userId, friendId);
        saveFriend(friendId, userId);
        // 更新请求状态为通过
        friendRequestService.updateRequestStatus(userId, friendId , type);
        // 使用websocket主动推送消息到请求发起者，使他拉取最新的好友列表
        Channel senderChannel = UserChannelRel.get(userId);
        if (senderChannel != null) {
            MsgContent msgContent = new MsgContent();
            msgContent.setAction(MsgAction.PULL_FRIEND.type);
            senderChannel.writeAndFlush(
                    new TextWebSocketFrame(JsonUtil.convertToJson(msgContent)));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFriend(String userId, String friendId) {
        MyFriends myFriends = new MyFriends();
        myFriends.setId(Sid.nextShort());
        myFriends.setMyUserId(userId);
        myFriends.setMyFriendUserId(friendId);
        // 保存我的好友
        myFriendsMapper.insert(myFriends);
    }

    @Override
    public List<UsersVO> queryMyFriendsList(String userId) {
        return myFriendsMapper.queryMyFriendsList(userId);
    }
}