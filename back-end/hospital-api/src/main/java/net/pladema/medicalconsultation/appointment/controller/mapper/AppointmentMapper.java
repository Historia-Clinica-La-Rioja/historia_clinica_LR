package net.pladema.medicalconsultation.appointment.controller.mapper;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentBasicPatientDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.patient.controller.dto.AppointmentPatientDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface AppointmentMapper {

    @Named("toAppointmentBasicPatientDto")
    AppointmentBasicPatientDto toAppointmentBasicPatientDto(AppointmentPatientDto patient);

    @Named("toAppointmentListDto")
    @Mapping(target = "id", source = "appointmentBo.id")
    @Mapping(target = "date", source = "appointmentBo.date")
    @Mapping(target = "hour", source = "appointmentBo.hour")
    @Mapping(target = "overturn", source = "appointmentBo.overturn")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "toAppointmentBasicPatientDto")
    AppointmentListDto toAppointmentListDto(AppointmentBo appointmentBo, AppointmentPatientDto patient);


    @Named("toAppointmentBo")
    AppointmentBo toAppointmentBo(CreateAppointmentDto createAppointmentDto);
}
