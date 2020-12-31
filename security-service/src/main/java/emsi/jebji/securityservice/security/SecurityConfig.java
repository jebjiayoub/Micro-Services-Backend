package emsi.jebji.securityservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import emsi.jebji.securityservice.filters.JwtAuthentificationFilter;
import emsi.jebji.securityservice.filters.JwtAuthorizationFilter;
import emsi.jebji.securityservice.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
	
	private UserDetailsServiceImpl userDetailsDervice;
	
	public SecurityConfig(UserDetailsServiceImpl userDetailsDervice) {
		this.userDetailsDervice = userDetailsDervice;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsDervice);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
				
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.headers().frameOptions().disable();
		//http.formLogin().loginPage("/login");
		//http.formLogin();
		//http.authorizeRequests().anyRequest().permitAll();
		http.authorizeRequests().antMatchers("/h2-console/**", "/refreshToken/**", "/login/**").permitAll();
		//http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/**").hasAuthority("ADMIN");
		//http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/**").hasAuthority("USER");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JwtAuthentificationFilter(authenticationManagerBean()));
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
