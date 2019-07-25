package org.fisco.bcos.business.service;

import org.fisco.bcos.business.param.LoginParam;
import org.fisco.bcos.business.param.RegisterParam;

public interface UserService {

    void register(RegisterParam param);

    void login(LoginParam param);

}
