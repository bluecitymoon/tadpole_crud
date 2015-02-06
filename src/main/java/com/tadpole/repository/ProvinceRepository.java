package com.tadpole.repository;


import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.tadpole.entity.Province;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public interface ProvinceRepository extends JpaRepository<Province, Integer> , JpaSpecificationExecutor<Province>{
	Province findByName(String name);
	
	ProvinceRepository getProvinceRepository();
	
	@Query(value="select id from ls_province", nativeQuery=true)
	List<Integer> getAllProvinceIds();
}
