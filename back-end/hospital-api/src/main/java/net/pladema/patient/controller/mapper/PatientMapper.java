package net.pladema.patient.controller.mapper;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.dto.BMPatientDto;
import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.mapper.PersonMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {PersonMapper.class, LocalDateMapper.class})
public interface PatientMapper {

	PatientSearchDto fromPatientSearch(PatientSearch patientSeach);

	List<PatientSearchDto> fromListPatientSearch(List<PatientSearch> patientSeach);

	AddressDto updatePatientAddress(APatientDto patient);

	Patient fromPatientDto(APatientDto patientDto);
	
	BMPatientDto fromPatient(Patient patient);


}
