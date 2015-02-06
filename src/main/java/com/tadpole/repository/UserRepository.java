package com.tadpole.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tadpole.entity.User;


public interface UserRepository extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User>{
	List<User> findByName(String name);
	List<User> findByUsernameAndPassword(String name, String password);
	
	User findByUsername(String username);
	
	User findByUsernameAndActive(String username, Boolean active);
}
