package net.pladema.patient.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.PersonAgeDto;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.dto.MergedPatientSearchDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.PatientGenderAgeDto;
import net.pladema.patient.controller.dto.PatientRegistrationSearchDto;
import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.MergedPatientSearch;
import net.pladema.patient.service.domain.PatientGenderAgeBo;
import net.pladema.patient.service.domain.PatientRegistrationSearch;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.mapper.GenderMapper;
import net.pladema.person.controller.mapper.PersonMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {PersonMapper.class, LocalDateMapper.class, GenderMapper.class})
public interface PatientMapper {

	PatientSearchDto fromPatientSearch(PatientSearch patientSearch);

	List<PatientSearchDto> fromListPatientSearch(List<PatientSearch> patientSearch);

	@AfterMapping
	default void personAgeMapping(@MappingTarget PatientSearchDto target, PatientSearch source){
		if (source.getPerson() != null && source.getPerson().getBirthDate() != null)
			target.getPerson().setPersonAge(new PersonAgeDto(source.getPerson().getBirthDate()));
	}

	@Named("toPatientRegistrationSearchDto")
	@Mapping(target = "idPatient", source = "patientId")
	PatientRegistrationSearchDto toPatientRegistrationSearchDto(PatientRegistrationSearch patientRegistrationSearch);

	@Named("toMergedPatientSearchDto")
	@Mapping(target = "idPatient", source = "patientId")
	MergedPatientSearchDto toMergedPatientSearchDto(MergedPatientSearch mergedPatientSearchFilter);

	AddressDto updatePatientAddress(APatientDto patient);

	@Mapping(target = "auditTypeId", source = "auditType.id")
	Patient fromPatientDto(APatientDto patientDto);

	@Named("toPatientGenderAgeDto")
	@Mapping(target = "gender.description", source = "gender.value")
	PatientGenderAgeDto toPatientGenderAgeDto(PatientGenderAgeBo patientGenderAgeBo);

	@AfterMapping
	default void personAgeMapping(@MappingTarget PatientGenderAgeDto target, PatientGenderAgeBo source){
		if (source.getBirthDate() != null)
			target.setAge(new PersonAgeDto(source.getBirthDate()));
	}

	@Named("toPatientSearchDtoList")
	List<PatientSearchDto> toPatientSearchDtoList(List<PatientSearch> patientSearches);

}
