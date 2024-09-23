package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderBasicPatientDto;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderBasicPatientBo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface StudyOrderPatientMapper {

	@Mapping(target = "id", source = "id")
	@Mapping(target = "person.firstName", source = "firstName")
	@Mapping(target = "person.lastName", source = "lastName")
	@Mapping(target = "person.identificationNumber", source = "identificationNumber")
	@Mapping(target = "person.identificationTypeId", source = "identificationTypeId")
	@Mapping(target = "person.genderId", source = "genderId")
	@Mapping(target = "person.birthDate", source = "birthDate")
	StudyOrderBasicPatientDto toPatientDto(StudyOrderBasicPatientBo patientBo);

}
