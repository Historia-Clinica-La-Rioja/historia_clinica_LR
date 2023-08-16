package ar.lamansys.virtualConsultation.infrastructure.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationNotificationDataBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationPriority;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationAvailableProfessionalAmountDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationNotificationDataDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationPatientDataDto;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationResponsibleProfessionalAvailabilityDto;

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
	VirtualConsultationDto fromVirtualConsultationBo(VirtualConsultationBo virtualConsultationBo);

	@Named("fromVirtualConsultationBoList")
	@IterableMapping(qualifiedByName = "fromVirtualConsultationBo")
	List<VirtualConsultationDto> fromVirtualConsultationBoList(List<VirtualConsultationBo> virtualConsultationBo);

	@Named("fromVirtualConsultationNotificationDataBo")
	@Mapping(target = "priority", source = "priorityId")
	VirtualConsultationNotificationDataDto fromVirtualConsultationNotificationDataBo(VirtualConsultationNotificationDataBo virtualConsultationNotificationDataBo);

	@Named("fromVirtualConsultationResponsibleProfessionalAvailabilityBo")
	VirtualConsultationResponsibleProfessionalAvailabilityDto fromVirtualConsultationResponsibleProfessionalAvailabilityBo(VirtualConsultationResponsibleProfessionalAvailabilityBo professionalAvailability);

	@Named("fromVirtualConsultationAvailableProfessionalAmountBo")
	VirtualConsultationAvailableProfessionalAmountDto fromVirtualConsultationAvailableProfessionalAmountBo(VirtualConsultationAvailableProfessionalAmountBo virtualConsultationAvailableProfessionalAmountBo);

	@Named("fromVirtualConsultationAvailableProfessionalAmountBoList")
	@IterableMapping(qualifiedByName = "fromVirtualConsultationAvailableProfessionalAmountBo")
	List<VirtualConsultationAvailableProfessionalAmountDto> fromVirtualConsultationAvailableProfessionalAmountBoList(List<VirtualConsultationAvailableProfessionalAmountBo> virtualConsultationAvailableProfessionalAmountBo);

}
