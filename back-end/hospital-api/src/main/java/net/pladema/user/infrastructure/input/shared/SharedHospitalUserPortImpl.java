package net.pladema.user.infrastructure.input.shared;

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

	@Override
	public HospitalUserPersonInfoDto getUserCompleteInfo(Integer userId) {
		log.debug("Input parameter -> userId {}", userId);
		HospitalUserPersonInfoDto result = toHospitalUserPersonInfoDto(getUserPersonInfo.run(userId));
		log.debug("Output -> {}", result);
		return result;
	}

	private HospitalUserPersonInfoDto toHospitalUserPersonInfoDto(UserPersonInfoBo bo) {
		return new HospitalUserPersonInfoDto(bo.getId(), bo.getEmail(), bo.getPersonId(), bo.getFirstName(), bo.getLastName());
	}
}
