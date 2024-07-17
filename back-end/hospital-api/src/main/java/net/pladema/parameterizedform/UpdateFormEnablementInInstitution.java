package net.pladema.parameterizedform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateFormEnablementInInstitution {

	private final ParameterizedFormStorage parameterizedFormStorage;

	public void run(Integer parameterizedFormId, Integer institutionId, Boolean enablement) {
		log.debug("Input parameters -> parameterizedFormId {}, institutionId {}, enablement {}", parameterizedFormId, institutionId, enablement);
		parameterizedFormStorage.updateFormEnablementInInstitution(parameterizedFormId, institutionId, enablement);
	}
}
