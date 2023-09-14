package ar.lamansys.virtualConsultation.application.getDomainVirtualConsultations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgx.shared.security.UserInfo;

import net.pladema.establishment.repository.CareLineRepository;
import net.pladema.establishment.service.domain.CareLineBo;
import net.pladema.staff.application.ports.ProfessionalProfessionStorage;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import net.pladema.staff.service.domain.ProfessionalProfessionsBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class GetDomainVirtualConsultationsServiceImpl implements GetDomainVirtualConsultationsService {

	private final FeatureFlagsService featureFlagsService;

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private final ProfessionalProfessionStorage professionalProfessionStorage;

	private final CareLineRepository careLineRepository;

	@Override
	public List<VirtualConsultationBo> run(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		List<Integer> healthcareProfessionalSpecialties = getHealthcareProfessionalClinicalSpecialties(doctorId);
		List<Integer> institutionCareLineIds = careLineRepository.getAllByInstitutionId(institutionId).stream().map(CareLineBo::getId).collect(Collectors.toList());
		List<VirtualConsultationBo> result = virtualConsultationRepository.getDomainVirtualConsultations(healthcareProfessionalSpecialties, institutionCareLineIds);
		if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS))
			result.forEach(virtualConsultation -> {
				if (virtualConsultation.getPatientSelfPerceivedName() != null)
					virtualConsultation.setPatientName(virtualConsultation.getPatientSelfPerceivedName());
			});
		log.debug("Output -> {}", result);
		return result;
	}

	private List<Integer> getHealthcareProfessionalClinicalSpecialties(Integer doctorId) {
		List<Integer> result = new ArrayList<>();
		List<ProfessionalProfessionsBo> healthcareProfessionalProfessions = professionalProfessionStorage.getProfessionsByHealthcareProfessionalId(doctorId);
		healthcareProfessionalProfessions.forEach(profession -> {
			List<Integer> clinicalSpecialtyIds = profession.getSpecialties().stream().map(healthcareProfessionalSpecialtyBo -> healthcareProfessionalSpecialtyBo.getClinicalSpecialty().getId()).collect(Collectors.toList());
			result.addAll(clinicalSpecialtyIds);
		});
		return result;
	}

}
