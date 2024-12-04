package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderBasicPatientDto;

import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderBasicPatientBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface StudyOrderWorkListPatientMapper {

	@Named("toStudyOrderBasicPatientDto")
	StudyOrderBasicPatientDto toStudyOrderBasicPatientDto(StudyOrderBasicPatientBo studyOrderBasicPatientBo);
}
