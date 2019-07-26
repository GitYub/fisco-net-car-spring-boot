package org.fisco.bcos.business.service;

import org.fisco.bcos.business.param.PlatformRegisterParam;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface DeployService {

    void deploy() throws Exception;

    void registerPlatform(PlatformRegisterParam param) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException;

    void mallSend2Platform(long userId, BigInteger point) throws Exception;

}
