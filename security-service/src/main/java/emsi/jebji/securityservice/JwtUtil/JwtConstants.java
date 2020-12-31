package emsi.jebji.securityservice.JwtUtil;

public class JwtConstants {
	public static final String SECRET = "mySecret3045";
	public static final String PREFIX = "Bearer ";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final long EXPIRE_ACCESS_TOKEN_TIME = 2*60*1000;
	public static final long EXPIRE_REFRESH_TOKEN_TIME = 20*60*1000;
}
