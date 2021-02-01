package net.pladema.user.controller.service;

import net.pladema.permissions.controller.mappers.UserInfoMapper;
import net.pladema.permissions.service.domain.UserBo;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.person.controller.service.PersonExternalServiceImpl;
import net.pladema.user.controller.dto.UserDto;
import net.pladema.user.controller.dto.UserPersonDto;
import net.pladema.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserExternalServiceImpl implements UserExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonExternalServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final UserService userService;

    private final UserInfoMapper userInfoMapper;

    private final PersonExternalService personExternalService;

    public UserExternalServiceImpl(
            UserService userService,
            UserInfoMapper userInfoMapper,
            PersonExternalService personExternalService
    ) {
        this.userService = userService;
        this.userInfoMapper = userInfoMapper;
        this.personExternalService = personExternalService;
    }

    @Override
    public UserDto getUser(Integer userId) {
        LOG.debug("Input parameter -> {}", userId);

        Optional<UserBo> userBoOpt = this.userService.getUser(userId);

        UserBo userBo = userBoOpt.orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        BasicDataPersonDto personBasicData = personExternalService.getBasicDataPerson(userBo.getPersonId());

        UserPersonDto userPersonDto = userInfoMapper.toUserPersonDto(personBasicData);

        UserDto result = userInfoMapper.toUserDto(userPersonDto, userBo);

        LOG.debug("User result {}", result);

        return result;
    }
}
