package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.user.dto.UserSharedInfoDto;

public interface SharedHospitalUserPort {

	HospitalUserPersonInfoDto getUserCompleteInfo(Integer userId);

	UserSharedInfoDto fetchUserInfoFromToken(String token);
}
