package org.fisco.bcos.business.service;

import org.fisco.bcos.business.param.BuyParam;
import org.fisco.bcos.business.param.DriverParam;

public interface DriverService {

    void finishOrder(DriverParam param) throws Exception;

    void buyItem(BuyParam param) throws Exception;

    void review(long userId) throws Exception;

    void resetPoint(long driverId, long point) throws Exception;

}
