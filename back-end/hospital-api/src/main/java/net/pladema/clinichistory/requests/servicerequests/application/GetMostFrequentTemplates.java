package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;
import net.pladema.snowstorm.services.domain.SnomedTemplateSearchItemBo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetMostFrequentTemplates {

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
	private final ServiceRequestStorage serviceRequestStorage;

	@Value("${app.service-request.concept.most-frequent.max:30}")
	private Integer mostFrequentTemplatesMaxResultQuantity;

	public List<SnomedTemplateSearchItemBo> run(Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);

		Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		List<SnomedTemplateSearchItemBo> result = serviceRequestStorage.getMostFrequentTemplates(institutionId, professionalId, mostFrequentTemplatesMaxResultQuantity);

		log.debug("Output -> {}", result);
		return result;
	}
}
