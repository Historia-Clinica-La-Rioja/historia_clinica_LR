package net.pladema.parameterizedform.application;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;
import net.pladema.parameterizedform.domain.enums.EFormScope;
import net.pladema.parameterizedform.domain.ParameterizedFormBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetActiveFormsInInstitution {

	private final ParameterizedFormStorage parameterizedFormStorage;

	public List<ParameterizedFormBo> run(Integer institutionId, EFormScope formScope){
		log.debug("Input parameters -> institutionId {}, eFormScope {}", institutionId, formScope);
		List<ParameterizedFormBo> result = parameterizedFormStorage.getActiveFormsByInstitutionAndScope(institutionId, formScope);
		log.debug("Output -> result {}", result);
		return result;
	}

}
