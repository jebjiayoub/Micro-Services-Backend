package emsi.jebji.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = passwordEncoder();
		auth.inMemoryAuthentication().withUser("user").password(passwordEncoder.encode("134789aaa")).roles("USER");
		auth.inMemoryAuthentication().withUser("ayoub").password(passwordEncoder.encode("134789aaa")).roles("USER","ADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.formLogin().loginPage("/login");
		http.formLogin();
		http.authorizeRequests().antMatchers("/save**/**","/delete**/**","/form**/**","/add**/**").hasRole("ADMIN");
		//http.authorizeRequests().antMatchers("/cinemas**/**").hasRole("USER");
		//http.authorizeRequests().anyRequest().authenticated();
		http.csrf().disable();
		http.exceptionHandling().accessDeniedPage("/notAuthorized");
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
