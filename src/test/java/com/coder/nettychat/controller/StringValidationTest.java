package com.coder.nettychat.controller;

import com.coder.nettychat.entity.Users;
import com.coder.nettychat.entity.vo.FriendRequestVO;
import com.coder.nettychat.entity.vo.FriendsVO;
import com.coder.nettychat.enums.ResultCode;
import com.coder.nettychat.service.ChatMsgService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StringValidationTest {
    private static final String[] BLANK = {null, "", " \t\r\n", "\u2003", "\u3000"};

    @Test
    void rejectsBlankMessageIdsAndPassesNonblankIdsUnchanged() {
        ChatMsgController controller = new ChatMsgController();
        ChatMsgService service = mock(ChatMsgService.class);
        ReflectionTestUtils.setField(controller, "chatMsgService", service);
        for (String value : BLANK) {
            assertEquals(ResultCode.PARAM_IS_BLANK.code(), controller.getNotSignedMsg(value).getCode());
        }
        verifyZeroInteractions(service);
        for (String value : new String[] {"id", " id ", "\u00a0", "\u200b", "\u0000"}) {
            when(service.getUnsignedMsg(value)).thenReturn(Collections.emptyList());
            assertEquals(ResultCode.SUCCESS.code(), controller.getNotSignedMsg(value).getCode());
            verify(service).getUnsignedMsg(value);
        }
    }

    @Test
    void rejectsEachBlankLoginFieldBeforeCallingServices() {
        for (String field : new String[] {"username", "password"}) {
            for (String value : BLANK) {
                Users user = new Users();
                user.setUsername("user");
                user.setPassword("password");
                ReflectionTestUtils.setField(user, field, value);
                assertEquals(ResultCode.USER_OR_PWD_NULL.code(),
                        new UserController().registerOrLogin(user).getCode());
            }
        }
    }

    @Test
    void rejectsBlankFriendSearchFieldsBeforeCallingServices() {
        FriendsController controller = new FriendsController();
        for (String value : BLANK) {
            assertEquals(ResultCode.PARAM_IS_BLANK.code(), controller.addFriend(value).getCode());
            assertEquals(ResultCode.PARAM_IS_BLANK.code(), controller.searchFriend(value, "friend").getCode());
            assertEquals(ResultCode.PARAM_IS_BLANK.code(), controller.searchFriend("id", value).getCode());
        }
    }

    @Test
    void rejectsEachBlankFriendRequestFieldBeforeCallingServices() {
        String[] fields = {"sendUserId", "acceptUsername", "verifyMessage", "acceptRemark"};
        for (String field : fields) {
            for (String value : BLANK) {
                FriendRequestVO request = new FriendRequestVO();
                for (String validField : fields) {
                    ReflectionTestUtils.setField(request, validField, "valid");
                }
                ReflectionTestUtils.setField(request, field, value);
                assertEquals(ResultCode.PARAM_IS_BLANK.code(),
                        new FriendRequestController().addFriendRequest(request).getCode());
            }
        }
    }

    @Test
    void rejectsEachBlankFriendOperationFieldBeforeCallingServices() {
        String[] fields = {"userId", "friendId", "friendRemark", "myRemark"};
        for (String field : fields) {
            for (String value : BLANK) {
                FriendsVO request = new FriendsVO();
                for (String validField : fields) {
                    ReflectionTestUtils.setField(request, validField, "valid");
                }
                ReflectionTestUtils.setField(request, field, value);
                assertEquals(ResultCode.PARAM_NOT_COMPLETE.code(),
                        new FriendRequestController().operatorFriendRequest(request).getCode());
            }
        }
    }
}
