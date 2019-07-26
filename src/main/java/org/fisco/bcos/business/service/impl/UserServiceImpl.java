package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.dto.LoginDTO;
import org.fisco.bcos.business.entity.DriverEntity;
import org.fisco.bcos.business.entity.UserEntity;
import org.fisco.bcos.business.exception.ExistException;
import org.fisco.bcos.business.param.LoginParam;
import org.fisco.bcos.business.param.RegisterParam;
import org.fisco.bcos.business.repository.DriverRepository;
import org.fisco.bcos.business.repository.UserRepository;
import org.fisco.bcos.business.service.UserService;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private DriverRepository driverRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository, DriverRepository driverRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public void register(RegisterParam param) {
        log.info(">>>>>>register");

        //注册用户基本信息
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(param.getUsername());
        userEntity.setPassword(param.getPassword());
        userEntity.setPhoneNumber(param.getPhoneNumber());  //是否做已存在校验？
        userEntity.setRole(2);  //0为商城，1为网约车平台，2为司机，默认为2
        try {
            userEntity.setPrivateKey(Credentials.create(Keys.createEcKeyPair()).getEcKeyPair().getPrivateKey().toString(16));
        } catch (Exception e) {
            throw  new ExistException("私钥创建失败");
        }
        userRepository.save(userEntity);    //用户注册信息执行
        log.info("用户注册成功：" + userEntity.getUsername());

        //注册用户司机相关信息
        DriverEntity driverEntity = new DriverEntity();
        driverEntity.setUserId(userEntity.getId());
        driverEntity.setPlatform(param.getPlatform());  //设置关联平台，0为滴滴，1为美团，2为神州
        driverEntity.setMiles(0);   //初始里程设置为0
        driverEntity.setStar(new Random().nextInt(6));  //初始星级设置(0到5随机产生)
        driverRepository.save(driverEntity);    //司机注册信息执行
        log.info("司机同时注册成功：" + userEntity.getUsername());

    }

    @Override
    public LoginDTO login(LoginParam param) {
        log.info(">>>>>>login");

        UserEntity userEntity = userRepository.findByPhoneNumber(param.getPhoneNumber());
        if (userEntity == null) {
            throw new ExistException("手机号码不存在");
        }

        if (!userEntity.getPassword().equals(param.getPassword())) {
            throw new ExistException("手机号码或密码错误");
        }

        // TODO: 2019/7/25 从链上获取到司机的星级、积分

        return LoginDTO.builder()
                .score(1)
                .star(1)
                .build();

    }
}
