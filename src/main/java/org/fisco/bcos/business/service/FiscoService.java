package org.fisco.bcos.business.service;

import org.fisco.bcos.web3j.crypto.Credentials;

import java.math.BigInteger;

public interface FiscoService {

    void send(Credentials credentials, String addressTo, BigInteger value, String data) throws Exception;

    void destroy(Credentials credentials, BigInteger value, String data) throws Exception;

    String balance(Credentials credentials, String addressTo) throws Exception;

    void issueWithAssetURI(Credentials credentials, String addressTo,
                           BigInteger value, String description, String data) throws Exception;

}
