package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderBasicPatientDto;

import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderBasicPatientBo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {LocalDateMapper.class, GenderDto.class})
public interface StudyOrderWorkListPatientMapper {

	@Mapping(target = "id", source = "id")
	@Mapping(target = "firstName", source = "firstName")
	@Mapping(target = "middleNames", source = "middleNames")
	@Mapping(target = "lastName", source = "lastName")
	@Mapping(target = "otherLastNames", source = "otherLastNames")
	@Mapping(target = "identificationNumber", source = "identificationNumber")
	@Mapping(target = "identificationTypeId", source = "identificationTypeId")
	@Mapping(target = "gender", source = ".")
	@Mapping(target = "birthDate", source = "birthDate")
	@Mapping(target = "patientTypeId", source = "patientTypeId")
	StudyOrderBasicPatientDto toPatientDto(StudyOrderBasicPatientBo patientBo);

	@Mapping(target = "gender", source = "patientBo")
	default GenderDto mapGender(StudyOrderBasicPatientBo patientBo) {
		if (patientBo.getGenderId() == null && patientBo.getGenderDescription() == null) {
			return null;
		}
		GenderDto genderDto = new GenderDto();
		genderDto.setId(patientBo.getGenderId());
		genderDto.setDescription(patientBo.getGenderDescription());
		return genderDto;
	}
}
