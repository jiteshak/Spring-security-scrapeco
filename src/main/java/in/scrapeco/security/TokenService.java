package in.scrapeco.security;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import in.scrapeco.entity.TokenLog;
import in.scrapeco.entity.User;
import in.scrapeco.exception.ApiException;
import in.scrapeco.exception.UnauthorizedException;
import in.scrapeco.repository.TokenLogRepository;
import in.scrapeco.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenService {

	@Value("${security.jwt.authUrl}")
	private String authUrl;

	@Value("${security.jwt.header}")
	private String header;

	@Value("${security.jwt.prefix}")
	private String prefix;

	@Value("${security.jwt.secret}")
	private String secret;

	@Value("${security.jwt.expiration}")
	private Long expiration;

	@Autowired
	private TokenLogService tokenLogService;

	@Autowired
	private TokenLogRepository tokenLogRepository;

	@Autowired
	private UserRepository userRepository;

	public TokenService() {
	}

	public String generateJwt(Token token, String ip) {
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
		final OffsetDateTime expirationDate2 = OffsetDateTime.now().plusSeconds(expiration);

		int tokenLogId = tokenLogService.createLoginLog(token.getUserId(), token.getEmail(), token.getUserType(),
				expirationDate2, ip);

		String jwt = Jwts.builder().setSubject(token.getSub() + "").setIssuedAt(createdDate)
				.setExpiration(expirationDate).claim("userId", token.getUserId()).claim("userName", token.getUserName())
				.claim("userType", token.getUserType().toString()).claim("email", token.getEmail())
				.claim("tokenLogId", tokenLogId).signWith(SignatureAlgorithm.HS512, secret).compact();

		TokenLog tokenLog = tokenLogService.getTokenLogById(tokenLogId);
		tokenLog.setToken(jwt);
		tokenLogRepository.save(tokenLog);

		return jwt;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public String getHeader() {
		return header;
	}

	public String getPrefix() {
		return prefix;
	}

	public Token verifyJwt(String token) {

		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

		Token tokenBody = new Token();
		tokenBody.setSub(claims.getSubject());
		tokenBody.setUserId((String) claims.get("userId"));
		tokenBody.setUserName((String) claims.get("userName"));
		tokenBody.setEmail((String) claims.get("email"));
		tokenBody.setUserType(claims.get("userType").toString());

		tokenBody.setTokenLogId((int) claims.get("tokenLogId"));

		TokenLog ol = tokenLogService.getTokenLogById(tokenBody.getTokenLogId());
		if (ol.getIsValid() != 1) {
			throw new UnauthorizedException("unauthorized");
		}

		return tokenBody;
	}

	public Token verifyJwt(HttpServletRequest request) {
		String header = request.getHeader((getHeader()));
		if (header == null || !header.startsWith(getPrefix())) {
			throw new ApiException("");
		}

		String token = header.replace(getPrefix(), "");
		return verifyJwt(token);
	}

	public void verifyUserType(Token token) {
		Optional<User> userO = userRepository.findByUserId(token.getUserId());
		if (!userO.isPresent()) {
			throw new ServiceException("Incorrect User Id or Password");
		}
		User user = userO.get();

		if (!token.getUserType().equals(user.getUserTypeEntity().getUserType().toString())) {
			throw new ServiceException("User is not valid");
		}
	}

	public String getToken(HttpServletRequest request) {
		String header = request.getHeader((getHeader()));
		if (header == null || !header.startsWith(getPrefix())) {
			throw new ApiException("Invalid header");
		}

		String token = header.replace(getPrefix(), "");
		return token;
	}
}
