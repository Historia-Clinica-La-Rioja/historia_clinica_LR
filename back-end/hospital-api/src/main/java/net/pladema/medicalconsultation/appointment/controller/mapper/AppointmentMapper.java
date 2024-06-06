package net.pladema.medicalconsultation.appointment.controller.mapper;

import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgh.shared.HospitalSharedAutoConfiguration;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentEquipmentShortSummaryDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentShortSummaryDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AssignedAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.BookedAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CustomRecurringAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.EmptyAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.EquipmentAppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.PatientAppointmentHistoryDto;
import net.pladema.medicalconsultation.appointment.controller.dto.UpdateAppointmentDateDto;
import net.pladema.medicalconsultation.appointment.domain.UpdateAppointmentDateBo;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.controller.dto.GroupAppointmentResponseDto;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentEquipmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBookingBo;
import net.pladema.medicalconsultation.appointment.service.domain.EmptyAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentSearchDto;

import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentResponseBo;
import net.pladema.medicalconsultation.appointment.service.domain.PatientAppointmentHistoryBo;

import net.pladema.medicalconsultation.diary.controller.dto.BlockDto;
import net.pladema.medicalconsultation.diary.domain.FreeAppointmentSearchFilterBo;
import net.pladema.medicalconsultation.diary.infrastructure.input.dto.FreeAppointmentSearchFilterDto;
import net.pladema.medicalconsultation.diary.service.domain.BlockBo;

import org.mapstruct.IterableMapping;
import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentBasicPatientDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDailyAmountDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.UpdateAppointmentDto;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import java.util.List;

@Mapper(uses = {LocalDateMapper.class, EAppointmentModality.class, EReferenceClosureType.class, SnomedMapper.class, StudyMapper.class})
public interface AppointmentMapper {

    @Named("toAppointmentListDto")
    @Mapping(target = "id", source = "appointmentBo.id")
    @Mapping(target = "date", source = "appointmentBo.date")
    @Mapping(target = "hour", source = "appointmentBo.hour")
    @Mapping(target = "overturn", source = "appointmentBo.overturn")
    @Mapping(target = "appointmentStateId", source = "appointmentBo.appointmentStateId")
    @Mapping(target = "patient", source = "patient")
    @Mapping(target = "phoneNumber", source = "appointmentBo.phoneNumber")
	@Mapping(target = "isProtected", source = "appointmentBo.protected")
	@Mapping(target = "createdOn", source = "appointmentBo.createdOn")
	@Mapping(target = "professionalPersonDto", source = "appointmentBo.professionalPersonBo")
	@Mapping(target = "diaryLabelDto", source = "appointmentBo.diaryLabelBo")
    AppointmentListDto toAppointmentListDto(AppointmentBo appointmentBo, AppointmentBasicPatientDto patient);

	@Named("toEquipmentAppointmentListDto")
	@Mapping(target = "id", source = "equipmentAppointmentBo.id")
	@Mapping(target = "date", source = "equipmentAppointmentBo.date")
	@Mapping(target = "hour", source = "equipmentAppointmentBo.hour")
	@Mapping(target = "overturn", source = "equipmentAppointmentBo.overturn")
	@Mapping(target = "appointmentStateId", source = "equipmentAppointmentBo.appointmentStateId")
	@Mapping(target = "patient", source = "patient")
	@Mapping(target = "isProtected", source = "equipmentAppointmentBo.protected")
	@Mapping(target = "reportStatusId", source = "equipmentAppointmentBo.reportStatusId")
	@Mapping(target = "studyName", expression = "java(!equipmentAppointmentBo.getStudies().isEmpty() ? equipmentAppointmentBo.getStudies().get(0) : null)")
	@Mapping(target = "studies", source = "equipmentAppointmentBo.studies")
	EquipmentAppointmentListDto toEquipmentAppointmentListDto(EquipmentAppointmentBo equipmentAppointmentBo, AppointmentBasicPatientDto patient);
  
    @Named("toAppointmentDto")
	@Mapping(target = "protected", source = "appointmentBo.protected")
	@Mapping(target = "orderData.serviceRequestId", source = "appointmentBo.orderData.encounterId")
	@Mapping(target = "transcribedOrderData", source = "appointmentBo.transcribedOrderData", qualifiedByName = "toTranscribedServiceRequestSummaryDto")
	@Mapping(target = "modality", source = "modalityId")
	@Mapping(target = "callLink", source = "callId", qualifiedByName = "generateCallLink")
	@Mapping(target = "diaryLabelDto", source = "appointmentBo.diaryLabelBo")
	@Mapping(target = "associatedReferenceClosureType", source = "associatedReferenceClosureTypeId")
	@Mapping(target = "recurringTypeDto", source = "appointmentBo.recurringTypeBo")
	@Mapping(target = "hasAppointmentChilds", source = "appointmentBo.hasAppointmentChilds")
	@Mapping(target = "parentAppointmentId", source = "appointmentBo.parentAppointmentId")
	@Mapping(target = "updatedOn", source = "appointmentBo.updatedOn")
	AppointmentDto toAppointmentDto(AppointmentBo appointmentBo);

	@Named("generateCallLink")
	default String generateCallLink(String callId) {
		if (callId != null)
			return HospitalSharedAutoConfiguration.JITSI_DOMAIN_URL + "/" + callId;
		else
			return null;
	}

    @Named("toAppointmentBo")
	@Mapping(target = "modalityId", source = "modality.id")
	@Mapping(target = "appointmentOptionId", source = "createAppointmentDto.appointmentOptionId")
    AppointmentBo toAppointmentBo(CreateAppointmentDto createAppointmentDto);

    @Named("toAppointmentDailyAmountDto")
    AppointmentDailyAmountDto toAppointmentDailyAmountDto(AppointmentDailyAmountBo appointmentDailyAmountBo);

	@Named("toAssignedAppointmentDto")
	@Mapping(target = "associatedReferenceClosureType", source = "associatedReferenceClosureTypeId")
	AssignedAppointmentDto toAssignedAppointmentDto(AppointmentAssignedBo appointmentAssignedBo);

	@Named("toBookingAppointmentDto")
	BookedAppointmentDto toBookingAppointmentDto(AppointmentBookingBo appointmentBookingBo);

	@IterableMapping(qualifiedByName = "toBookingAppointmentDto")
	@Named("toBookingAppointmentDtoList")
	List<BookedAppointmentDto> toBookingAppointmentDtoList(List<AppointmentBookingBo> appointmentBookingBos);

	@Named("toUpdateAppointmentBo")
	UpdateAppointmentBo toUpdateAppointmentBo(UpdateAppointmentDto updateAppointmentDto);

	@Named("toAppointmentShortSummaryDto")
	AppointmentShortSummaryDto toAppointmentShortSummaryDto(AppointmentShortSummaryBo appointmentShortSummaryBo);

	@Named("toAppointmentEquipmentShortSummaryDto")
	AppointmentEquipmentShortSummaryDto toAppointmentEquipmentShortSummaryDto(AppointmentEquipmentShortSummaryBo appointmentEquipmentShortSummaryBo);

	@Named("toAppointmentSearchBo")
	AppointmentSearchBo toAppointmentSearchBo(AppointmentSearchDto appointmentSearchDto);

	@Named("toEmptyAppointmentDto")
	EmptyAppointmentDto toEmptyAppointmentDto(EmptyAppointmentBo emptyAppointmentBo);

	@Named("toBlockBo")
	BlockBo toBlockBo(BlockDto appointmentSearchDto);

	@Named("toPatientAppointmentHistoryDto")
	@Mapping(source = "date", target = "dateTime.date")
	@Mapping(source = "time", target = "dateTime.time")
	PatientAppointmentHistoryDto toPatientAppointmentHistoryDto(PatientAppointmentHistoryBo patientAppointmentHistoryBo);

	@Named("fromFreeAppointmentSearchFilterDto")
	FreeAppointmentSearchFilterBo fromFreeAppointmentSearchFilterDto(FreeAppointmentSearchFilterDto freeAppointmentSearchFilterDto);

	@Named("fromUpdateAppointmentDateDto")
	@Mapping(target = "time", source = "date.time")
	@Mapping(target = "date", source = "date.date")
	UpdateAppointmentDateBo fromUpdateAppointmentDateDto(UpdateAppointmentDateDto updateAppointmentDateDto);

	@Named("toCustomRecurringAppointmentDto")
	CustomRecurringAppointmentDto toCustomRecurringAppointmentDto(CustomRecurringAppointmentBo customRecurringAppointmentBo);

	@Named("toGroupAppointmentDto")
	GroupAppointmentResponseDto toGroupAppointmentDto(GroupAppointmentResponseBo groupAppointmentResponseBo);
}
