package net.pladema.medicalconsultation.appointment.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentBasicPatientDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.patient.controller.dto.HealthInsurancePatientDataDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;

@Mapper(uses = {LocalDateMapper.class})
public interface AppointmentMapper {

    @Named("toAppointmentBasicPatientDto")
    AppointmentBasicPatientDto toAppointmentBasicPatientDto(HealthInsurancePatientDataDto patient);

    @Named("toAppointmentListDto")
    @Mapping(target = "id", source = "appointmentBo.id")
    @Mapping(target = "date", source = "appointmentBo.date")
    @Mapping(target = "hour", source = "appointmentBo.hour")
    @Mapping(target = "overturn", source = "appointmentBo.overturn")
    @Mapping(target = "appointmentStateId", source = "appointmentBo.appointmentStateId")
    @Mapping(target = "medicalCoverageName", source = "appointmentBo.medicalCoverageName")
    @Mapping(target = "medicalCoverageAffiliateNumber", source = "appointmentBo.medicalCoverageAffiliateNumber")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "toAppointmentBasicPatientDto")
    AppointmentListDto toAppointmentListDto(AppointmentBo appointmentBo, HealthInsurancePatientDataDto patient);
  
    @Named("toAppointmentDto")
    AppointmentDto toAppointmentDto(AppointmentBo appointmentBo);

    @Named("toAppointmentBo")
    AppointmentBo toAppointmentBo(CreateAppointmentDto createAppointmentDto);
}
