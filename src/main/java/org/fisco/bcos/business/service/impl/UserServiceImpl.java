package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
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
    public void login(LoginParam param) {
        log.info(">>>>>>login");

    }
}
