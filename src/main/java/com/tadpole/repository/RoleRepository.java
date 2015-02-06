package com.tadpole.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tadpole.entity.City;
import com.tadpole.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> , JpaSpecificationExecutor<Role>{
}
