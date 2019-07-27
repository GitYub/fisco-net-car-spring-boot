package org.fisco.bcos.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.param.BuyParam;
import org.fisco.bcos.business.param.DriverParam;
import org.fisco.bcos.business.service.DriverService;
import org.fisco.bcos.business.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/driver")
@Slf4j
public class DriverController {

    private DriverService driverService;

    @Autowired
    DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/order")
    public JsonData finishOrder(DriverParam param) throws Exception {
        log.info(">>>>>>finish order");

        driverService.finishOrder(param);
        return JsonData.success();
    }

    @PostMapping("/review")
    public JsonData review(long driverId) throws Exception {
        log.info(">>>>>>review");

        driverService.review(driverId);
        return JsonData.success();
    }

    @PostMapping("/item")
    public JsonData buyItem(BuyParam param) throws Exception {
        log.info(">>>>>>buy item");

        driverService.buyItem(param);
        return JsonData.success();
    }

    @DeleteMapping("/point")
    public JsonData resetPoint(long driverId, long point) throws Exception {
        log.info(">>>>>>resetPoint :{}", driverId);

        driverService.resetPoint(driverId, point);
        return JsonData.success();
    }

}
