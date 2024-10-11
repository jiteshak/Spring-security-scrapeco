package in.scrapeco.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import in.scrapeco.entity.User;
import in.scrapeco.entity.dto.UserAddDto;
import in.scrapeco.exception.ApiException;
import in.scrapeco.repository.UserRepository;
import in.scrapeco.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User addUser(UserAddDto request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ApiException("User already exists");
		}

		// Create a new User entity
		User user = new User();
		String password = SecurityUtil.generatePassword();
		user.setEmail(request.getEmail());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setPhone(request.getMobile());
		user.setCompanyName(request.getCompanyName());
		user.setCompanyAddress(request.getCompanyAddress());
		user.setCompanyDescription(request.getCompanyDescription());
		user.setSecondaryPhoneNumber(request.getSecondaryPhoneNumber());
		user.setSecondaryEmail(request.getSecondaryEmail());
		user.setLocation(request.getLocation());
		user.setPaymentFlag(request.getPaymentFlag());
		user.setIndustryType(request.getIndustryType()); // Make sure to include this in your DTO
		user.setCompanyType(request.getCompanyType()); // Make sure to include this in your DTO
		user.setPassword(password); // Store the plain password here only if you're hashing it later

		// Save the user to the database
		userRepository.save(user);

		return user;
	}
}
