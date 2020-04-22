package net.pladema.user.controller;

import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.user.controller.dto.PasswordResetDto;
import net.pladema.user.controller.dto.UserDto;
import net.pladema.user.controller.mappers.UserMapper;
import net.pladema.user.repository.PasswordResetTokenRepository;
import net.pladema.user.repository.UserRepository;
import net.pladema.user.repository.entity.PasswordResetToken;
import net.pladema.user.service.UserPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("password-reset")
public class PasswordResetController {
	protected final Logger logger;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserPasswordService userPasswordService;
	private final PasswordResetTokenRepository passwordResetTokenRepository;

	public PasswordResetController(UserRepository userRepository, UserMapper userMapper, UserPasswordService userPasswordService, PasswordResetTokenRepository passwordResetTokenRepository) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.userPasswordService = userPasswordService;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
	}


	@PostMapping
	public @ResponseBody
	UserDto setPassword(@Valid @RequestBody PasswordResetDto passwordResetDto) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(passwordResetDto.getToken())
				.orElseThrow(() -> new NotFoundException("bad-token", "Código inválido"));

		UserDto userDto = userRepository.findById(passwordResetToken.getUserId())
				.map(userMapper::fromUser)
				.orElseThrow(() -> new NotFoundException("bad-token-user", "Código de usuario inválido"));

		userPasswordService.setPassword(passwordResetToken.getUserId(), passwordResetDto.getPassword());
		passwordResetTokenRepository.disableTokens(passwordResetToken.getUserId());

		return userDto;
	}
}
