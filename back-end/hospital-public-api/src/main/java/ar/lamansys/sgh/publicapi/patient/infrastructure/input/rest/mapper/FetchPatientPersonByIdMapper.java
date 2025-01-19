package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.publicapi.patient.domain.PersonBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.person.PersonDto;

public class FetchPatientPersonByIdMapper {

	public static PersonDto fromBo(PersonBo personBo){
		return PersonDto.builder()
				.firstName(personBo.getFirstName())
				.middleNames(personBo.getMiddleNames())
				.lastName(personBo.getLastName())
				.otherLastNames(personBo.getOtherLastNames())
				.identificationTypeId(personBo.getIdentificationTypeId())
				.identificationTypeDescription(personBo.getIdentificationTypeDescription())
				.identificationNumber(personBo.getIdentificationNumber())
				.genderId(personBo.getGenderId())
				.birthDate(personBo.getBirthDate())
				.genderDescription(personBo.getGenderDescription())
				.cuil(personBo.getCuil())
				.selfDeterminationName(personBo.getSelfDeterminationName())
				.selfDeterminationGender(personBo.getSelfDeterminationGender())
				.selfDeterminationGenderDescription(personBo.getSelfDeterminationGenderDescription())
				.build();
	}
}
