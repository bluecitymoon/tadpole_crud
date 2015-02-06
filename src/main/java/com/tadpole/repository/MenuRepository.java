package com.tadpole.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tadpole.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> , JpaSpecificationExecutor<Menu>{
}
