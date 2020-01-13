package com.coder.nettychat.mapper;

import com.coder.nettychat.entity.ChatMsg;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author monkJay
 * @description
 * @date 2020/1/7 19:51
 */
@Mapper
public interface ChatMsgMapper extends tk.mybatis.mapper.common.Mapper<ChatMsg> {
}
