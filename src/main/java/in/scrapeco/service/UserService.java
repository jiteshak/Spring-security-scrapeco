package in.scrapeco.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.scrapeco.email.EmailValidator;
import in.scrapeco.entity.Role;
import in.scrapeco.entity.User;
import in.scrapeco.entity.VerificationToken;
import in.scrapeco.entity.dto.LoginRequest;
import in.scrapeco.exception.APIException;
import in.scrapeco.repository.RoleRepository;
import in.scrapeco.repository.UserRepository;
import in.scrapeco.repository.VerificationTokenRepository;
import in.scrapeco.security.JwtTokenUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	private EmailValidator emailValidator;

	public String login(LoginRequest loginRequest) {

		if (!userRepository.existsByEmail(loginRequest.getEmail())) {
			throw new APIException("Email does not already exists!");
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
		return jwtTokenUtil.generateToken(userDetails.getUsername());
	}

	public User registerUser(User user) {

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new APIException("Email already exists!");
		}

		boolean isValidEmail = emailValidator.isValidEmail(user.getEmail());
		if (!isValidEmail) {
			throw new IllegalStateException(String.format("Email %s is not valid", user.getEmail()));
		}

		Role role = roleRepository.findByName("ROLE_USER");
		user.getRoles().add(role);
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		return userRepository.save(user);
	}

	public void createVerificationToken(User user, String token) {
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(new Date());
		verificationTokenRepository.save(verificationToken);
	}

	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	public void enableUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);
	}
}
