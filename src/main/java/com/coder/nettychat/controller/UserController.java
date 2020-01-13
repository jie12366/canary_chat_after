package com.coder.nettychat.controller;

import com.coder.nettychat.component.FastdfsClient;
import com.coder.nettychat.entity.Users;
import com.coder.nettychat.entity.bo.UsersBO;
import com.coder.nettychat.enums.ResultCode;
import com.coder.nettychat.service.UserService;
import com.coder.nettychat.utils.*;
import com.coder.nettychat.utils.file.Base64DecodeMultipartFile;
import com.coder.nettychat.utils.file.FileUtil;
import com.coder.nettychat.utils.result.JsonResult;
import com.coder.nettychat.utils.result.ReturnVOUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author monkJay
 * @description
 * @date 2020/1/7 23:36
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FastdfsClient fastdfsClient;

    @PostMapping("/login")
    public JsonResult registerOrLogin(@RequestBody Users user) {
        // 0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername())|| StringUtils.isBlank(user.getPassword())) {
            return JsonResult.failure(ResultCode.USER_OR_PWD_NULL);
        }

        // 1. 判断用户名是否存在，如果存在就登录，如果不存在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult;
        if (usernameIsExist) {
            // 1.1 登录
            userResult = userService.queryUserForLogin(user.getUsername(), Md5Util.getMd5(user.getPassword()));
            if (userResult == null) {
                return JsonResult.failure(ResultCode.USER_LOGIN_ERROR);
            }
        } else {
            // 1.2 注册
            user.setNickname(user.getUsername());
            user.setFaceImage("");
            user.setFaceImageBig("");
            user.setPassword(Md5Util.getMd5(user.getPassword()));
            userResult = userService.saveUser(user);
        }
        return JsonResult.success(ReturnVOUtil.copyToUsersVO(userResult));
    }

    /**
     * 上传用户头像
     * @param userBO 前端传来的参数封装的对象
     * @return JsonResult
     * @throws Exception
     */
    @PostMapping("/faceBase64")
    public JsonResult uploadFaceBase64(@RequestBody UsersBO userBO) throws Exception {
        // 获取前端传过来的base64字符串, 然后转换为文件对象再上传
        String base64Data = userBO.getFaceData();
        // 通过工具类将base64字符串转换为MultipartFile对象
        MultipartFile faceFile = Base64DecodeMultipartFile.base64ToMultipartFile(base64Data);
        String path = fastdfsClient.uploadBase64(faceFile);
        // 上传文件到fastdfs
        String url = FileUtil.IMG_SERVER_URL + path;

        // 获取缩略图的url
        // 缩略图需要在文件名后加上缩略图后缀
        String thump = "_80x80.";
        String[] arr = path.split("\\.");
        String thumpImgUrl = FileUtil.IMG_SERVER_URL + arr[0] + thump + arr[1];

        // 更新用户头像
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);
        Users result = userService.updateUserInfo(user);
        return JsonResult.success(ReturnVOUtil.copyToUsersVO(result));
    }

    @PostMapping("/nickname")
    public JsonResult setNickname(@RequestBody UsersBO usersBO){
        // 更新用户昵称
        Users user = new Users();
        user.setId(usersBO.getUserId());
        user.setNickname(usersBO.getNickname());
        Users result = userService.updateUserInfo(user);
        return JsonResult.success(ReturnVOUtil.copyToUsersVO(result));
    }
}