package net.pladema.user.infrastructure.input.rest;

import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.restorepasswordbyusername.RestorePasswordByUsername;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.user.application.getuserpersondata.GetUserPersonData;
import net.pladema.user.infrastructure.input.rest.dto.PersonDataDto;
import net.pladema.user.infrastructure.input.rest.mapper.UserDataDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/public-user")
@Tag(name = "Public user", description = "Public user")
public class PublicUserController {

    private final Logger logger;

    private final GetUserPersonData getUserPersonData;

    private final UserDataDtoMapper userDataDtoMapper;

	private final RestorePasswordByUsername restorePasswordByUsername;


    public PublicUserController(GetUserPersonData getUserPersonData,
                                UserDataDtoMapper userDataDtoMapper,
								RestorePasswordByUsername restorePasswordByUsername) {
        this.getUserPersonData = getUserPersonData;
        this.userDataDtoMapper = userDataDtoMapper;
		this.restorePasswordByUsername = restorePasswordByUsername;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @GetMapping
    public ResponseEntity<PersonDataDto> getUserPersonData(@RequestParam String token) {
        PersonDataDto result = userDataDtoMapper.PersonDataBoToPersonDataDto(this.getUserPersonData.execute(token));
        logger.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

	@PostMapping("/restore-password")
	public ResponseEntity<String> restorePassword(@RequestParam(name="username") String username){
		log.debug("Restore password by mail for user {}", username);
		String result = restorePasswordByUsername.execute(username);
		log.debug("Password reset link sended to username {} ", username);
		return ResponseEntity.ok().body(result);
	}

}
