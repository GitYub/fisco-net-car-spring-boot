package org.fisco.bcos.business.repository;

import org.fisco.bcos.business.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {

    UserEntity findByPhoneNumber(String phoneNumber);

}
