package org.fisco.bcos.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.BAC001;
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
        log.info(">>>>>>>>>>>>>test");
        BigInteger blockNumber = web3j.getBlockNumber().send().getBlockNumber();
        log.info("blockNumber is {}", blockNumber);
        assertTrue(blockNumber.compareTo(new BigInteger("0")) >= 0);

        BigInteger pbft = web3j.getPbftView().send().getPbftView();
        String pdftStr = pbft.toString();

        log.info("pbftView is {}", pdftStr);

        String version = web3j.getNodeVersion().send().getNodeVersion().getVersion();

        log.info("version is {}", version);

        return JsonData.success("123");
    }

    @PostMapping("/deploy")
    public JsonData deploy() throws Exception {
        log.info(">>>>>>>>>>>>deploy");

        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("210000000");

        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
        Credentials credentials = Credentials.create("123456");
        Credentials.create(Keys.createEcKeyPair());

        String description = "fisco net car integration";
        String shortName = "integration";
        BigInteger minAmount = BigInteger.valueOf(1);
        BigInteger maxAmount = BigInteger.valueOf(10000000);

        BAC001 bac001 = BAC001.deploy(web3j, credentials, contractGasProvider,
                description, shortName, minAmount, maxAmount).send();

        BigInteger totalAmount = bac001.totalAmount().send().abs();
        String contractAddress = bac001.getContractAddress();

        log.info("total amount is : {}", totalAmount);
        log.info("contract address is : {}", contractAddress);

        return JsonData.success("123");
    }

}
