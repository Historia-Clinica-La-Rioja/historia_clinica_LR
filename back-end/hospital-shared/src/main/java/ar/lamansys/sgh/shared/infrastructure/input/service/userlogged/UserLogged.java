package ar.lamansys.sgh.shared.infrastructure.input.service.userlogged;

import ar.lamansys.sgh.shared.infrastructure.output.repository.SharedHealthcareProfessionalRepository;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserLogged {

	private static final String OUTPUT = "Output -> {}";

	private static final String NO_INPUT_PARAMETERS = "No input parameters";

	private final SharedHealthcareProfessionalRepository sharedHealthcareProfessionalRepository;

	public UserLogged(SharedHealthcareProfessionalRepository sharedHealthcareProfessionalRepository) {
		this.sharedHealthcareProfessionalRepository = sharedHealthcareProfessionalRepository;
	}

	public Integer getProfessionalId() {
		log.debug(NO_INPUT_PARAMETERS);
		Integer professionalId = sharedHealthcareProfessionalRepository.getProfessionalId(UserInfo.getCurrentAuditor());
		log.debug(OUTPUT, professionalId);
		return professionalId;
	}
}
