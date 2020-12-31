package emsi.jebji.securityservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import emsi.jebji.securityservice.dao.AppRoleRepository;
import emsi.jebji.securityservice.dao.AppUserRepository;
import emsi.jebji.securityservice.entities.AppRole;
import emsi.jebji.securityservice.entities.AppUser;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	
	private PasswordEncoder passwordEncoder;

	public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
		super();
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AppUser addNewUser(AppUser user) {
		String pass = user.getPassword();
		user.setPassword(passwordEncoder.encode(pass));
		return appUserRepository.save(user);
	}

	@Override
	public AppRole addNewRole(AppRole role) {
		return appRoleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String userName, String roleName) {
		AppUser user = appUserRepository.findByUsername(userName);
        AppRole role = appRoleRepository.findByRoleName(roleName);
        user.getAppRoles().add(role);
	}

	@Override
	public AppUser LoadUserByUserame(String userName) {
		return appUserRepository.findByUsername(userName);
	}

	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}

}
