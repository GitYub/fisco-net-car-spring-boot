package org.fisco.bcos.business.service;

import org.fisco.bcos.business.param.PlatformRegisterParam;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface DeployService {

    void registerPlatform(PlatformRegisterParam param) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException;

    void deployBAC001() throws Exception;

    void deployBAC002() throws Exception;

    void mallSend2Platform(long userId, BigInteger point) throws Exception;

    String getBalance(long userId) throws Exception;

    void license2Chain(long userId) throws Exception;

}
