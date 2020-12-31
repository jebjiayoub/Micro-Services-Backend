package emsi.jebji.securityservice;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import emsi.jebji.securityservice.entities.AppRole;
import emsi.jebji.securityservice.entities.AppUser;
import emsi.jebji.securityservice.service.AccountService;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
        	accountService.addNewRole(new AppRole(null,"USER"));
            accountService.addNewRole(new AppRole(null,"ADMIN"));
            accountService.addNewRole(new AppRole(null,"PRODUCTS_MANAGER"));
            accountService.addNewRole(new AppRole(null,"CUSTOMERS_MANAGER"));
            accountService.addNewRole(new AppRole(null,"BILLS_MANAGER"));

            accountService.addNewUser(new AppUser(null,"user1","134789",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user2","134789",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user3","134789",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user4","134789",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"admin","134789",new ArrayList<>()));

            accountService.addRoleToUser("user1","USER");

            accountService.addRoleToUser("user2","USER");
            accountService.addRoleToUser("user2","PRODUCTS_MANAGER");
                        
            accountService.addRoleToUser("user3","USER");
            accountService.addRoleToUser("user3","CUSTOMERS_MANAGER");        
            
            accountService.addRoleToUser("user4","USER");
            accountService.addRoleToUser("user4","BILLS_MANAGER");

            accountService.addRoleToUser("admin","USER");
            accountService.addRoleToUser("admin","ADMIN");
            
            AppUser user=accountService.LoadUserByUserame("admin");
            user.getAppRoles().forEach(r ->{
                System.out.println(r.getRoleName());
            } );

        };
	}
}
