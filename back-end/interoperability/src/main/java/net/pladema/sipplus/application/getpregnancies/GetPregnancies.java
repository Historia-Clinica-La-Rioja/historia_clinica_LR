package net.pladema.sipplus.application.getpregnancies;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.sipplus.application.port.SipPlusWSStorage;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetPregnancies {

	private final SipPlusWSStorage sipPlusWSStorage;

	public List<Integer> run(Integer patientId) throws RestTemplateApiException {
		return sipPlusWSStorage.getPregnancies(patientId);
	}
}
