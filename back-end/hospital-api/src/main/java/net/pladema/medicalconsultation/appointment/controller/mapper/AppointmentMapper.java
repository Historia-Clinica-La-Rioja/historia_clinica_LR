package net.pladema.medicalconsultation.appointment.controller.mapper;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentEquipmentShortSummaryDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentShortSummaryDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AssignedAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.EmptyAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.EquipmentAppointmentListDto;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentEquipmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.service.domain.EmptyAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentSearchDto;

import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import net.pladema.medicalconsultation.diary.controller.dto.BlockDto;
import net.pladema.medicalconsultation.diary.service.domain.BlockBo;

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

@Mapper(uses = {LocalDateMapper.class})
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
	@Mapping(target = "studyName", source = "equipmentAppointmentBo.studyName")
	EquipmentAppointmentListDto toEquipmentAppointmentListDto(EquipmentAppointmentBo equipmentAppointmentBo, AppointmentBasicPatientDto patient);
  
    @Named("toAppointmentDto")
	@Mapping(target = "protected", source = "appointmentBo.protected")
	AppointmentDto toAppointmentDto(AppointmentBo appointmentBo);

    @Named("toAppointmentBo")
    AppointmentBo toAppointmentBo(CreateAppointmentDto createAppointmentDto);

    @Named("toAppointmentDailyAmountDto")
    AppointmentDailyAmountDto toAppointmentDailyAmountDto(AppointmentDailyAmountBo appointmentDailyAmountBo);

	@Named("toAssignedAppointmentDto")
	AssignedAppointmentDto toAssignedAppointmentDto(AppointmentAssignedBo appointmentAssignedBo);

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
}
