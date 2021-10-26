package net.pladema.user.controller;

import io.swagger.annotations.Api;
import net.pladema.user.controller.dto.UserDataDto;
import net.pladema.user.controller.mappers.UserDtoMapper;
import net.pladema.user.controller.service.createDefaultUser.CreateDefaultUserService;
import net.pladema.user.controller.service.getUser.GetUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/institution/{institutionId}")
@Api(value = "User", tags = {"User"})
public class UserController {

    private final Logger logger;
    private final GetUserService getUserService;
    private final UserDtoMapper userDtoMapper;
    private final CreateDefaultUserService createDefaultUserService;


    public UserController(GetUserService getUserService,
                          UserDtoMapper userDtoMapper,
                          CreateDefaultUserService createDefaultUserService) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.getUserService = getUserService;
        this.userDtoMapper = userDtoMapper;
        this.createDefaultUserService = createDefaultUserService;
    }

    @GetMapping("/person/{personId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
    public UserDataDto getUserData(@PathVariable(name = "institutionId") Integer institutionId,
                                   @PathVariable(name = "personId") Integer personId) {
        logger.debug("Input parameters -> {}", personId);
        UserDataDto result = userDtoMapper.UserDataBoToUserDataDto(getUserService.run(personId));
        logger.debug("Output -> {}", result);
        return result;
    }

    @PostMapping(value = "/person/{personId}")
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
    public Integer createDefaultUser(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "personId") Integer personId){
        logger.debug("Input parameters -> {}", personId);
        Integer result = createDefaultUserService.run(personId);
        logger.debug("Output -> {}", result);
        return result;
    }
}
