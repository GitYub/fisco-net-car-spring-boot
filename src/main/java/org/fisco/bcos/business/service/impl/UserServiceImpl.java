package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.dto.LoginDTO;
import org.fisco.bcos.business.entity.UserEntity;
import org.fisco.bcos.business.exception.ExistException;
import org.fisco.bcos.business.param.LoginParam;
import org.fisco.bcos.business.param.RegisterParam;
import org.fisco.bcos.business.repository.UserRepository;
import org.fisco.bcos.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(RegisterParam param) {
        log.info(">>>>>>register");

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
