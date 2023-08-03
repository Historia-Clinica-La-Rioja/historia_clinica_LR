package ar.lamansys.virtualConsultation.infrastructure.input.rest;

import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import ar.lamansys.sgx.shared.security.UserInfo;
import ar.lamansys.virtualConsultation.application.getDomainVirtualConsultations.GetDomainVirtualConsultationsService;
import ar.lamansys.virtualConsultation.application.saveVirtualConsultation.SaveVirtualConsultationRequestService;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationRequestBo;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationRequestDto;
import ar.lamansys.virtualConsultation.infrastructure.mapper.VirtualConsultationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/virtual-consultation")
@RequiredArgsConstructor
public class VirtualConsultationController {

	private final MqttCallExternalService mqttCallExternalService;

	private final SaveVirtualConsultationRequestService saveVirtualConsultationService;

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
	
	private final GetDomainVirtualConsultationsService getDomainVirtualConsultationsService;

	private final VirtualConsultationMapper virtualConsultationMapper;

	@PostMapping(value = "/{institutionId}")
	public Integer saveVirtualConsultationRequest(@PathVariable(name = "institutionId") Integer institutionId,
												  @RequestBody VirtualConsultationRequestDto virtualConsultation) {
		log.debug("Input parameters -> institutionId {}, virtualConsultation {}", institutionId, virtualConsultation);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		VirtualConsultationRequestBo virtualConsultationBo = new VirtualConsultationRequestBo(virtualConsultation);
		virtualConsultationBo.setResponsibleHealthcareProfessionalId(doctorId);
		virtualConsultationBo.setStatusId(EVirtualConsultationStatus.PENDING.getId());
		virtualConsultationBo.setInstitutionId(institutionId);
		Integer result = saveVirtualConsultationService.run(virtualConsultationBo);
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping(value = "/domain")
	public List<VirtualConsultationDto> getDomainVirtualConsultation() {
		List<VirtualConsultationDto> result = virtualConsultationMapper.fromVirtualConsultationBoList(getDomainVirtualConsultationsService.run());
		log.debug("Output -> {}", result);
		return result;
	}

}
