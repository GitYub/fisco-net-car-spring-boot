package org.fisco.bcos.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.param.BuyParam;
import org.fisco.bcos.business.param.DriverParam;
import org.fisco.bcos.business.service.DriverService;
import org.fisco.bcos.business.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/driver")
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

    @PostMapping("/item")
    public JsonData buyItem(BuyParam param) {
        log.info(">>>>>>buy item");

      //  driverService.buyItem();
        return JsonData.success();
    }

}
