package emsi.jebji.securityservice.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import emsi.jebji.securityservice.JwtUtil.JwtConstants;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
 
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-with, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
		response.addHeader("Access-Control-Expose-Headers", "Origin, Accept, X-Requested-with, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
		
		
		
		if(request.getServletPath().equals("/refreshToken")) {
			filterChain.doFilter(request, response);
		}
		else {
			String authorizationToken = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
			if(authorizationToken!=null && authorizationToken.startsWith(JwtConstants.PREFIX)) {
				try {
					String jwt = authorizationToken.substring(JwtConstants.PREFIX.length());
					Algorithm algorithm = Algorithm.HMAC256(JwtConstants.SECRET);
					JWTVerifier jwtVerifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
					String username = decodedJWT.getSubject();
					System.out.println("RequestUsername : " + username);
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					for(String role : roles) {
						authorities.add(new SimpleGrantedAuthority(role));
					}
					UsernamePasswordAuthenticationToken authenticationToken =
				                new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
	 			} catch (Exception e) {
	 				response.setHeader("error-message", e.getMessage());
	 				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
		
	}

}
