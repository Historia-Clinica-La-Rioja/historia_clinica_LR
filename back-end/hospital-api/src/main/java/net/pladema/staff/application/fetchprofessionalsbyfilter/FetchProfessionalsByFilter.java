package net.pladema.staff.application.fetchprofessionalsbyfilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.RoleUtils;
import net.pladema.staff.application.ports.HealthcareProfessionalStorage;

import net.pladema.staff.domain.HealthcareProfessionalSearchBo;

import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchProfessionalsByFilter {

	private final HealthcareProfessionalStorage healthcareProfessionalStorage;

	public List<HealthcareProfessionalBo> execute(HealthcareProfessionalSearchBo searchCriteria) {
		log.debug("Input parameter getAllProfessionalsByFilter -> searchCriteria {}", searchCriteria);
		List<Short> professionalERolIds = RoleUtils.getProfessionalERoleIds();
		List<HealthcareProfessionalBo> result = healthcareProfessionalStorage.fetchAllProfessionalsByFilter(searchCriteria, professionalERolIds);
		log.debug("Output result -> {} ", result);
		return result;
	}
}
