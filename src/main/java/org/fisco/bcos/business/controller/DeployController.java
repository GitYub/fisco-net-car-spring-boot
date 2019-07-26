package org.fisco.bcos.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.BAC001;
import org.fisco.bcos.business.param.MallSend2PlatformParam;
import org.fisco.bcos.business.param.PlatformRegisterParam;
import org.fisco.bcos.business.service.DeployService;
import org.fisco.bcos.business.util.JsonData;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
@RequestMapping("/deploy")
@Slf4j
public class DeployController {

    private DeployService deployService;

    @Autowired
    DeployController(DeployService deployService) {
        this.deployService = deployService;
    }

    @PostMapping("/deploy")
    public JsonData deploy() throws Exception {
        log.info(">>>>>>>>>>>>deploy");

        deployService.deploy();
        return JsonData.success("deploy success");
    }

    @GetMapping("/totalAccount")
    public JsonData getTotalAccount() throws Exception {
        log.info(">>>>>>>>>>>>getTotalAccount");

        deployService.deploy();
        return JsonData.success("getTotalAccount success");
    }

    @PostMapping("/platform")
    public JsonData registerPlatform(PlatformRegisterParam param) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        log.info(">>>>>>>register platform: {}", param);

        deployService.registerPlatform(param);
        return JsonData.success("register platform success");
    }

    @PostMapping("/send")
    public JsonData mallSend2Platform(MallSend2PlatformParam param) throws Exception {
        log.info(">>>>>>>mallSend2Platform: {}", param);

        deployService.mallSend2Platform(param.getUserId(), BigInteger.valueOf(param.getPoint()));
        return JsonData.success("register platform success");
    }

}
