package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.BAC001;
import org.fisco.bcos.business.dto.PublisherInfoDTO;
import org.fisco.bcos.business.entity.LicenseEntity;
import org.fisco.bcos.business.entity.UserEntity;
import org.fisco.bcos.business.param.PlatformRegisterParam;
import org.fisco.bcos.business.repository.LicenseRepository;
import org.fisco.bcos.business.repository.UserRepository;
import org.fisco.bcos.business.service.DeployService;
import org.fisco.bcos.business.service.FiscoService;
import org.fisco.bcos.business.util.AddressConst;
import org.fisco.bcos.business.util.ReadFile;
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

    private LicenseRepository licenseRepository;

    private UserRepository userRepository;

    private FiscoService fiscoService;

    @Autowired
    DeployServiceImpl(LicenseRepository licenseRepository, UserRepository userRepository, FiscoService fiscoService) {
        this.licenseRepository = licenseRepository;
        this.userRepository = userRepository;
        this.fiscoService = fiscoService;
    }

    @Override
    public void deployBAC001() throws Exception {
        log.info(">>>>>>>>>deployBAC001");

        Credentials credentials = Credentials.create("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");

        log.info("credentials address:{}", credentials.getAddress());
        log.info("credentials private key:{}", credentials.getEcKeyPair().getPrivateKey().toString(16));
        log.info("credentials public key:{}", credentials.getEcKeyPair().getPublicKey());

        String description = "fisco net car integration";
        String shortName = "integration";
        BigInteger minAmount = BigInteger.valueOf(1);
        BigInteger maxAmount = BigInteger.valueOf(500000000);

        PublisherInfoDTO publisherInfoDTO = fiscoService.deployBAC001(credentials, description, shortName, minAmount, maxAmount);

        UserEntity userEntity = UserEntity.builder()
                .role(0)
                .privateKey(publisherInfoDTO.getPrivateKey())
                .username("发行方")
                .address(publisherInfoDTO.getAddress())
                .phoneNumber("123456789")
                .password("123456789")
                .build();

        userRepository.save(userEntity);
    }

    @Override
    public void deployBAC002() throws Exception {
        log.info(">>>>>>>>>deployBAC002");

        Credentials credentials = Credentials.create(Keys.createEcKeyPair());

        log.info("credentials address:{}", credentials.getAddress());
        log.info("credentials private key:{}", credentials.getEcKeyPair().getPrivateKey().toString(16));
        log.info("credentials public key:{}", credentials.getEcKeyPair().getPublicKey());

        String description = "fisco net car license";
        String shortName = "license";

        PublisherInfoDTO publisherInfoDTO = fiscoService.deployBAC002(credentials, description, shortName);

        UserEntity userEntity = UserEntity.builder()
                .role(0)
                .privateKey(publisherInfoDTO.getPrivateKey())
                .username("BAC002发行方")
                .address(publisherInfoDTO.getAddress())
                .phoneNumber("12345678910")
                .password("12345678910")
                .build();

        userRepository.save(userEntity);
    }

    @Override
    public void license2Chain(long userId) throws Exception {
        log.info(">>>>>>>license2Chain");

        UserEntity mallUserEntity = userRepository.findByPhoneNumber("12345678910");
        Credentials credentials = Credentials.create(mallUserEntity.getPrivateKey());

        String[] lines = ReadFile.toArrayByRandomAccessFile("classpath:车牌.txt");
        log.info("Read from file:{}", (Object) lines);

        UserEntity driverEntity = userRepository.findById(userId);
        String addressTo = driverEntity.getAddress();

        LicenseEntity licenseEntity;
        for (String line: lines
             ) {
            licenseEntity = LicenseEntity.builder()
                    .description(line)
                    .build();
            licenseEntity = licenseRepository.save(licenseEntity);

            String data = "发行商给用户" + driverEntity.getUsername() + "一块车牌：" + line;
            fiscoService.issueWithAssetURI(credentials, addressTo,
                    BigInteger.valueOf(licenseEntity.getId()), licenseEntity.getDescription(), data);
        }


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
        log.info("mallUserEntity's public key is : {}", mallCredentials.getEcKeyPair().getPublicKey());
        log.info("mallUserEntity's private key is : {}", mallCredentials.getEcKeyPair().getPrivateKey());

        log.info("platformUserEntity's address is : {}", platformCredentials.getAddress());
        log.info("platformUserEntity's public key is : {}", platformCredentials.getEcKeyPair().getPublicKey());
        log.info("platformUserEntity's private key : {}", platformCredentials.getEcKeyPair().getPrivateKey());

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
                .address(credentials.getAddress())
                .privateKey(credentials.getEcKeyPair().getPrivateKey().toString(16))
                .role(1)
                .build();

        log.info("注册平台:{}的公钥：{}", param.getUsername(), credentials.getEcKeyPair().getPublicKey());
        log.info("注册平台:{}的私钥：{}", param.getUsername(), credentials.getEcKeyPair().getPrivateKey());
        log.info("注册平台:{}的地址：{}", param.getUsername(), credentials.getAddress());
        userRepository.save(userEntity);
    }

    @Override
    public String getBalance(long userId) throws Exception {
        log.info(">>>>>>>getBalance");
        UserEntity userEntity = userRepository.findById(userId);
        Credentials credentials = Credentials.create(userEntity.getPrivateKey());
        String balance = fiscoService.balance(credentials, credentials.getAddress());
        log.info(">>>>>>>getBalance result is ：{}", balance);
        return balance;
    }
}
