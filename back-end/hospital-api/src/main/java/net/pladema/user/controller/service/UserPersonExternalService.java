package net.pladema.user.controller.service;

import net.pladema.user.controller.dto.UserDto;
import net.pladema.user.service.HospitalUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersonExternalService {

    private final HospitalUserService hospitalUserService;

    public UserPersonExternalService(HospitalUserService hospitalUserService) {
        this.hospitalUserService = hospitalUserService;
    }

    public Optional<UserDto> getUser(Integer userId) {
        return Optional.ofNullable(hospitalUserService.getUserPersonInfo(userId))
                .map(user -> new UserDto(user.getEmail(), user.getId(), user.getPersonId(), user.getFirstName(), user.getLastName(), user.getNameSelfDetermination(), user.getPreviousLogin()));
    }
}
