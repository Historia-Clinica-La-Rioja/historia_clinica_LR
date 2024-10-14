package net.pladema.establishment.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.institutionalgroup.SharedInstitutionalGroupPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.InstitutionalGroupInstitutionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class SharedInstitutionalGroupPortImpl implements SharedInstitutionalGroupPort {

	private final InstitutionalGroupInstitutionRepository institutionalGroupInstitutionRepository;

	@Override
	public List<Integer> getInstitutionIdsByUserId(Integer userId){
		log.debug("Input parameters -> userId {}", userId);
		List<Integer> result = institutionalGroupInstitutionRepository.findInstitutionIdsByUserId(userId);
		log.debug("OUTPUT -> {}", result);
		return result;
	}

}
