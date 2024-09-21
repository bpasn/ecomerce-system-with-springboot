package com.app.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.domain.repositories.IAuthRepository;
import com.app.infrastructure.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, String>, IAuthRepository {

}
