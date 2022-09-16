package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.user.dto.UserSharedInfoDto;

import java.util.Optional;

public interface SharedHospitalUserPort {

	HospitalUserPersonInfoDto getUserCompleteInfo(Integer userId);

	Optional<UserSharedInfoDto> fetchUserInfoFromNormalToken(String token);
}
