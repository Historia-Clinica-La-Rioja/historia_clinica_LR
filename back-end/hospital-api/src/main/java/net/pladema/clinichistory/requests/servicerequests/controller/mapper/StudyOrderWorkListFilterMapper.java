package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderWorkListFilterDto;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListFilterBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface StudyOrderWorkListFilterMapper {

	@Named("fromStudyOrderWorkListFilterDto")
	StudyOrderWorkListFilterBo fromStudyOrderWorkListFilterDto(StudyOrderWorkListFilterDto dto);
}
