package emsi.jebji.securityservice.web;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import emsi.jebji.securityservice.JwtUtil.JwtConstants;
import emsi.jebji.securityservice.entities.AppRole;
import emsi.jebji.securityservice.entities.AppUser;
import emsi.jebji.securityservice.service.AccountService;
import lombok.Data;

@RestController
public class AccountRestController {

	private AccountService accountService;

	public AccountRestController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping(path = "/users")
	@PostAuthorize("hasAuthority('USER')")
	List<AppUser> listUsers() {
		return accountService.listUsers();
	}

	@PostMapping(path = "/users")
	@PostAuthorize("hasAuthority('ADMIN')")
	AppUser addUser(@RequestBody AppUser user) {
		return accountService.addNewUser(user);
	}

	@PostMapping(path = "/roles")
	@PostAuthorize("hasAuthority('ADMIN')")
	AppRole addRole(@RequestBody AppRole role) {
		return accountService.addNewRole(role);
	}

	@PostMapping(path = "/addRoleToUser")
	@PostAuthorize("hasAuthority('ADMIN')")
	void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
		accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
	}

	@GetMapping(path="/refreshToken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String authorizationToken = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
		if(authorizationToken!=null && authorizationToken.startsWith(JwtConstants.PREFIX)) {
			try {
				String jwtRefreshToken = authorizationToken.substring(JwtConstants.PREFIX.length());
				Algorithm algorithm = Algorithm.HMAC256(JwtConstants.SECRET);
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(jwtRefreshToken);
				String username = decodedJWT.getSubject();
				AppUser user = accountService.LoadUserByUserame(username);
				String jwtAccessToken = JWT.create()
						.withSubject(user.getUsername())
		                .withExpiresAt(new Date(System.currentTimeMillis()+JwtConstants.EXPIRE_ACCESS_TOKEN_TIME))
		                .withIssuer(request.getRequestURL().toString())
		                .withClaim("roles",user.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
		                .sign(algorithm);

				Map<String,String> idToken = new HashMap<>();
				idToken.put("access-token", jwtAccessToken);
				idToken.put("refresh-token", jwtRefreshToken);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
 			} catch (Exception e) {
 				//response.setHeader("error-message", e.getMessage());
 				//response.sendError(HttpServletResponse.SC_FORBIDDEN);
 				throw e;
 			}
		}
		else {
			throw new RuntimeException("Refresh Token Required");
		}
	}
	
	@GetMapping("/profile")
	public AppUser profile(Principal principal) {
		return accountService.LoadUserByUserame(principal.getName());
	}
}

@Data
class RoleUserForm {
	String username;
	String roleName;
}
