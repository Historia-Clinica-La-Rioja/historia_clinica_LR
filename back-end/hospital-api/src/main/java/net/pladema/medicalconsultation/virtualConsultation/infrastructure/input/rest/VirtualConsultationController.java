package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest;

import java.util.List;

import javax.validation.Valid;

import net.pladema.medicalconsultation.virtualConsultation.application.notifyVirtualConsultationAcceptedCall.NotifyVirtualConsultationAcceptedCallService;
import net.pladema.medicalconsultation.virtualConsultation.application.notifyVirtualConsultationCancelledCall.NotifyVirtualConsultationCancelledCallService;
import net.pladema.medicalconsultation.virtualConsultation.application.notifyVirtualConsultationIncomingCall.NotifyVirtualConsultationIncomingCallService;

import net.pladema.medicalconsultation.virtualConsultation.application.notifyVirtualConsultationRejectedCall.NotifyVirtualConsultationRejectedCallService;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationFilterBo;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationFilterDto;

import net.pladema.medicalconsultation.virtualConsultation.infrastructure.mapper.VirtualConsultationMapper;
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
import net.pladema.medicalconsultation.virtualConsultation.application.changeClinicalProfessionalAvailability.ChangeClinicalProfessionalAvailabilityService;
import net.pladema.medicalconsultation.virtualConsultation.application.changeResponsibleProfessionalAvailability.ChangeResponsibleProfessionalAvailabilityService;
import net.pladema.medicalconsultation.virtualConsultation.application.changeVirtualConsultationStatus.ChangeVirtualConsultationStatusService;
import net.pladema.medicalconsultation.virtualConsultation.application.getAvailableProfessionalAmountByProfessionalId.GetAvailableProfessionalAmountByProfessionalIdService;
import net.pladema.medicalconsultation.virtualConsultation.application.getDomainVirtualConsultations.GetDomainVirtualConsultationsService;
import net.pladema.medicalconsultation.virtualConsultation.application.getProfessionalAvailabilityService.GetProfessionalAvailabilityService;
import net.pladema.medicalconsultation.virtualConsultation.application.getResponsibleProfesionalAvailability.GetResponsibleProfessionalAvailabilityService;
import net.pladema.medicalconsultation.virtualConsultation.application.getResponsibleUserIdByVirtualConsultationId.GetResponsibleUserIdByVirtualConsultationIdService;
import net.pladema.medicalconsultation.virtualConsultation.application.getVirtualConsultationById.GetVirtualConsultationByIdService;
import net.pladema.medicalconsultation.virtualConsultation.application.getVirtualConsultationNotificationData.GetVirtualConsultationNotificationDataService;
import net.pladema.medicalconsultation.virtualConsultation.application.getVirtualConsultationsByInstitution.GetVirtualConsultationsByInstitutionService;
import net.pladema.medicalconsultation.virtualConsultation.application.saveVirtualConsultation.SaveVirtualConsultationRequestService;
import net.pladema.medicalconsultation.virtualConsultation.domain.ClinicalProfessionalAvailabilityBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationRequestBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationAvailableProfessionalAmountDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationNotificationDataDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationRequestDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationResponsibleProfessionalAvailabilityDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationStatusDataDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationStatusDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.mqtt.VirtualConsultationPublisher;
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

	private final GetProfessionalAvailabilityService getProfessionalAvailabilityService;

	private final GetResponsibleProfessionalAvailabilityService getResponsibleProfessionalAvailabilityService;
	
	private final GetAvailableProfessionalAmountByProfessionalIdService getAvailableProfessionalAmountByProfessionalIdService;

	private final GetVirtualConsultationsByInstitutionService getVirtualConsultationsByInstitutionService;

	private final NotifyVirtualConsultationIncomingCallService notifyVirtualConsultationIncomingCallService;

	private final NotifyVirtualConsultationCancelledCallService  notifyVirtualConsultationCancelledCallService;

	private final NotifyVirtualConsultationRejectedCallService notifyVirtualConsultationRejectedCallService;

	private final NotifyVirtualConsultationAcceptedCallService notifyVirtualConsultationAcceptedCallService;

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

	@GetMapping(value = "/institution/{institutionId}/domain")
	public List<VirtualConsultationDto> getDomainVirtualConsultation(@PathVariable(name = "institutionId") Integer institutionId, @RequestBody @Valid VirtualConsultationFilterDto filter) {
		log.debug("Input parameters -> institutionId {}, filter {}", institutionId, filter);
		VirtualConsultationFilterBo filterBo = virtualConsultationMapper.toVirtualConsultationFilterBo(filter);
		List<VirtualConsultationDto> result = virtualConsultationMapper.fromVirtualConsultationBoList(getDomainVirtualConsultationsService.run(institutionId, filterBo));
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping(value = "/institution/{institutionId}")
	public List<VirtualConsultationDto> getVirtualConsultationsByInstitution(@PathVariable(name = "institutionId") Integer institutionId,
																			 @RequestBody @Valid VirtualConsultationFilterDto filter) {
		log.debug("Input parameters -> institutionId {}, filter {}", institutionId, filter);
		VirtualConsultationFilterBo filterBo = virtualConsultationMapper.toVirtualConsultationFilterBo(filter);
		filterBo.setInstitutionId(institutionId);
		List<VirtualConsultationDto> result = virtualConsultationMapper.fromVirtualConsultationBoList(getVirtualConsultationsByInstitutionService.run(filterBo));
		log.debug("Output -> {}", result);
		return result;
	}

	@PostMapping(value = "/notify/{virtualConsultationId}")
	public void notifyVirtualConsultationCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		Integer responsibleUserId = getResponsibleUserIdByVirtualConsultationIdService.run(virtualConsultationId);
		virtualConsultationPublisher.publish("NOTIFY", virtualConsultationId + "-" + responsibleUserId);
	}

	@PostMapping(value = "/notify-incoming-call/{virtualConsultationId}")
	public void notifyVirtualConsultationIncomingCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		notifyVirtualConsultationIncomingCallService.run(virtualConsultationId, doctorId);
	}

	@PostMapping(value = "/notify-cancelled-call/{virtualConsultationId}")
	public void notifyVirtualConsultationCancelledCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		notifyVirtualConsultationCancelledCallService.run(virtualConsultationId);
	}

	@PostMapping(value = "/notify-rejected-call/{virtualConsultationId}")
	public void notifyVirtualConsultationRejectedCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		notifyVirtualConsultationRejectedCallService.run(virtualConsultationId);
	}

	@PostMapping(value = "/notify-accepted-call/{virtualConsultationId}")
	public void notifyVirtualConsultationAcceptedCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		notifyVirtualConsultationAcceptedCallService.run(virtualConsultationId);
	}

	@GetMapping("/notification/{virtualConsultationId}")
	public VirtualConsultationNotificationDataDto getVirtualConsultationCall(@PathVariable(name = "virtualConsultationId") Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		VirtualConsultationNotificationDataDto result = virtualConsultationMapper.fromVirtualConsultationNotificationDataBo(getVirtualConsultationNotificationDataService.run(virtualConsultationId));
		result.setVirtualConsultationId(virtualConsultationId);
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
	public Boolean changeClinicalProfessionalAvailability(@RequestBody Boolean availability) throws JsonProcessingException {
		log.debug("Input parameters -> availability {}", availability);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		ClinicalProfessionalAvailabilityBo professionalAvailability = new ClinicalProfessionalAvailabilityBo(doctorId, availability);
		Boolean result = changeClinicalProfessionalAvailabilityService.run(professionalAvailability);
		List<VirtualConsultationAvailableProfessionalAmountDto> message = virtualConsultationMapper.fromVirtualConsultationAvailableProfessionalAmountBoList(getAvailableProfessionalAmountByProfessionalIdService.run(doctorId));
		virtualConsultationPublisher.publish("CHANGE-PROFESSIONAL-STATE", objectMapper.writeValueAsString(message));
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

	@GetMapping(value = "/professional-availability")
	public Boolean getProfessionalAvailability() {
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		Boolean result = getProfessionalAvailabilityService.run(doctorId);
		log.debug("Output -> {}", result);
		return result;
	}
	
	@GetMapping(value = "/institution/{institutionId}/responsible-professional-availability")
	public Boolean getResponsibleStatus(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		Boolean result = getResponsibleProfessionalAvailabilityService.run(doctorId, institutionId);
		log.debug("Output -> {}", result);
		return result;
	}

}
