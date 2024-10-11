package in.scrapeco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import in.scrapeco.entity.User;
import in.scrapeco.entity.dto.CommonDto;
import in.scrapeco.entity.dto.UserAddDto;
import in.scrapeco.exception.ApiException;
import in.scrapeco.security.Token;
import in.scrapeco.security.TokenLogService;
import in.scrapeco.security.TokenService;
import in.scrapeco.service.Emailservice;
import in.scrapeco.service.UserService;
import in.scrapeco.utils.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public class UserController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private TokenLogService tokenLogService;

	@Autowired
	private UserService userService;

	@Autowired
	private Emailservice emailservice;

	@PostMapping("")
	public CommonDto addUser(HttpServletRequest request, @Valid @RequestBody UserAddDto addUserDto) {
		try {
			Token token = tokenService.verifyJwt(request);
			tokenService.verifyUserType(token);

			User user = userService.addUser(addUserDto);

			String resetToken = tokenLogService.createResetLog(user.getUserId(), user.getEmail(),
					user.getUserTypeEntity().getUserType(), CommonUtil.getRemoteAddress(request));

			emailservice.sendPasswordToUser(user, resetToken);

			return new CommonDto("User Added successfully", user.getFirstName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApiException(e.getMessage());
		}
	}

}
