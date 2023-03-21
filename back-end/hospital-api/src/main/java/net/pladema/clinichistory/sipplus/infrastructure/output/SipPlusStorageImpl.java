package net.pladema.clinichistory.sipplus.infrastructure.output;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.sipplus.application.port.SipPlusStorage;

import net.pladema.clinichistory.sipplus.application.port.exceptions.SipPlusException;

import net.pladema.clinichistory.sipplus.application.port.exceptions.SipPlusExceptionEnum;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SipPlusStorageImpl implements SipPlusStorage {

	private final Environment env;

	@Override
	public String getUrlBase() {
		String sipUrlBase = env.getProperty("ws.sip.plus.url.base");
		if (sipUrlBase == null || sipUrlBase.isBlank())
			throw new SipPlusException(SipPlusExceptionEnum.MISSING_PROPERTY, "La url no se encuentra configurada en el archivo de propiedades");
		return sipUrlBase;
	}

}
