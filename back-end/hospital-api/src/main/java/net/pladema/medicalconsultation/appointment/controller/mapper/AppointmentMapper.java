package net.pladema.medicalconsultation.appointment.controller.mapper;

import net.pladema.medicalconsultation.appointment.controller.dto.*;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

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
    AppointmentListDto toAppointmentListDto(AppointmentBo appointmentBo, AppointmentBasicPatientDto patient);
  
    @Named("toAppointmentDto")
    AppointmentDto toAppointmentDto(AppointmentBo appointmentBo);

    @Named("toAppointmentBo")
    AppointmentBo toAppointmentBo(CreateAppointmentDto createAppointmentDto);

    @Named("toAppointmentDailyAmountDto")
    AppointmentDailyAmountDto toAppointmentDailyAmountDto(AppointmentDailyAmountBo appointmentDailyAmountBo);
}
