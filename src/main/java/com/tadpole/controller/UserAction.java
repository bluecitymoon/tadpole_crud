package com.tadpole.controller;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tadpole.constants.SystemConstants;
import com.tadpole.entity.City;
import com.tadpole.entity.Province;
import com.tadpole.entity.Role;
import com.tadpole.entity.User;
import com.tadpole.exception.ApplicationException;
import com.tadpole.repository.CityRepository;
import com.tadpole.repository.ProvinceRepository;
import com.tadpole.repository.RoleRepository;
import com.tadpole.repository.UserRepository;
import com.tadpole.service.UserService;
import com.tadpole.util.SystemUtils;
import com.tadpole.vo.ResponseVo;

@Component("userAction")
@Scope("prototype")
public class UserAction extends AbstractAction {

	private static final long serialVersionUID = -3519886427026056067L;
	private String username;
	private String name;

	@Autowired
	private RoleRepository roleRepository;

	private List<User> users;

	private Set<String> usersList;

	private User user;

	@Resource(name = "userService")
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private ProvinceRepository provinceRepository;

	private List<Role> roles;

	private List<City> chinaCities;
	private List<Province> chinaProvinces;

	private List<String> userOwnedCityIds;

	public void setUsername(String username) {

		this.username = username;
	}

	public String doLogin() {

		setupSession();

		String username = getParameter("username");
		String password = getParameter("password");

		return SUCCESS;
	}

	public String doLogoff() {

		setupSession();

		session.clear();

		return SUCCESS;

	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String loadGrabPage() {

		return SUCCESS;
	}

	public String loadAssignLocationToUser() {

		return SUCCESS;
	}

	public String ajaxFindUser() {

		String name = getParameter("userName");

		if (StringUtils.isEmpty(name)) {
			users = userRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
		} else {
			users = userService.findUserByName(name);
		}

		for (User user : users) {
			user.setRoles(null);
			user.setPassword("");
		}
		return SUCCESS;
	}

	/**
	 * for auto-complete plugin
	 * 
	 * @return
	 */
	public String getAllUserAccounts() {

		usersList = userService.findAllAccounts();

		return SUCCESS;
	}

	public String loadAllUsers() {

		users = userRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
		
		return SUCCESS;
	}

	public String createUser() {

		try {
			String userJson = getParameter("userJson");

			if (StringUtils.isEmpty(userJson)) {
				setResponse(SystemUtils.makeGeneralErrorResponse(null));
				return SUCCESS;
			}

			User userEntity = SystemUtils.getJavaObjectFromJsonString(userJson, User.class);

			// new user
			if (userEntity.getId() == null) {

				User userInDb = userRepository.findByUsername(userEntity.getUsername());

				if (userInDb == null) {

					String password = userEntity.getPassword();
					userEntity.setPassword(SystemUtils.getEncodedPassword(password, userEntity.getUsername()));
					userEntity.setActive(true);

				} else {

					setResponse(ResponseVo.newFailMessage(userEntity.getUsername() + "已经存在！"));

					return SUCCESS;
				}

			}

			userRepository.save(userEntity);

		} catch (Exception e) {
			setResponse(SystemUtils.makeGeneralErrorResponse(e));
			return SUCCESS;
		}
		setResponse(SystemUtils.makeGeneralSuccessResponse());

		return SUCCESS;
	}

	public String disactiveUser() {

		try {
			String userJson = getParameter("userJson");

			if (StringUtils.isEmpty(userJson)) {
				setResponse(SystemUtils.makeGeneralErrorResponse(null));
				return SUCCESS;
			}

			User userEntity = SystemUtils.getJavaObjectFromJsonString(userJson, User.class);

			userEntity.setActive(false);
			userEntity.setRoles(null);

			userRepository.save(userEntity);
		} catch (Exception e) {
			setResponse(SystemUtils.makeGeneralErrorResponse(e));
			return SUCCESS;
		}

		setResponse(SystemUtils.makeGeneralSuccessResponse());

		return SUCCESS;
	}

	public String updateUserRole() {

		try {
			String userJson = getParameter("userJson");
			String roleJson = getParameter("roleJson");
			String checkedOrNot = getParameter("checkedOrNot");

			User userFromClient = SystemUtils.getJavaObjectFromJsonString(userJson, User.class);
			Role roleFromClient = SystemUtils.getJavaObjectFromJsonString(roleJson, Role.class);

			User freshUser = userRepository.findOne(userFromClient.getId());
			Role freshRole = roleRepository.findOne(roleFromClient.getId());

			if (StringUtils.isNotBlank(checkedOrNot) && checkedOrNot.equals("true")) {

				freshUser.getRoles().add(freshRole);
				userRepository.save(freshUser);

			} else {

				freshUser.getRoles().remove(freshRole);
				userRepository.save(freshUser);
			}

		} catch (Exception e) {
			setResponse(SystemUtils.makeGeneralErrorResponse(e));
			return SUCCESS;
		}

		setResponse(SystemUtils.makeGeneralSuccessResponse());
		return SUCCESS;
	}

	public String resetPassword() {

		try {
			String userJson = getParameter("userJson");
			String newPasswordToReset = getParameter("newPasswordToReset");

			if (StringUtils.isEmpty(userJson)) {
				setResponse(SystemUtils.makeGeneralErrorResponse(null));
				return SUCCESS;
			}

			User userEntity = SystemUtils.getJavaObjectFromJsonString(userJson, User.class);

			User freshUserInDb = userRepository.findOne(userEntity.getId());

			freshUserInDb.setPassword(SystemUtils.getEncodedPassword(newPasswordToReset, freshUserInDb.getUsername()));

			userRepository.save(freshUserInDb);

		} catch (Exception e) {
			setResponse(SystemUtils.makeGeneralErrorResponse(e));
			return SUCCESS;
		}

		setResponse(SystemUtils.makeGeneralSuccessResponse());

		return SUCCESS;
	}

	public String getAllRoles() {

		roles = roleRepository.findAll();
		for (Role role : roles) {
			role.setUsers(null);
		}
		return SUCCESS;
	}

	public String updateUserCity() {

		try {
			String selectedCities = getParameter("selectedCities");

			String targetUserId = getParameter("userId");
			User userToAssign = null;
			if (StringUtils.isBlank(targetUserId)) {
				setResponse(SystemUtils.makeGeneralErrorResponse(new ApplicationException("未选择用户")));
				return SUCCESS;
			} else {
				userToAssign = userRepository.findOne(Integer.valueOf(targetUserId));
			}

			Object[] cityArray = JSONArray.fromObject(selectedCities).toArray();

			userService.updateUserCityAssignments(userToAssign, cityArray);

		} catch (Exception e) {

			setResponse(SystemUtils.makeGeneralErrorResponse(e));
			return SUCCESS;
		}

		setResponse(SystemUtils.makeGeneralSuccessResponse());

		return SUCCESS;
	}

	public String showAssignedCities() {

		String userId = getParameter("userId");

		if (StringUtils.isBlank(userId)) {
			setResponse(SystemUtils.makeGeneralErrorResponse(new ApplicationException("没有找到用户!")));

			return SUCCESS;
		}

		User targetUser = userRepository.findOne(Integer.valueOf(userId));

		List<City> userCities = cityRepository.findByUsers(ImmutableList.of(targetUser));
		for (City city : userCities) {
			if (null == userOwnedCityIds) {
				userOwnedCityIds = Lists.newArrayList();
			}

			userOwnedCityIds.add("city" + city.getId());
		}

		return SUCCESS;
	}

	public String loadChina() {

		chinaProvinces = provinceRepository.findAll();
		for (Province province : chinaProvinces) {
			List<City> citiesInTheProvince = province.getCitys();

			for (City singleCityInChina : citiesInTheProvince) {

				singleCityInChina.setProvince(null);
			}
		}

		return SUCCESS;
	}

	public List<City> getChinaCities() {

		return chinaCities;
	}

	public void setChinaCities(List<City> chinaCities) {

		this.chinaCities = chinaCities;
	}

	public String getUsername() {

		return username;
	}

	public List<User> getUsers() {

		return users;
	}

	public void setUsers(List<User> users) {

		this.users = users;
	}

	public Set<String> getUsersList() {

		return usersList;
	}

	public void setUsersList(Set<String> usersList) {

		this.usersList = usersList;
	}

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	public List<Role> getRoles() {

		return roles;
	}

	public void setRoles(List<Role> roles) {

		this.roles = roles;
	}

	public List<Province> getChinaProvinces() {

		return chinaProvinces;
	}

	public void setChinaProvinces(List<Province> chinaProvinces) {

		this.chinaProvinces = chinaProvinces;
	}

	public List<String> getUserOwnedCityIds() {

		return userOwnedCityIds;
	}

	public void setUserOwnedCityIds(List<String> userOwnedCityIds) {

		this.userOwnedCityIds = userOwnedCityIds;
	}

}