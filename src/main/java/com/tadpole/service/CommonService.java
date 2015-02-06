package com.tadpole.service;

import java.util.List;

import com.tadpole.entity.Province;
import com.tadpole.entity.User;


public interface CommonService {

	User getCurrentLoggedInUser();
	
	List<Province> findUserAssignedCities();
	
	boolean checkUserNotHasRole(String username, String roleName);
}
