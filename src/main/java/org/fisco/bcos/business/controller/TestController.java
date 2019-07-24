package org.fisco.bcos.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.BAC001;
import org.fisco.bcos.business.util.JsonData;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    private Web3j web3j;



    @Autowired
    TestController(Web3j web3j) {
        this.web3j = web3j;
    }

    @GetMapping("/string")
    public JsonData test() throws Exception {
        log.info("test");
        BigInteger blockNumber = web3j.getBlockNumber().send().getBlockNumber();
        log.info("blockNumber is {}", blockNumber);
        assertTrue(blockNumber.compareTo(new BigInteger("0")) >= 0);

        BigInteger pbft = web3j.getPbftView().send().getPbftView();
        String pdftStr = pbft.toString();

        log.info("pbftView is {}", pdftStr);

        String version = web3j.getNodeVersion().send().getNodeVersion().getVersion();

        log.info("version is {}", version);

        BigInteger minAmount = BigInteger.ONE;
        BigInteger maxAmount = BigInteger.TEN;
        BAC001 bac001 = BAC001.deploy(web3j, );
        BigInteger bigInteger = bac001.totalAmount().send().abs();

        return JsonData.success("123");
    }

}
