package com.coder.nettychat.mapper;

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
}
