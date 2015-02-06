package com.tadpole.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tadpole.entity.Role;
import com.tadpole.entity.User;
import com.tadpole.repository.UserRepository;

/**
 * An implementation of the spring security {@link UserDetailsService}.
 *
 */
@Service("securityDetailsService")
public class SecurityDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

    /**
     * {@inheritDoc}
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {

    	User user = userRepository.findByUsernameAndActive(username, true);
    	
    	if (user == null) {
    		
			return null;
			
		} else {
			
			List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
			
			List<Role> userRoles = user.getRoles();
			for (Role role : userRoles) {
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
				roles.add(grantedAuthority);
				
			}
			UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
			
			return userDetails;
		}
   }

}
