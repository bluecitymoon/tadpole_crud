package com.tadpole.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tadpole.entity.City;
import com.tadpole.entity.Dictionary;
import com.tadpole.entity.Menu;
import com.tadpole.entity.Province;
import com.tadpole.entity.Role;
import com.tadpole.entity.User;
import com.tadpole.repository.CityRepository;
import com.tadpole.repository.DropDownRepository;
import com.tadpole.repository.MenuRepository;
import com.tadpole.repository.ProvinceRepository;
import com.tadpole.repository.UserRepository;
import com.tadpole.service.CommonService;
import com.tadpole.util.SystemUtils;
import com.tadpole.vo.ResponseVo;

@Component("commonAction")
@Scope("prototype")
public class CommonAction extends AbstractAction {

	private static final long serialVersionUID = 7274858323873739463L;

	@Resource(name = "commonService")
	private CommonService commonService;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private DropDownRepository dropDownRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CityRepository cityRepository;

	private List<Dictionary> companyTypes;

	private List<Province> provinces;
	private List<City> cities;
	private List<Menu> menus;
	private User user;

	public String loadMe() {

		String username = SystemUtils.getCurrentUserName();

		user = userRepository.findByUsername(username);

		List<Role> roles = user.getRoles();
		for (Role role : roles) {
			role.setUsers(null);
		}
		return SUCCESS;

	}

	public String findAllProvinces() {

		String dataRange = getParameter("dataRange");

		if (StringUtils.isNotBlank(dataRange) && dataRange.equals("all")) {

			provinces = provinceRepository.findAll();

			for (Province province : provinces) {

				List<City> cities = province.getCitys();

				for (City city : cities) {
					city.setProvince(null);
				}
			}

		} else {
			provinces = commonService.findUserAssignedCities();
		}

		return SUCCESS;
	}

	public String findAllCities() {

		return SUCCESS;
	}

	public String resetMyPassword() {

		String oldPassword = getParameter("oldPassword");
		String newPassword = getParameter("newPassword");

		User currentLoggedInUser = commonService.getCurrentLoggedInUser();

		String encodedPassword = SystemUtils.getEncodedPassword(oldPassword, currentLoggedInUser.getUsername());
		if (encodedPassword.equals(currentLoggedInUser.getPassword())) {

			String newEncodedPassword = SystemUtils.getEncodedPassword(newPassword, currentLoggedInUser.getUsername());
			currentLoggedInUser.setPassword(newEncodedPassword);

			userRepository.saveAndFlush(currentLoggedInUser);

			setResponse(SystemUtils.makeGeneralSuccessResponse());
		} else {

			setResponse(ResponseVo.newFailMessage("你输入的旧密码不正确。"));

		}

		return SUCCESS;
	}

	public String findDropDownDataSouce() {

		String identityType = getParameter("identityType");

		companyTypes = dropDownRepository.findByIdentity(identityType);

		return SUCCESS;
	}

	public String findAllMenus() {

		menus = menuRepository.findAll();

		return SUCCESS;
	}

	public List<Menu> getMenus() {

		return menus;
	}

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	public void setMenus(List<Menu> menus) {

		this.menus = menus;
	}

	public List<Province> getProvinces() {

		return provinces;
	}

	public void setProvinces(List<Province> provinces) {

		this.provinces = provinces;
	}

	public List<City> getCities() {

		return cities;
	}

	public void setCities(List<City> cities) {

		this.cities = cities;
	}

	public List<Dictionary> getCompanyTypes() {

		return companyTypes;
	}

	public void setCompanyTypes(List<Dictionary> companyTypes) {

		this.companyTypes = companyTypes;
	}

}