package com.coder.nettychat.service.impl;

import com.coder.nettychat.component.FastdfsClient;
import com.coder.nettychat.entity.Users;
import com.coder.nettychat.mapper.UsersMapper;
import com.coder.nettychat.service.UserService;
import com.coder.nettychat.utils.file.Base64DecodeMultipartFile;
import com.coder.nettychat.utils.file.FileUtil;
import com.coder.nettychat.utils.LogUtil;
import com.coder.nettychat.utils.qrcode.QRCodeUtil;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author monkJay
 * @description
 * @date 2020/1/7 23:46
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UsersMapper usersMapper;

    @Resource
    FastdfsClient fastdfsClient;

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users res = usersMapper.selectOne(users);
        return res != null;
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public Users queryUserForLogin(String username, String pwd) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        // 对比数据库中的值和传入的值
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",pwd);
        return usersMapper.selectOneByExample(userExample);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Users saveUser(Users users) {
        // 生成一个唯一的ID
        String userId = Sid.nextShort();
        // 生成二维码
        users.setQrcode(createQrcode(users, null));
        // 为每个用户分配一个唯一ID
        users.setId(userId);
        usersMapper.insert(users);
        return users;
    }

    /**
     * 生成一个二维码，内容是账号id
     * @param users 账号信息
     * @param logoUrl logo的网络链接
     * @return 生成的二维码网络链接
     */
    private String createQrcode(Users users, String logoUrl){
        String qrcode = null;
        try {
            // 设置二维码生成的位置
            String qrCodePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\qrcode\\" + users.getId() + "qrcode.png";
            // 设置二维码的内容
            String content = "chat_qrcode:" + users.getUsername();
            // 如果没有设置logo，就使用默认的
            if (logoUrl == null){
                logoUrl = "https://pic.feizl.com/upload/allimg/190123/gxtxjslmijsvh4m.jpg";
            }
            // 生成二维码
            QRCodeUtil.encodeQRCode(content, qrCodePath, logoUrl);
            // 获取文件的字节流
            byte[] bytes = Files.readAllBytes(Paths.get(qrCodePath));
            // 将文件转换为base64
            String base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            // base64再转换为multipartFile对象
            MultipartFile multipartFile = Base64DecodeMultipartFile.base64ToMultipartFile(base64);
            // 将二维码图片上传到服务器
            qrcode = FileUtil.IMG_SERVER_URL + fastdfsClient.uploadQRCode(multipartFile);
            // 删除本地文件
            Files.delete(Paths.get(qrCodePath));
        } catch (IOException e) {
            LogUtil.error(e.getMessage(), e);
        }
        return qrcode;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Users updateUserInfo(Users users) {
        Users users1 = queryUsersById(users.getId());
        // 如果头像修改了，就修改相应的二维码的logo
        if (users.getFaceImage() != null && !users.getFaceImage().equals(users1.getFaceImage())){
            users.setQrcode(createQrcode(users1, users.getFaceImage()));
        }
        usersMapper.updateByPrimaryKeySelective(users);
        return queryUsersById(users.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public Users queryUsersById(String userId){
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public Users queryUsersByUsername(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return usersMapper.selectOneByExample(example);
    }
}