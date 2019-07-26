package org.fisco.bcos.business.service;

import org.fisco.bcos.business.param.DriverParam;

public interface DriverService {

    void finishOrder(DriverParam param) throws Exception;

    void buyItem();

}
