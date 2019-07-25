package org.fisco.bcos.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.param.LoginParam;
import org.fisco.bcos.business.param.RegisterParam;
import org.fisco.bcos.business.service.UserService;
import org.fisco.bcos.business.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public JsonData login(LoginParam param) {
        log.info(">>>>>>>login");
        userService.login(param);
        return JsonData.success();
    }

    @RequestMapping("/register")
    public JsonData register(RegisterParam param) {
        log.info(">>>>>>>>>register");
        userService.register(param);
        return JsonData.success();
    }

}
