package net.pladema.medicalconsultation.virtualConsultation.infrastructure.mapper;

import java.util.List;

import ar.lamansys.sgh.shared.HospitalSharedAutoConfiguration;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationFilterBo;

import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationFilterDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationNotificationDataBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationPriority;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationAvailableProfessionalAmountDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationNotificationDataDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationPatientDataDto;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationResponsibleProfessionalAvailabilityDto;

@Mapper(uses = {EVirtualConsultationPriority.class, EVirtualConsultationStatus.class, VirtualConsultationPatientDataDto.class, LocalDateMapper.class})
public interface VirtualConsultationMapper {

	@Named("fromVirtualConsultationBo")
	@Mapping(target = "patientData.id", source = "patientId")
	@Mapping(target = "patientData.name", source = "patientName")
	@Mapping(target = "patientData.lastName", source = "patientLastName")
	@Mapping(target = "patientData.age", source = "patientAge")
	@Mapping(target = "patientData.gender", source = "patientGender")
	@Mapping(target = "responsibleData.firstName", source = "responsibleFirstName")
	@Mapping(target = "responsibleData.lastName", source = "responsibleLastName")
	@Mapping(target = "responsibleData.healthcareProfessionalId", source = "responsibleHealthcareProfessionalId")
	@Mapping(target = "responsibleData.available", source = "responsibleAvailability")
	@Mapping(target = "institutionData.id", source = "institutionId")
	@Mapping(target = "institutionData.name", source = "institutionName")
	@Mapping(target = "status", source = "statusId")
	@Mapping(target = "priority", source = "priorityId")
	@Mapping(target = "callLink", source = "callId", qualifiedByName = "generateCallLink")
	VirtualConsultationDto fromVirtualConsultationBo(VirtualConsultationBo virtualConsultationBo);

	@Named("generateCallLink")
	default String generateCallLink(String callId) {
		if (callId != null)
			return HospitalSharedAutoConfiguration.JITSI_DOMAIN_URL + "/" + callId;
		else
			return null;
	}

	@Named("fromVirtualConsultationBoList")
	@IterableMapping(qualifiedByName = "fromVirtualConsultationBo")
	List<VirtualConsultationDto> fromVirtualConsultationBoList(List<VirtualConsultationBo> virtualConsultationBo);

	@Named("fromVirtualConsultationNotificationDataBo")
	@Mapping(target = "priority", source = "priorityId")
	@Mapping(target = "callLink", source = "callId", qualifiedByName = "generateCallLink")
	@Mapping(target = "patientData.id", source = "patientId")
	@Mapping(target = "patientData.firstName", source = "patientName")
	@Mapping(target = "patientData.lastName", source = "patientLastName")
	VirtualConsultationNotificationDataDto fromVirtualConsultationNotificationDataBo(VirtualConsultationNotificationDataBo virtualConsultationNotificationDataBo);

	@Named("fromVirtualConsultationResponsibleProfessionalAvailabilityBo")
	VirtualConsultationResponsibleProfessionalAvailabilityDto fromVirtualConsultationResponsibleProfessionalAvailabilityBo(VirtualConsultationResponsibleProfessionalAvailabilityBo professionalAvailability);

	@Named("fromVirtualConsultationAvailableProfessionalAmountBo")
	VirtualConsultationAvailableProfessionalAmountDto fromVirtualConsultationAvailableProfessionalAmountBo(VirtualConsultationAvailableProfessionalAmountBo virtualConsultationAvailableProfessionalAmountBo);

	@Named("fromVirtualConsultationAvailableProfessionalAmountBoList")
	@IterableMapping(qualifiedByName = "fromVirtualConsultationAvailableProfessionalAmountBo")
	List<VirtualConsultationAvailableProfessionalAmountDto> fromVirtualConsultationAvailableProfessionalAmountBoList(List<VirtualConsultationAvailableProfessionalAmountBo> virtualConsultationAvailableProfessionalAmountBo);

	@Named("toVirtualConsultationFilterBo")
	@Mapping(target = "priorityId", source = "priority.id")
	@Mapping(target = "statusId", source = "status.id")
	VirtualConsultationFilterBo toVirtualConsultationFilterBo(VirtualConsultationFilterDto virtualConsultationFilterDto);

}
