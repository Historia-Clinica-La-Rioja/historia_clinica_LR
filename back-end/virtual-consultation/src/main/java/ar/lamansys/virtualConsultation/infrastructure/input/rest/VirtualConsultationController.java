package ar.lamansys.virtualConsultation.infrastructure.input.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;
import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import ar.lamansys.sgx.shared.security.UserInfo;
import ar.lamansys.virtualConsultation.application.changeResponsibleProfessionalAvailability.ChangeResponsibleProfessionalAvailabilityService;
import ar.lamansys.virtualConsultation.application.getDomainVirtualConsultations.GetDomainVirtualConsultationsService;
import ar.lamansys.virtualConsultation.application.getVirtualConsultationNotificationData.GetVirtualConsultationNotificationDataService;
import ar.lamansys.virtualConsultation.application.getVirtualConsultationResponsibleProfessionalAvailability.GetVirtualConsultationResponsibleProfessionalAvailabilityService;
import ar.lamansys.virtualConsultation.application.saveVirtualConsultation.SaveVirtualConsultationRequestService;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationRequestBo;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationNotificationDataDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationRequestDto;
import ar.lamansys.virtualConsultation.infrastructure.mapper.VirtualConsultationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

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

	private final GetVirtualConsultationNotificationDataService getVirtualConsultationNotificationDataService;

	private final GetVirtualConsultationResponsibleProfessionalAvailabilityService getVirtualConsultationResponsibleProfessionalAvailabilityService;

	private final ChangeResponsibleProfessionalAvailabilityService changeResponsibleProfessionalAvailabilityService;

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

	@PostMapping(value = "/notify/{virtualConsultationId}")
	public void notifyVirtualConsultationCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) throws JsonProcessingException {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		VirtualConsultationNotificationDataDto notification = virtualConsultationMapper.fromVirtualConsultationNotificationDataBo(getVirtualConsultationNotificationDataService.run(virtualConsultationId));
		ObjectMapper jsonMapper = new ObjectMapper();
		mqttCallExternalService.publish(new MqttMetadataDto("HSI/VIRTUAL-CONSULTATION/NOTIFY", jsonMapper.writeValueAsString(notification), false, 2));
	}

	@PostMapping(value = "/{institutionId}/change-responsible-state")
	public Boolean changeResponsibleAttentionState(@PathVariable(name = "institutionId") Integer institutionId, @RequestBody Boolean attentionValue) {
		log.debug("Input parameters -> institutionId {}, attentionValue {}", institutionId, attentionValue);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		VirtualConsultationResponsibleProfessionalAvailabilityBo virtualConsultationProfessionalAvailability = getVirtualConsultationResponsibleProfessionalAvailabilityService.run(doctorId, institutionId);
		if (virtualConsultationProfessionalAvailability == null) {
			virtualConsultationProfessionalAvailability = new VirtualConsultationResponsibleProfessionalAvailabilityBo(doctorId, institutionId);
		}
		virtualConsultationProfessionalAvailability.setAvailable(attentionValue);
		return changeResponsibleProfessionalAvailabilityService.run(virtualConsultationProfessionalAvailability);
	}

}
