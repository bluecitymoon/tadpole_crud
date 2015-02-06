package com.tadpole.script;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ImmutableList;
import com.tadpole.entity.JobScheduleConfiguration;
import com.tadpole.entity.Menu;
import com.tadpole.entity.Role;
import com.tadpole.entity.User;
import com.tadpole.repository.CityRepository;
import com.tadpole.repository.DropDownRepository;
import com.tadpole.repository.JobScheduleConfigurationRepository;
import com.tadpole.repository.MenuRepository;
import com.tadpole.repository.ProvinceRepository;
import com.tadpole.repository.RoleRepository;
import com.tadpole.repository.UserRepository;

/**
 * This class is for basic data preparation for the web app starting up. using this class without retrieving data from the UI. The data must not be
 * changed.
 * 
 * @author jjiang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestInitializationScripts {

	@Autowired
	private DropDownRepository dropDownRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private JobScheduleConfigurationRepository jobScheduleConfigurationRepository;

	@Test
	public void testInitialConfiguration() {

		JobScheduleConfiguration jobScheduleConfiguration = new JobScheduleConfiguration();

		jobScheduleConfiguration.setFeUrlStartHour(20);
		jobScheduleConfiguration.setFeUrlStartMinute(10);
		jobScheduleConfiguration.setGjUrlStartHour(20);
		jobScheduleConfiguration.setGjUrlStartMinute(20);
		jobScheduleConfiguration.setOteUrlStartHour(20);
		jobScheduleConfiguration.setOteUrlStartMinute(30);

		jobScheduleConfiguration.setFeStartHour(2);
		jobScheduleConfiguration.setFeStartMinute(2);
		jobScheduleConfiguration.setGjStartHour(2);
		jobScheduleConfiguration.setGjStartMinute(2);
		jobScheduleConfiguration.setOteStartHour(2);
		jobScheduleConfiguration.setOteStartMinute(2);

		jobScheduleConfigurationRepository.save(jobScheduleConfiguration);

	}

	@Test
	public void testInitialMenus() {

		Menu menu2 = new Menu("用户管理", "/crud/admin/loadUser.ls", "userManager");
		Menu menu3 = new Menu("系统开发", "/crud/admin/configuration.ls", "systemConfiguration");

		menuRepository.save(ImmutableList.of(menu2, menu3));

	}

	@Test
	public void testCreateUserAndRoles() {

		User adminUser = new User("Jerry Jiang", "admin", getEncodedPassword("admin", "admin"), true);

		Role superAdmin = new Role("ROLE_ADMIN", "超管");

		adminUser.setRoles(ImmutableList.of(superAdmin));

		List<Role> roles = ImmutableList.of(superAdmin);

		List<User> users = ImmutableList.of(adminUser);

		roleRepository.save(roles);

		userRepository.save(users);

	}

	private String getEncodedPassword(String password, String username) {

		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);
		return shaPasswordEncoder.encodePassword(password, username);
	}

	@Test
	public void testActiveUsers() {

		List<User> users = userRepository.findAll();
		for (User user : users) {
			user.setActive(true);
		}

		userRepository.save(users);
	}

	@Test
	public void testJson() {

		BarChartData barChartData = new BarChartData();

		List<String> labels = ImmutableList.of("Shanghai", "BeiJing");
		barChartData.setLabels(labels);

		Data data = new Data();
		data.setData(ImmutableList.of("1110", "233"));
		barChartData.setDatasets(ImmutableList.of(data));

		System.out.println(JSONObject.fromObject(barChartData));
	}

	/**
	 * var barChartData = { labels : [ "January", "February", "March", "April", "May", "June", "July" ], datasets : [ { fillColor :
	 * "rgba(220,220,220,0.5)", strokeColor : "rgba(220,220,220,0.8)", highlightFill : "rgba(220,220,220,0.75)", highlightStroke :
	 * "rgba(220,220,220,1)", data : [ randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(),
	 * randomScalingFactor(), randomScalingFactor(), randomScalingFactor() ] }, { fillColor : "rgba(151,187,205,0.5)", strokeColor :
	 * "rgba(151,187,205,0.8)", highlightFill : "rgba(151,187,205,0.75)", highlightStroke : "rgba(151,187,205,1)", data : [ randomScalingFactor(),
	 * randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor() ] } ]
	 * }
	 */
	public class BarChartData {

		private List<String> labels;
		private List<Data> datasets;

		public List<String> getLabels() {

			return labels;
		}

		public void setLabels(List<String> labels) {

			this.labels = labels;
		}

		public List<Data> getDatasets() {

			return datasets;
		}

		public void setDatasets(List<Data> datasets) {

			this.datasets = datasets;
		}

	}

	public class Data {

		private String fillColor = "rgba(220,220,220,0.5)";
		private String strokeColor = "rgba(220,220,220,0.8)";
		private String highlightFill = "rgba(220,220,220,0.75)";
		private String highlightStroke = "rgba(220,220,220,1)";

		private List<String> data = new ArrayList<String>();

		public String getFillColor() {

			return fillColor;
		}

		public void setFillColor(String fillColor) {

			this.fillColor = fillColor;
		}

		public String getStrokeColor() {

			return strokeColor;
		}

		public void setStrokeColor(String strokeColor) {

			this.strokeColor = strokeColor;
		}

		public List<String> getData() {

			return data;
		}

		public void setData(List<String> data) {

			this.data = data;
		}

		public String getHighlightFill() {

			return highlightFill;
		}

		public void setHighlightFill(String highlightFill) {

			this.highlightFill = highlightFill;
		}

		public String getHighlightStroke() {

			return highlightStroke;
		}

		public void setHighlightStroke(String highlightStroke) {

			this.highlightStroke = highlightStroke;
		}
	}

}
