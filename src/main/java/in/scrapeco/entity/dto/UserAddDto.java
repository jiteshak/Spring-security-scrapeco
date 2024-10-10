package in.scrapeco.entity.dto;

import java.util.HashSet;
import java.util.Set;

import in.scrapeco.entity.Role;
import in.scrapeco.entity.UserTypeEntity;
import in.scrapeco.entity.enums.CompanyType;
import in.scrapeco.entity.enums.IndustryType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddDto {

	private String email;

	private String password;

	private String phone;

	private String companyName;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	private IndustryType industryType;

	@Enumerated(EnumType.STRING)
	private CompanyType companyType;

	private Set<Role> roles = new HashSet<>();

	private UserTypeEntity userTypeEntity;
}
