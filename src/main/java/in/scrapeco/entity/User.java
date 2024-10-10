package in.scrapeco.entity;

import java.util.HashSet;
import java.util.Set;

import in.scrapeco.entity.enums.CompanyType;
import in.scrapeco.entity.enums.IndustryType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_details_tb")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "phone_number")
	private String phone;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "industry_type")
	private IndustryType industryType;

	@Enumerated(EnumType.STRING)
	@Column(name = "company_type")
	private CompanyType companyType;

	@Column(name = "secondary_phone_number")
	private String secondaryPhoneNumber;

	@Column(name = "company_address")
	private String companyAddress;

	@Column(name = "company_description")
	private String companyDescription;

	@Lob
	@Column(name = "company_logo")
	private byte[] companyLogo;

	@Column(name = "department")
	private String department;

	@Column(name = "responsibility")
	private String responsibility;

	@Column(name = "secondary_email")
	private String secondaryEmail;

	@Column(name = "location")
	private String location;

	@Column(name = "payment_flag")
	private Boolean paymentFlag;

	private Boolean enabled = false;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "user_type_id", referencedColumnName = "user_type_id")
	private UserTypeEntity userTypeEntity;
}
