package net.pladema.user.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.user.application.updateEnable.UpdateEnable;
import net.pladema.user.application.createDefaultUser.CreateDefaultUser;
import net.pladema.user.application.getUser.GetUser;
import net.pladema.user.infrastructure.input.rest.dto.UserDataDto;
import net.pladema.user.infrastructure.input.rest.mapper.UserDataDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/institution/{institutionId}")
@Tag(name = "User", description = "User")
@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
public class UserController {

    private final Logger logger;
    private final GetUser getUser;
    private final UserDataDtoMapper userDataDtoMapper;
    private final CreateDefaultUser createDefaultUser;
    private final UpdateEnable updateEnable;


    public UserController(GetUser getUser,
                          UserDataDtoMapper userDataDtoMapper,
                          CreateDefaultUser createDefaultUser,
                          UpdateEnable updateEnable) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.getUser = getUser;
        this.userDataDtoMapper = userDataDtoMapper;
        this.createDefaultUser = createDefaultUser;
        this.updateEnable = updateEnable;
    }

    @GetMapping("/person/{personId}")
    public UserDataDto getUserData(@PathVariable(name = "institutionId") Integer institutionId,
                                   @PathVariable(name = "personId") Integer personId) {
        logger.debug("Input parameters -> {}", personId);
        UserDataDto result = userDataDtoMapper.UserDataBoToUserDataDto(getUser.run(personId));
        logger.debug("Output -> {}", result);
        return result;
    }

    @PostMapping(value = "/person/{personId}")
    public Integer createDefaultUser(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "personId") Integer personId) {
        logger.debug("Input parameters -> {}", personId);
        Integer result = createDefaultUser.run(personId);
        logger.debug("Output -> {}", result);
        return result;
    }

    @PutMapping(value = "/user/{userId}")
    public ResponseEntity<Boolean> updateEnable(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "userId") Integer userId,
            @RequestBody Boolean enable
    ) {
        logger.debug("Input parameters -> userId {}, enable {}", userId, enable);
        Boolean result = updateEnable.run(userId, enable);
        logger.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
