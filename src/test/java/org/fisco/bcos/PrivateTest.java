package org.fisco.bcos;

import org.fisco.bcos.web3j.crypto.Credentials;
import org.junit.Test;

public class PrivateTest {

    @Test
    public void testPrivateKey() {
        Credentials credentials = Credentials.create("29408348983086041980004411060899680998982890699252031460792827112295649001040");
        System.out.println( credentials.getEcKeyPair().getPrivateKey().toString(16));
        System.out.println( credentials.getEcKeyPair().getPrivateKey().toString(10));

        String address ="0x0691b72dabc58ad95fe7be03df405cf5e3d706da";
        System.out.println(credentials.getAddress());

    }
}
