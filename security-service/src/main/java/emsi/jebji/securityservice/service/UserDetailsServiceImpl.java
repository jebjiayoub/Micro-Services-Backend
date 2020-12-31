package emsi.jebji.securityservice.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import emsi.jebji.securityservice.entities.AppUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private AccountService accountService;
	
	public UserDetailsServiceImpl(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = accountService.LoadUserByUserame(username);
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		try {
			appUser.getAppRoles().forEach(role->{
				authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
			});
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		return new User(appUser.getUsername(), appUser.getPassword(), authorities);
	}

}
