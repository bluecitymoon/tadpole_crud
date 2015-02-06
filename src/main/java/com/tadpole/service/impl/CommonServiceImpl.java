package com.tadpole.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.tadpole.entity.City;
import com.tadpole.entity.Province;
import com.tadpole.entity.Role;
import com.tadpole.entity.User;
import com.tadpole.repository.CityRepository;
import com.tadpole.repository.UserRepository;
import com.tadpole.service.CommonService;
import com.tadpole.util.SystemUtils;

@Service("commonService")
public class CommonServiceImpl implements CommonService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	public User getCurrentLoggedInUser() {

		String username = SystemUtils.getCurrentUserName();
		
		return userRepository.findByUsername(username);
		
	}

	public List<Province> findUserAssignedCities() {
		
		List<City> userCities = cityRepository.findByUsers(ImmutableList.of(getCurrentLoggedInUser()));
		
		List<Province> provinces = new ArrayList<Province>();
		
		for (City singleCity : userCities) {
			Province singleProvince = singleCity.getProvince();
			
			if (!provinces.contains(singleProvince)) {
				provinces.add(singleProvince);
			}
		}
		
		for (Province province : provinces) {
			
			List<City> cities = province.getCitys();
			List<City> assignedCities = new ArrayList<City>();
			for (City city : cities) {
				if (checkExistsInCityList(city, userCities)) {
					assignedCities.add(city);
				}
			}
			province.setCitys(assignedCities);
		}
		
		return provinces;
	}
	
	private boolean checkExistsInCityList(City city, List<City> cities) {
		for (City singleCity : cities) {
			if (singleCity.getId() == city.getId()) {
				return true;
			}
		}
		
		return false;
	}

	public boolean checkUserNotHasRole(String username, String roleName) {

		User userToCheck = userRepository.findByUsername(username);
		
		for ( Role role : userToCheck.getRoles()) {
			
			if (role.getName().equals(roleName)) {
				return false;
			}
		}
		
		return true;
	}
}
