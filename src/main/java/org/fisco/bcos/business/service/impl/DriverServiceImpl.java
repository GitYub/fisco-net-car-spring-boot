package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.entity.DriverEntity;
import org.fisco.bcos.business.entity.ItemEntity;
import org.fisco.bcos.business.entity.UserEntity;
import org.fisco.bcos.business.param.BuyParam;
import org.fisco.bcos.business.param.DriverParam;
import org.fisco.bcos.business.repository.DriverRepository;
import org.fisco.bcos.business.repository.ItemRepository;
import org.fisco.bcos.business.repository.UserRepository;
import org.fisco.bcos.business.service.DriverService;
import org.fisco.bcos.business.service.FiscoService;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {

    private DriverRepository driverRepository;

    private UserRepository userRepository;

    private FiscoService fiscoService;

    private ItemRepository itemRepository;

    @Autowired
    DriverServiceImpl(DriverRepository driverRepository,
                      FiscoService fiscoService,
                      UserRepository userRepository,
                      ItemRepository itemRepository) {
        this.driverRepository = driverRepository;
        this.fiscoService = fiscoService;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void finishOrder(DriverParam param) throws Exception {
        log.info(">>>>>>>>finishOrder");
        DriverEntity driverEntity = driverRepository.findById(param.getDriverId());
        UserEntity driverUserEntity = userRepository.findById(driverEntity.getUserId());

        log.info("driverEntity is : {}", driverEntity);
        log.info("driverUserEntity is : {}", driverUserEntity);

        if (param.getIsSafeDrive() == 1) {
            driverEntity.setMiles(driverEntity.getMiles() + param.getMiles());
            driverRepository.save(driverEntity);

            int platform = driverEntity.getPlatform();
            UserEntity platformEntity = null;

            switch (platform) {
                case 0:
                    platformEntity = userRepository.findByUsername("滴滴");
                    break;
                case 1:
                    platformEntity = userRepository.findByUsername("美团");
                    break;
                case 2:
                    platformEntity = userRepository.findByUsername("神州");
                    break;
                default:break;
            }

            log.info("platformEntity is : {}", platformEntity);

            if (platformEntity != null) {
                Credentials platformCredentials = Credentials.create(platformEntity.getPrivateKey());
                Credentials driverCredentials = Credentials.create(driverUserEntity.getPrivateKey());

                log.info("司机：{}完成安全驾驶，获得里程：{}，积分：{}",
                        platformEntity.getUsername(), driverEntity.getMiles(), driverEntity.getMiles());

                fiscoService.send(platformCredentials, driverCredentials.getAddress(),
                        BigInteger.valueOf(param.getMiles()),
                        "平台：" + platformEntity.getUsername() + "向司机：" + driverUserEntity.getUsername()
                        + "转账：" + param.getMiles());
            }

        }
    }

    @Override
    public void buyItem(BuyParam param) throws Exception {
        log.info(">>>>>>>>buyItem");

        ItemEntity itemEntity = itemRepository.findById(param.getItemId());
        long point = itemEntity.getPoint();

        DriverEntity driverEntity = driverRepository.findById(param.getDriverId());
        UserEntity driverUserEntity = userRepository.findById(driverEntity.getUserId());

        UserEntity mallUserEntity = userRepository.findByPhoneNumber("123456789");
        UserEntity platformUserEntity = userRepository.findById(mallUserEntity.getId());

        Credentials driverCredentials = Credentials.create(driverUserEntity.getPrivateKey());
        Credentials mallCredentials = Credentials.create(platformUserEntity.getPrivateKey());

        log.info("司机地址：{}", driverCredentials.getAddress());
        log.info("发行方地址：{}", mallCredentials.getAddress());

        fiscoService.send(driverCredentials, mallCredentials.getAddress(),
                BigInteger.valueOf(point),
                "司机:" + driverUserEntity.getUsername() + "花费：" + point + "购买" + itemEntity.getItemName());
        log.info("司机：{}向发行方购买：{}，花费积分：{}",
                driverUserEntity.getUsername(), itemEntity.getItemName(), point);
    }
}
