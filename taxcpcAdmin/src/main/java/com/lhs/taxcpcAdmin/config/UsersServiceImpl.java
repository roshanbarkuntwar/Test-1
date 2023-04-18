package com.lhs.taxcpcAdmin.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.UserMastRepository;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UsersServiceImpl implements UserDetailsService {

	@Autowired
	private UserMastRepository userMastRepo;

	@Autowired
	private LhsStringUtility strUtl;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		UserDetails user = null;
		log.info("User is valid1. {}", username);
		if (!strUtl.isNull(username)) {
			UserMast userMast = userMastRepo.findByLoginId(username);
//			log.info("User is valid2. {}", userMast);

			List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();

			grantedAuths.add(new SimpleGrantedAuthority("LoginId :" + userMast.getLoginId()));
			grantedAuths.add(new SimpleGrantedAuthority("UserCode :" + userMast.getUser_code()));
			grantedAuths.add(new SimpleGrantedAuthority("UserRole :" + userMast.getRole_code()));
			grantedAuths.add(new SimpleGrantedAuthority("UserStatus :" + userMast.getUser_status()));
//			grantedAuths.add(new SimpleGrantedAuthority("EntityCode :" + userMast.getEntity_code()));
//			grantedAuths.add(new SimpleGrantedAuthority("ClientCode :" + userMast.getClient_code()));

			user = new User(userMast.getLoginId(), userMast.getLoginPwd(), grantedAuths);
//			log.info("User is valid3. {}", user);

		}

		return user;
	}// end method
}// end class
