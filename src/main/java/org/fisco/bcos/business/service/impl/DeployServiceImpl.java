package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.BAC001;
import org.fisco.bcos.business.entity.UserEntity;
import org.fisco.bcos.business.param.PlatformRegisterParam;
import org.fisco.bcos.business.repository.UserRepository;
import org.fisco.bcos.business.service.DeployService;
import org.fisco.bcos.business.service.FiscoService;
import org.fisco.bcos.business.service.UserService;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
@Slf4j
public class DeployServiceImpl implements DeployService {

    private Web3j web3j;

    private UserRepository userRepository;

    private FiscoService fiscoService;

    @Autowired
    DeployServiceImpl(Web3j web3j, UserRepository userRepository, FiscoService fiscoService) {
        this.web3j = web3j;
        this.userRepository = userRepository;
        this.fiscoService = fiscoService;
    }

    @Override
    public void deploy() throws Exception {
        log.info(">>>>>>>>>deploy");

        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("210000000");

        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
        Credentials credentials = Credentials.create(Keys.createEcKeyPair());

        log.info("credentials address:{}", credentials.getAddress());
        log.info("credentials private key:{}", credentials.getEcKeyPair().getPrivateKey());
        log.info("credentials public key:{}", credentials.getEcKeyPair().getPublicKey());

        UserEntity userEntity = UserEntity.builder()
                .role(0)
                .privateKey(credentials.getEcKeyPair().getPrivateKey().toString())
                .phoneNumber("123456789")
                .password("123456789")
                .build();

        userRepository.save(userEntity);

        String description = "fisco net car integration";
        String shortName = "integration";
        BigInteger minAmount = BigInteger.valueOf(1);
        BigInteger maxAmount = BigInteger.valueOf(500000000);

        BAC001 bac001 = BAC001.deploy(web3j, credentials, contractGasProvider,
                description, shortName, minAmount, maxAmount).send();

        BigInteger totalAmount = bac001.totalAmount().send().abs();
        String contractAddress = bac001.getContractAddress();

        log.info("total amount is : {}", totalAmount);
        log.info("contract address is : {}", contractAddress);
    }

    @Override
    public void mallSend2Platform(long userId, BigInteger point) throws Exception {
        log.info(">>>>>>>>>mallSend2Platform");

        UserEntity mallUserEntity = userRepository.findByPhoneNumber("123456789");
        UserEntity platformUserEntity = userRepository.findById(userId);

        log.info("mallUserEntity is : {}", mallUserEntity);
        log.info("platformUserEntity is : {}", platformUserEntity);

        Credentials mallCredentials = Credentials.create(mallUserEntity.getPrivateKey());
        Credentials platformCredentials = Credentials.create(platformUserEntity.getPrivateKey());

        log.info("mallUserEntity's address is : {}", mallCredentials.getAddress());
        log.info("mallUserEntity's public key is : {}", mallCredentials.getEcKeyPair().getPublicKey().toString());
        log.info("mallUserEntity's private key is : {}", mallCredentials.getEcKeyPair().getPrivateKey().toString());

        log.info("platformUserEntity's address is : {}", platformCredentials.getAddress());
        log.info("platformUserEntity's public key is : {}", platformCredentials.getEcKeyPair().getPublicKey().toString());
        log.info("platformUserEntity's private key : {}", platformCredentials.getEcKeyPair().getPrivateKey().toString());

        fiscoService.send(mallCredentials, platformCredentials.getAddress(), point,
                "商城向平台" + platformUserEntity.getUsername() + "转账" + point.toString() + "积分");
    }

    @Override
    public void registerPlatform(PlatformRegisterParam param) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        log.info(">>>>>>>>>registerPlatform");

        Credentials credentials = Credentials.create(Keys.createEcKeyPair());

        UserEntity userEntity = UserEntity.builder()
                .password(param.getPassword())
                .phoneNumber(param.getPhoneNumber())
                .username(param.getUsername())
                .privateKey(credentials.getEcKeyPair().getPrivateKey().toString())
                .role(1)
                .build();
        userRepository.save(userEntity);
    }
}
