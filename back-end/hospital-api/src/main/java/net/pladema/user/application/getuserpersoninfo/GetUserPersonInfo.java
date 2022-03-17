package net.pladema.user.application.getuserpersoninfo;

import net.pladema.user.application.getuserpersoninfo.exception.GetUserPersonInfoEnumException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.getuserpersoninfo.exception.GetUserPersonInfoException;
import net.pladema.user.application.port.HospitalUserStorage;
import net.pladema.user.controller.service.domain.UserPersonInfoBo;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetUserPersonInfo {

	private final HospitalUserStorage storage;

	public UserPersonInfoBo run(Integer userId) {
		log.debug("input parameter -> userId {}", userId);
		UserPersonInfoBo result = storage.getUserPersonInfo(userId)
				.orElseThrow(() -> new GetUserPersonInfoException(GetUserPersonInfoEnumException.UNEXISTED_USER, String.format("El usuario con id %s no existe", userId)));
		log.debug("Ouput -> {}", result);
		return result;
	}
}
