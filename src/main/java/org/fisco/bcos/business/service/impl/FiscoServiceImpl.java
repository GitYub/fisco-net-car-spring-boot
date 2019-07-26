package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.BAC001;
import org.fisco.bcos.business.service.FiscoService;
import org.fisco.bcos.business.util.AddressConst;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Slf4j
public class FiscoServiceImpl implements FiscoService {

    private Web3j web3j;

    @Autowired
    FiscoServiceImpl(Web3j web3j) {
        this.web3j = web3j;
    }

    @Override
    public void send(Credentials credentials, String addressTo, BigInteger value, String data) throws Exception {
        log.info(">>>>>>send");

        BAC001 bac001 = BAC001.load(AddressConst.CONTRACT_ADDRESS, web3j, credentials, getGasProvider());
        log.info("load successful");

        bac001.send(addressTo, value, "send asset").send();
        log.info(">>>>>>send successful");
    }

    @Override
    public void destroy(Credentials credentials, BigInteger value, String data) throws Exception {
        log.info(">>>>>>destroy");

        BAC001 bac001 = BAC001.load(AddressConst.CONTRACT_ADDRESS, web3j, credentials, getGasProvider());
        bac001.destroy(value, "destroy asset").send();
    }

    @Override
    public String balance(Credentials credentials, String addressTo) throws Exception {
        log.info(">>>>>>balance");

        BAC001 bac001 = BAC001.load(AddressConst.CONTRACT_ADDRESS, web3j, credentials, getGasProvider());
        log.info(">>>>>>balance successful");
        return bac001.balance(addressTo).send().toString();
    }

    private ContractGasProvider getGasProvider() {
        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("210000000");
        return new StaticGasProvider(gasPrice, gasLimit);
    }
}
