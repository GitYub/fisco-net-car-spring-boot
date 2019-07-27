package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.BAC001;
import org.fisco.bcos.BAC002;
import org.fisco.bcos.business.dto.PublisherInfoDTO;
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

        BAC001 bac001 = BAC001.load(AddressConst.BAC001_CONTRACT_ADDRESS, web3j, credentials, getGasProvider());
        log.info("load successful");
        log.info("totalAmount is : {}", bac001.totalAmount().send().toString());

        log.info("owner balance is : {}", bac001.balance(credentials.getAddress()).send());
        log.info("owner balance is : {}", bac001.balance("0x1e8e6930a2ae8aa209feba731ed3e3e6c31b4714").send());

        log.info("address :{}", addressTo);
        log.info("privatekey :{}", credentials.getEcKeyPair().getPrivateKey().toString(16));
        log.info("valid: {} ", bac001.isValid());
        try {
            bac001.send(addressTo, value, data).send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info(">>>>>>send successful");
    }

    @Override
    public void destroy(Credentials credentials, BigInteger value, String data) throws Exception {
        log.info(">>>>>>destroy");

        BAC001 bac001 = BAC001.load(AddressConst.BAC001_CONTRACT_ADDRESS, web3j, credentials, getGasProvider());
        bac001.destroy(value, "destroy asset").send();
    }

    @Override
    public String balance(Credentials credentials, String addressTo) throws Exception {
        log.info(">>>>>>balance");

        BAC001 bac001 = BAC001.load(AddressConst.BAC001_CONTRACT_ADDRESS, web3j, credentials, getGasProvider());
        log.info(">>>>>>balance successful");
        return bac001.balance(addressTo).send().toString();
    }

    @Override
    public PublisherInfoDTO deployBAC001(Credentials credentials, String description, String shortName,
                                         BigInteger minAmount, BigInteger maxAmount) throws Exception {
        log.info(">>>>>>>>deploy bac001");

        BAC001 bac001 = BAC001.deploy(web3j, credentials, getGasProvider(),
                description, shortName, minAmount, maxAmount).send();

        PublisherInfoDTO publisherInfoDTO = PublisherInfoDTO.builder()
                .publicKey(credentials.getEcKeyPair().getPublicKey().toString(16))
                .address(credentials.getAddress())
                .privateKey(credentials.getEcKeyPair().getPrivateKey().toString(16))
                .contractAddress(bac001.getContractAddress())
                .build();

        AddressConst.BAC001_CONTRACT_ADDRESS = publisherInfoDTO.getContractAddress();

        BigInteger totalAmount = bac001.totalAmount().send().abs();

        log.info("BAC001发行总量为: {}", totalAmount);
        log.info("BAC001发行商地址: {}", publisherInfoDTO.getAddress());
        log.info("BAC001发行商公钥 is : {}", publisherInfoDTO.getPublicKey());
        log.info("BAC001发行商私钥 is : {}", publisherInfoDTO.getPrivateKey());
        log.info("BAC001合约地址：{}", publisherInfoDTO.getContractAddress());

        return publisherInfoDTO;

    }

    @Override
    public PublisherInfoDTO deployBAC002(Credentials credentials, String description, String shortName) throws Exception {
        log.info(">>>>>>>>deploy bac002");

        BAC002 bac002 = BAC002.deploy(web3j, credentials, getGasProvider(), description, shortName).send();

        log.info("BAC002资产发行成功");

        PublisherInfoDTO publisherInfoDTO = PublisherInfoDTO.builder()
                .address(credentials.getAddress())
                .contractAddress(bac002.getContractAddress())
                .privateKey(credentials.getEcKeyPair().getPrivateKey().toString(16))
                .publicKey(credentials.getEcKeyPair().getPublicKey().toString(16))
                .build();

        log.info("BAC002合约地址：{}", publisherInfoDTO.getContractAddress());
        log.info("BAC002发行商地址：{}", publisherInfoDTO.getAddress());
        log.info("BAC002发行商公钥：{}", publisherInfoDTO.getPublicKey());
        log.info("BAC002发行商私钥：{}", publisherInfoDTO.getPrivateKey());

        return publisherInfoDTO;

    }

    @Override
    public void issueWithAssetURI(Credentials credentials, String addressTo,
                                  BigInteger value, String description, String data) throws Exception {
        log.info(">>>>>>issueWithAssetURI");

        BAC002 bac002 = BAC002.load(AddressConst.BAC002_CONTRACT_ADDRESS, web3j, credentials ,getGasProvider());
        bac002.issueWithAssetURI(addressTo, value, description, data.getBytes()).send();

        log.info("{}的资产：{}上链成功，资产id为：{}", addressTo, description, value);

    }

    @Override
    public ContractGasProvider getGasProvider() {
        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("210000000");
        return new StaticGasProvider(gasPrice, gasLimit);
    }

    public void test() {

    }
}
