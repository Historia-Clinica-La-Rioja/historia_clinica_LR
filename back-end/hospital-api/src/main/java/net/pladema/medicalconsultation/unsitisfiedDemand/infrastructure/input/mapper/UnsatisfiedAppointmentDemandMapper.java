package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.input.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.medicalconsultation.unsitisfiedDemand.domain.UnsatisfiedAppointmentDemandBo;

import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.input.dto.UnsatisfiedAppointmentDemandDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = LocalDateMapper.class)
public interface UnsatisfiedAppointmentDemandMapper {

	@Mapping(source = "modality.id", target = "modalityId")
	@Named("fromUnsatisfiedDemandDto")
	UnsatisfiedAppointmentDemandBo fromUnsatisfiedDemandDto(UnsatisfiedAppointmentDemandDto unsatisfiedAppointmentDemandDto);

}
