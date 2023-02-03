package net.pladema.clinichistory.sipplus.infrastructure.output;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.TokenExternalService;
import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.auth.user.SgxUserDetails;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.sipplus.application.port.SipPlusStorage;

import net.pladema.clinichistory.sipplus.application.port.exceptions.SipPlusException;

import net.pladema.clinichistory.sipplus.application.port.exceptions.SipPlusExceptionEnum;

import net.pladema.clinichistory.sipplus.domain.SipPlusUrlDataBo;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SipPlusStorageImpl implements SipPlusStorage {

	private final Environment env;
	private final TokenExternalService tokenExternalService;

	@Override
	public SipPlusUrlDataBo getUrlData() {
		return SipPlusUrlDataBo.builder()
				.urlBase(getUrlBase())
				.token(getToken())
				.embedSystem(getEmbedSystem())
				.build();
	}

	private String getToken() {
		SgxUserDetails userDetails = SecurityContextUtils.getUserDetails();
		Integer userId = userDetails.userId;
		String username = userDetails.getUsername();
		String token = tokenExternalService.generateToken(userId, username);
		return token;
	}

	private String getUrlBase() {
		String sipUrlBase = env.getProperty("ws.sip.plus.url.base");
		if (sipUrlBase == null || sipUrlBase.isBlank())
			throw new SipPlusException(SipPlusExceptionEnum.MISSING_SIP_URL_PROPERTY, "La url de sip no se encuentra configurada en el archivo de propiedades");
		return sipUrlBase;
	}

	private String getEmbedSystem() {
		String embedSystem = env.getProperty("ws.sip.plus.embed-system");
		if (embedSystem == null || embedSystem.isBlank())
			throw new SipPlusException(SipPlusExceptionEnum.MISSING_EMBED_SYSTEM_NAME_PROPERTY, "Es necesario ingresar el nombre de sistema embedido definido en el archivo de configuracion de sip");
		return embedSystem;
	}

}
