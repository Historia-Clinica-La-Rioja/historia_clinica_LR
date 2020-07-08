package net.pladema.permissions.controller.mappers;

import net.pladema.permissions.service.domain.UserBo;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.user.controller.dto.UserDto;
import net.pladema.user.controller.dto.UserPersonDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserInfoMapper {

    UserPersonDto toUserPersonDto(BasicDataPersonDto basicDataPersonDto);

    UserDto toUserDto(UserPersonDto personDto, UserBo userBo);
}
