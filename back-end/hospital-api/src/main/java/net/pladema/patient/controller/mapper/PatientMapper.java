package net.pladema.patient.controller.mapper;

import java.util.List;

import net.pladema.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.dto.BMPatientDto;
import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.repository.domain.BasicListedPatient;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.mapper.PersonMapper;

@Mapper(uses = {PersonMapper.class, LocalDateMapper.class})
public interface PatientMapper {

	public PatientSearchDto fromPatientSearch(PatientSearch patientSeach);

	public List<PatientSearchDto> fromListPatientSearch(List<PatientSearch> patientSeach);

	public AddressDto updatePatientAddress(APatientDto patient);

	public BMPatientDto fromPerson(BMPersonDto person);
	
	public Patient fromPatientDto(APatientDto patientDto);
	
	public BMPatientDto fromPatient(Patient patient);

    @Mapping(target = "id", source = "patientId")
    public BMPatientDto fromBasicListedPatient(BasicListedPatient patient);

    public List<BMPatientDto> fromBasicListedPatientList(List<BasicListedPatient> patientsList);
    
}
