package com.tadpole.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tadpole.entity.City;

public interface CityRepository extends JpaRepository<City, Integer> , JpaSpecificationExecutor<City>{
	
	public City findByName(String name);
}
