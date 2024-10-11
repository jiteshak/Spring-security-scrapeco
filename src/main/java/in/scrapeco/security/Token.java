package in.scrapeco.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

	private String sub;
	private String userType;
	private String userId;
	private Integer tokenLogId;
	private String userName;
	private String email;

}
