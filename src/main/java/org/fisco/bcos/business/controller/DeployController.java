package org.fisco.bcos.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.param.MallSend2PlatformParam;
import org.fisco.bcos.business.param.PlatformRegisterParam;
import org.fisco.bcos.business.service.DeployService;
import org.fisco.bcos.business.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
@RequestMapping("/api/deploy")
@Slf4j
public class DeployController {

    private DeployService deployService;

    @Autowired
    DeployController(DeployService deployService) {
        this.deployService = deployService;
    }

    @PostMapping("/deployBAC001")
    public JsonData deployBAC001() throws Exception {
        log.info(">>>>>>>>>>>>deployBAC001");

        deployService.deployBAC001();
        return JsonData.success("deploy success");
    }

    @PostMapping("/deployBAC002")
    public JsonData deployBAC002() throws Exception {
        log.info(">>>>>>>>>>>>deployBAC002");

        deployService.deployBAC002();
        return JsonData.success("deploy success");
    }

    @PostMapping("/platform")
    public JsonData registerPlatform(PlatformRegisterParam param) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        log.info(">>>>>>>register platform: {}", param);

        deployService.registerPlatform(param);
        return JsonData.success("register platform success");
    }

    @PostMapping("/license")
    public JsonData license2Chain(long userId) throws Exception {
        log.info(">>>>>>>license2Chain");

        deployService.license2Chain(userId);
        return JsonData.success("license to chain success");
    }

    @PostMapping("/send")
    public JsonData mallSend2Platform(MallSend2PlatformParam param) throws Exception {
        log.info(">>>>>>>mallSend2Platform: {}", param);

        deployService.mallSend2Platform(param.getUserId(), BigInteger.valueOf(param.getPoint()));
        return JsonData.success("send success");
    }

    @GetMapping("/point/{userId}")
    public JsonData getBalance(@PathVariable("userId") long userId) throws Exception {
        log.info(">>>>>>>get {}'s balance", userId);
        return JsonData.success(deployService.getBalance(userId));
    }

}
