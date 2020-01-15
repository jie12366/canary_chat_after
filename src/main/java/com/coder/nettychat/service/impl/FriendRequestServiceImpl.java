package com.coder.nettychat.service.impl;

import com.coder.nettychat.entity.FriendsRequest;
import com.coder.nettychat.entity.Users;
import com.coder.nettychat.entity.vo.FriendRequestVO;
import com.coder.nettychat.enums.RequestStatus;
import com.coder.nettychat.mapper.FriendsRequestMapper;
import com.coder.nettychat.service.FriendRequestService;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author monkJay
 * @description
 * @date 2020/1/11 0:10
 */
@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Resource
    FriendsRequestMapper friendsRequestMapper;

    @Resource
    UserServiceImpl userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendFriendRequest(FriendRequestVO friendRequestVO) {
        // 查找好友的账号信息
        Users users = userService.queryUsersByUsername(friendRequestVO.getAcceptUsername());
        // 判断这个添加好友的请求是否存在
        FriendsRequest friendsRequest = queryRequestIsExist(friendRequestVO.getSendUserId(), users.getId());
        // 如果请求不存在，再进行下一步，否则直接结束
        if (friendsRequest == null){
            // 新建一个添加好友请求并插入数据库中
            FriendsRequest request = new FriendsRequest();
            request.setId(Sid.nextShort());
            request.setSendUserId(friendRequestVO.getSendUserId());
            request.setAcceptUserId(users.getId());
            request.setRequestDataTime(new Date());
            // 添加好友的验证信息
            request.setRequestMessage(friendRequestVO.getVerifyMessage());
            // 接收者的备注
            request.setAcceptRemark(friendRequestVO.getAcceptRemark());
            request.setRequestStatus(RequestStatus.WAITING_VERIFY.message);
            friendsRequestMapper.insert(request);
        }
    }

    private FriendsRequest queryRequestIsExist(String myUserId, String friendUserId){
        Example example = new Example(FriendsRequest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId", myUserId);
        criteria.andEqualTo("acceptUserId", friendUserId);
        return friendsRequestMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public List<FriendRequestVO> queryRequestByUserId(String userId) {
        return friendsRequestMapper.getRequestByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRequestStatus(String userId, String friendId, Integer type) {
        Example example = new Example(FriendsRequest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendUserId", friendId);
        criteria.andEqualTo("acceptUserId", userId);
        // 先根据条件获取到FriendsRequest对象
        FriendsRequest friendsRequest = friendsRequestMapper.selectOneByExample(example);
        // 更新请求的状态
        friendsRequest.setRequestStatus(RequestStatus.getMsgByKey(type));
        // 更新请求
        friendsRequestMapper.updateByExampleSelective(friendsRequest, example);
    }

    @Override
    public boolean queryRequestIsHandled(String userId, String friendId) {
        FriendsRequest friendsRequest = queryRequestIsExist(friendId, userId);
        // 如果请求存在，且请求已经被处理过
        if (friendsRequest != null &&
                !friendsRequest.getRequestStatus().equals(RequestStatus.WAITING_VERIFY.message)){
            return true;
        }
        return false;
    }
}