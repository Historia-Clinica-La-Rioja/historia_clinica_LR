package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.requests.servicerequests.domain.InformerObservationBo;

import net.pladema.clinichistory.requests.servicerequests.domain.StudyAppointmentBo;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto.InformerObservationDto;

import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto.StudyAppointmentDto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface StudyAppointmentMapper {

	@Named("toInformerObservationBo")
	InformerObservationBo toInformerObservationBo(InformerObservationDto informerObservationDto);

	@Named("toInformerObservationDto")
	InformerObservationDto toInformerObservationDto(InformerObservationBo informerObservationBo);

	@Named("toStudyAppointmentDto")
	StudyAppointmentDto toStudyAppointmentDto(StudyAppointmentBo studyAppointmentBo);

	@Named("toStudyAppointmentBo")
	StudyAppointmentBo toStudyAppointmentBo(StudyAppointmentDto studyAppointmentDto);
}