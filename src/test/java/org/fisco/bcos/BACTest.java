package org.fisco.bcos;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.service.DeployService;
import org.fisco.bcos.business.service.FiscoService;
import org.fisco.bcos.business.util.AddressConst;
import org.fisco.bcos.business.util.ReadFile;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

@Slf4j
public class BACTest extends BaseTest {

    @Autowired
    private Web3j web3j;

    @Autowired
    private FiscoService fiscoService;

    @Autowired
    private DeployService deployService;

    @Test
    public void testBAC002Send() {
        String[] a = ReadFile.toArrayByRandomAccessFile("classpath:车牌.txt");
        for (String b : a) {
            System.out.println(b);
        }
    }

    @Test
    public void testBAC002() throws Exception {
        Credentials credentials = Credentials.create("a2f393834219dbc85005929fe4e10c2252cd4771aeea9a5b052442dfa72e57bb");
        String addressTo = "0xe6019660ee4d811be0fec70244866416dfca1945";
        fiscoService.issueWithAssetURI(credentials, addressTo, BigInteger.ONE, "粤A12345", "发行商给用户34一块车牌");

        BAC002 bac002Bob = BAC002.load(AddressConst.BAC002_CONTRACT_ADDRESS, web3j, credentials, fiscoService.getGasProvider());
        System.out.println(bac002Bob.getContractAddress());

        System.out.println(bac002Bob.balance(addressTo).send().toString());
        System.out.println(bac002Bob.ownerOf(BigInteger.ONE).send());
    }

    @Test
    public void testBAC001() throws Exception {

        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("2100000000");
        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice,gasLimit);
//根据私钥导入账户
        Credentials credentials = Credentials.create("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");
        Credentials credentials1 = Credentials.create("29408348983086041980004411060899680998982890699252031460792827112295649001040");
        Credentials credentialsBob = Credentials.create("2");
// 生成随机私钥使用下面方法；
// Credentials credentialsBob =Credentials.create(Keys.createEcKeyPair());
        String Bob = "0x2b5ad5c4795c026514f8317c7a215e218dccd6cf";
        String Owner = "0x148947262ec5e21739fe3a931c29e8b84ee34a0f";

        String Alice = "0x1abc9fd9845cd5a0acefa72e4f40bcfd4136f864";
// 获取spring配置文件，生成上下文ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");


// 部署合约 即发行资产 资产描述：fisco bcos car asset; 资产简称 TTT; 最小转账单位 1 ;发行总量 10000000;
        org.fisco.bcos.BAC001 bac001 = org.fisco.bcos.BAC001.deploy(web3j, credentials, contractGasProvider, "GDX car asset", "TTT",BigInteger.valueOf(1), BigInteger.valueOf(1000000)).send();
        String contractAddress = bac001.getContractAddress();
//增加 发行者
        bac001.addIssuer(Alice).send();

// 增发资产
        bac001.issue(Alice, new BigInteger("10000"),"increase 10000 asset  ").send();
// 销毁资产, owner销毁自己的资产
        bac001.destroy(new BigInteger("10000"), "destroy 10000 asset").send();
//转账 以及转账备注 Owner -> Alice
        bac001.send(Alice,new BigInteger("10000"),"dinner money").send();
//Owner批准Bob可以从自己账户转走1000TTT
        bac001.approve(Bob, new BigInteger("10000")).send();
//Bob开始转走Owner 1000TTT ，需要先根据credentialsBob 重新load合约；
        org.fisco.bcos.BAC001 bac001Bob = org.fisco.bcos.BAC001.load(contractAddress,web3j,credentialsBob,contractGasProvider);
        bac001Bob.safeSendFrom(Owner,Alice,new BigInteger("10000"),"dddd").send();
//查询余额
    }




}
