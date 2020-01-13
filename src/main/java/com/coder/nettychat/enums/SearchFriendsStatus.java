package com.coder.nettychat.enums;

/**
 * @author monkJay
 * @description
 * @date 2020/1/10 14:38
 */
public enum SearchFriendsStatus {
    /**
     * 搜索好友的结果
     */
    SUCCESS(0, "OK"),
    USER_NOT_EXIST(1, "用户不存在"),
    NOT_YOURSELF(2, "不能添加自己"),
    ALREADY_FRIEND(3, "该账号已经是你的好友");

    /**
     * 搜索好友的状态
     */
    public final Integer status;

    /**
     * 状态对应的备注信息
     */
    public final String msg;

    SearchFriendsStatus(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }

    /**
     * 根据枚举的key获取对应的value
     * @param key 枚举的key
     * @return 对应的值
     */
    public static String getMsgByKey(Integer key){
        for (SearchFriendsStatus friendsStatus : values()){
            if (friendsStatus.getStatus().equals(key)){
                return friendsStatus.getMsg();
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
