package org.fisco.bcos.business.repository;

import org.fisco.bcos.business.entity.DriverEntity;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<DriverEntity, Long> {

    DriverEntity findById(long id);

}
