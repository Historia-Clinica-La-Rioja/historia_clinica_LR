package net.pladema.user.controller;

import io.swagger.annotations.Api;
import net.pladema.user.controller.dto.UserDataDto;
import net.pladema.user.controller.mappers.UserDtoMapper;
import net.pladema.user.controller.service.UpdateEnable.UpdateEnableService;
import net.pladema.user.controller.service.createDefaultUser.CreateDefaultUserService;
import net.pladema.user.controller.service.getUser.GetUserService;
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
@Api(value = "User", tags = {"User"})
@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
public class UserController {

    private final Logger logger;
    private final GetUserService getUserService;
    private final UserDtoMapper userDtoMapper;
    private final CreateDefaultUserService createDefaultUserService;
    private final UpdateEnableService updateEnableService;


    public UserController(GetUserService getUserService,
                          UserDtoMapper userDtoMapper,
                          CreateDefaultUserService createDefaultUserService,
                          UpdateEnableService updateEnableService) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.getUserService = getUserService;
        this.userDtoMapper = userDtoMapper;
        this.createDefaultUserService = createDefaultUserService;
        this.updateEnableService = updateEnableService;
    }

    @GetMapping("/person/{personId}")
    public UserDataDto getUserData(@PathVariable(name = "institutionId") Integer institutionId,
                                   @PathVariable(name = "personId") Integer personId) {
        logger.debug("Input parameters -> {}", personId);
        UserDataDto result = userDtoMapper.UserDataBoToUserDataDto(getUserService.run(personId));
        logger.debug("Output -> {}", result);
        return result;
    }

    @PostMapping(value = "/person/{personId}")
    @Transactional
    public Integer createDefaultUser(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "personId") Integer personId) {
        logger.debug("Input parameters -> {}", personId);
        Integer result = createDefaultUserService.run(personId);
        logger.debug("Output -> {}", result);
        return result;
    }

    @PutMapping(value = "/user/{userId}")
    @Transactional
    public ResponseEntity<Boolean> updateEnable(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "userId") Integer userId,
            @RequestBody Boolean enable
    ) {
        logger.debug("Input parameters -> userId {}, enable {}", userId, enable);
        Boolean result = updateEnableService.run(userId, enable);
        logger.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
