package ar.lamansys.virtualConsultation.infrastructure.input.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgx.shared.security.UserInfo;
import ar.lamansys.virtualConsultation.application.changeClinicalProfessionalAvailability.ChangeClinicalProfessionalAvailabilityService;
import ar.lamansys.virtualConsultation.application.changeResponsibleProfessionalAvailability.ChangeResponsibleProfessionalAvailabilityService;
import ar.lamansys.virtualConsultation.application.changeVirtualConsultationStatus.ChangeVirtualConsultationStatusService;
import ar.lamansys.virtualConsultation.application.getDomainVirtualConsultations.GetDomainVirtualConsultationsService;
import ar.lamansys.virtualConsultation.application.getResponsibleUserIdByVirtualConsultationId.GetResponsibleUserIdByVirtualConsultationIdService;
import ar.lamansys.virtualConsultation.application.getVirtualConsultationById.GetVirtualConsultationByIdService;
import ar.lamansys.virtualConsultation.application.getVirtualConsultationNotificationData.GetVirtualConsultationNotificationDataService;
import ar.lamansys.virtualConsultation.application.saveVirtualConsultation.SaveVirtualConsultationRequestService;
import ar.lamansys.virtualConsultation.domain.ClinicalProfessionalAvailabilityBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationRequestBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationNotificationDataDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationRequestDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationResponsibleProfessionalAvailabilityDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationStatusDataDto;
import ar.lamansys.virtualConsultation.infrastructure.mapper.VirtualConsultationMapper;
import ar.lamansys.virtualConsultation.infrastructure.mqtt.VirtualConsultationPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@RestController
@Slf4j
@RequestMapping("/virtual-consultation")
@RequiredArgsConstructor
public class VirtualConsultationController {

	private final VirtualConsultationPublisher virtualConsultationPublisher;

	private final SaveVirtualConsultationRequestService saveVirtualConsultationService;

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
	
	private final GetDomainVirtualConsultationsService getDomainVirtualConsultationsService;

	private final VirtualConsultationMapper virtualConsultationMapper;

	private final GetVirtualConsultationNotificationDataService getVirtualConsultationNotificationDataService;

	private final ChangeClinicalProfessionalAvailabilityService changeClinicalProfessionalAvailabilityService;

	private final ChangeResponsibleProfessionalAvailabilityService changeResponsibleProfessionalAvailabilityService;

	private final GetResponsibleUserIdByVirtualConsultationIdService getResponsibleUserIdByVirtualConsultationIdService;

	private final ChangeVirtualConsultationStatusService changeVirtualConsultationStatusService;

	private final ObjectMapper objectMapper;

	private final GetVirtualConsultationByIdService getVirtualConsultationByIdService;

	private final SnomedService snomedService;

	@PostMapping(value = "/{institutionId}")
	public Integer saveVirtualConsultationRequest(@PathVariable(name = "institutionId") Integer institutionId,
												  @RequestBody @Valid VirtualConsultationRequestDto virtualConsultation) {
		log.debug("Input parameters -> institutionId {}, virtualConsultation {}", institutionId, virtualConsultation);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		VirtualConsultationRequestBo virtualConsultationBo = new VirtualConsultationRequestBo(virtualConsultation);
		virtualConsultationBo.setResponsibleHealthcareProfessionalId(doctorId);
		virtualConsultationBo.setStatusId(EVirtualConsultationStatus.PENDING.getId());
		virtualConsultationBo.setInstitutionId(institutionId);
		virtualConsultationBo.setMotiveSnomedId(snomedService.getSnomedId(new SnomedBo(virtualConsultation.getMotive().getSctid(), virtualConsultation.getMotive().getPt())).get());
		if (virtualConsultation.getProblem() != null)
			virtualConsultationBo.setProblemSnomedId(snomedService.getSnomedId(new SnomedBo(virtualConsultation.getProblem().getSctid(), virtualConsultation.getProblem().getPt())).orElse(null));
		Integer result = saveVirtualConsultationService.run(virtualConsultationBo);
		virtualConsultationPublisher.publish("NEW-VIRTUAL-CONSULTATION", result.toString());
		log.debug("Output -> {}", result);
		return result;
	}


	@GetMapping(value = "/{virtualConsultationId}")
	public VirtualConsultationDto getVirtualConsultation(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		VirtualConsultationDto result = virtualConsultationMapper.fromVirtualConsultationBo(getVirtualConsultationByIdService.run(virtualConsultationId));
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
	public void notifyVirtualConsultationCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		Integer responsibleUserId = getResponsibleUserIdByVirtualConsultationIdService.run(virtualConsultationId);
		virtualConsultationPublisher.publish("NOTIFY", virtualConsultationId + "-" + responsibleUserId);
	}

	@GetMapping("/notification/{virtualConsultationId}")
	public VirtualConsultationNotificationDataDto getVirtualConsultationCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		VirtualConsultationNotificationDataDto result = virtualConsultationMapper.fromVirtualConsultationNotificationDataBo(getVirtualConsultationNotificationDataService.run(virtualConsultationId));
		log.debug("Output -> {}", result);
		return result;
	}

	@PostMapping(value = "/{institutionId}/change-responsible-state")
	public Boolean changeResponsibleAttentionState(@PathVariable(name = "institutionId") Integer institutionId,
												   @RequestBody Boolean attentionValue) throws JsonProcessingException {
		log.debug("Input parameters -> institutionId {}, attentionValue {}", institutionId, attentionValue);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		VirtualConsultationResponsibleProfessionalAvailabilityBo virtualConsultationProfessionalAvailability = new VirtualConsultationResponsibleProfessionalAvailabilityBo(doctorId, institutionId, attentionValue);
		Boolean result = changeResponsibleProfessionalAvailabilityService.run(virtualConsultationProfessionalAvailability);
		VirtualConsultationResponsibleProfessionalAvailabilityDto message = virtualConsultationMapper.fromVirtualConsultationResponsibleProfessionalAvailabilityBo(virtualConsultationProfessionalAvailability);
		virtualConsultationPublisher.publish("CHANGE-RESPONSIBLE-STATE", objectMapper.writeValueAsString(message));
		log.debug("Output -> {}", result);
		return result;
	}

	@PostMapping("/change-clinical-professional-state")
	public Boolean changeClinicalProfessionalAvailability(@RequestBody Boolean availability) {
		log.debug("Input parameters -> availability {}", availability);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		ClinicalProfessionalAvailabilityBo professionalAvailability = new ClinicalProfessionalAvailabilityBo(doctorId, availability);
		Boolean result = changeClinicalProfessionalAvailabilityService.run(professionalAvailability);
		log.debug("Output -> {}", result);
		return result;
	}

	@PutMapping(value = "/{virtualConsultationId}/state")
	public void changeVirtualConsultationState(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId,
											   @RequestBody @Valid VirtualConsultationStatusDto virtualConsultationStatus) throws JsonProcessingException {
		log.debug("Input parameters -> virtualConsultationId {}, virtualConsultationStatus {}", virtualConsultationId, virtualConsultationStatus);
		EVirtualConsultationStatus eVirtualConsultationStatus = virtualConsultationStatus.getStatus();
		changeVirtualConsultationStatusService.run(virtualConsultationId, eVirtualConsultationStatus);
		VirtualConsultationStatusDataDto statusData = new VirtualConsultationStatusDataDto(virtualConsultationId, eVirtualConsultationStatus);
		virtualConsultationPublisher.publish("CHANGE-VIRTUAL-CONSULTATION-STATE", objectMapper.writeValueAsString(statusData));
	}

}
