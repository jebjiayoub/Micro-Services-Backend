package emsi.jebji.securityservice.service;


import java.util.List;

import emsi.jebji.securityservice.entities.AppRole;
import emsi.jebji.securityservice.entities.AppUser;

public interface AccountService {
	
	AppUser addNewUser(AppUser user);
	
    AppRole addNewRole(AppRole role);
    
    void addRoleToUser(String userName,String RoleName);
    
    AppUser LoadUserByUserame(String UserName);
    
    List<AppUser> listUsers();
    
}
