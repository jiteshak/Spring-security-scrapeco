package in.scrapeco.entity.dto;

import in.scrapeco.entity.Role;
import in.scrapeco.entity.UserTypeEntity;
import in.scrapeco.entity.enums.CompanyType;
import in.scrapeco.entity.enums.IndustryType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Phone number is required")
	private String mobile;

	private String companyName;

	@NotBlank(message = "Company address is required")
	private String companyAddress;

	private String companyDescription; // Added field

	private String secondaryPhoneNumber; // Added field

	private String secondaryEmail; // Added field

	private String location; // Added field

	private Boolean paymentFlag; // Added field

	@NotNull(message = "Industry type is required")
	private IndustryType industryType;

	@NotNull(message = "Company type is required")
	private CompanyType companyType;

	private Role role;

	private UserTypeEntity userTypeEntity;

}
