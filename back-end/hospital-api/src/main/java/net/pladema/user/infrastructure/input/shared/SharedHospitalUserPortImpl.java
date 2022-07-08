package net.pladema.user.infrastructure.input.shared;

import ar.lamansys.sgh.shared.infrastructure.input.service.user.dto.UserSharedInfoDto;
import net.pladema.user.application.getuserpersondata.GetUserPersonData;

import net.pladema.user.infrastructure.input.rest.dto.PersonDataDto;

import net.pladema.user.infrastructure.input.rest.mapper.UserDataDtoMapper;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.HospitalUserPersonInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.getuserpersoninfo.GetUserPersonInfo;
import net.pladema.user.controller.service.domain.UserPersonInfoBo;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharedHospitalUserPortImpl implements SharedHospitalUserPort {

	private final GetUserPersonInfo getUserPersonInfo;

	private final GetUserPersonData getUserPersonData;

	private final UserDataDtoMapper userDataDtoMapper;

	@Override
	public HospitalUserPersonInfoDto getUserCompleteInfo(Integer userId) {
		log.debug("Input parameter -> userId {}", userId);
		HospitalUserPersonInfoDto result = toHospitalUserPersonInfoDto(getUserPersonInfo.run(userId));
		log.debug("Output -> {}", result);
		return result;
	}

	private HospitalUserPersonInfoDto toHospitalUserPersonInfoDto(UserPersonInfoBo bo) {
		return new HospitalUserPersonInfoDto(bo.getId(), bo.getEmail(), bo.getPersonId(), bo.getFirstName(), bo.getLastName(), bo.getNameSelfDetermination());
	}
	@Override
	public UserSharedInfoDto fetchUserInfoFromToken(String token) {
		PersonDataDto result = userDataDtoMapper.PersonDataBoToPersonDataDto(getUserPersonData.execute(token));
		return new UserSharedInfoDto(result.getUserId());
	}
}
