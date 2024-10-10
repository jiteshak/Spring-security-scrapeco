package in.scrapeco.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.scrapeco.email.EmailService;
import in.scrapeco.entity.User;
import in.scrapeco.entity.VerificationToken;
import in.scrapeco.entity.dto.JwtResponse;
import in.scrapeco.entity.dto.LoginRequest;
import in.scrapeco.service.TokenBlacklistService;
import in.scrapeco.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		User registeredUser = userService.registerUser(user);

		String token = UUID.randomUUID().toString();
		userService.createVerificationToken(registeredUser, token);
		emailService.sendVerificationEmail(registeredUser, token);

		return ResponseEntity.ok("Registration successful. Please check your email for verification.");
	}

	@GetMapping("/confirm")
	public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			return ResponseEntity.badRequest().body("Invalid token");
		}
		User user = verificationToken.getUser();
		userService.enableUser(user);

		return ResponseEntity.ok("Email verified successfully!");
	}

	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
		String token = userService.login(loginRequest);
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setToken(token);
		return ResponseEntity.ok(jwtResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);

			if (tokenBlacklistService.isTokenBlacklisted(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please sign in again");
			}

			tokenBlacklistService.blacklistToken(token);
			return ResponseEntity.ok("Successfully logged out");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please sign in again");
		}
	}

}
