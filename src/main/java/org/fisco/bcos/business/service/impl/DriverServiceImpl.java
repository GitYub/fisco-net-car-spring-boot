package org.fisco.bcos.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.business.entity.DriverEntity;
import org.fisco.bcos.business.entity.UserEntity;
import org.fisco.bcos.business.param.DriverParam;
import org.fisco.bcos.business.repository.DriverRepository;
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

    @Autowired
    DriverServiceImpl(DriverRepository driverRepository,
                      FiscoService fiscoService,
                      UserRepository userRepository) {
        this.driverRepository = driverRepository;
        this.fiscoService = fiscoService;
        this.userRepository = userRepository;
    }

    @Override
    public void finishOrder(DriverParam param) throws Exception {
        log.info(">>>>>>>>finishOrder");
        DriverEntity driverEntity = driverRepository.findById(param.getDriverId());
        UserEntity driverUserEntity = userRepository.findById(driverEntity.getUserId());
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

            if (platformEntity != null) {
                Credentials platformCredentials = Credentials.create(platformEntity.getPrivateKey());
                Credentials driverCredentials = Credentials.create(driverUserEntity.getPrivateKey());

                fiscoService.send(platformCredentials, driverCredentials.getAddress(),
                        BigInteger.valueOf(param.getMiles()),
                        "平台：" + platformEntity.getUsername() + "向司机：" + driverUserEntity.getUsername()
                        + "转账：" + param.getMiles());
            }

        }
    }


}
