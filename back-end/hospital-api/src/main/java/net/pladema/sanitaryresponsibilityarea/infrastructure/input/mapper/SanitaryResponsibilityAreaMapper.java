package net.pladema.sanitaryresponsibilityarea.infrastructure.input.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.GlobalCoordinatesMapper;
import net.pladema.sanitaryresponsibilityarea.domain.GetPatientCoordinatesByOutpatientConsultationFilterBo;
import net.pladema.sanitaryresponsibilityarea.domain.SanitaryRegionPatientMapCoordinatesBo;
import net.pladema.sanitaryresponsibilityarea.domain.GetPatientCoordinatesByAddedInstitutionFilterBo;
import net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest.dto.GetPatientCoordinatesByOutpatientConsultationFilterDto;
import net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest.dto.SanitaryRegionPatientMapCoordinatesDto;
import net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest.dto.GetPatientCoordinatesByAddedInstitutionFilterDto;

import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {GlobalCoordinatesMapper.class, LocalDateMapper.class})
public interface SanitaryResponsibilityAreaMapper {

	@Mapping(target = "institutionId", expression = "java(institutionId)")
	@Named("fromGetPatientCoordinatesByAddedInstitutionFilterDto")
	GetPatientCoordinatesByAddedInstitutionFilterBo fromGetPatientCoordinatesByAddedInstitutionFilterDto(@Context Integer institutionId, GetPatientCoordinatesByAddedInstitutionFilterDto getPatientCoordinatesByAddedInstitutionFilterDto);

	@Named("toGetPatientCoordinatesByAddedInstitutionDto")
	SanitaryRegionPatientMapCoordinatesDto toGetPatientCoordinatesByAddedInstitutionDto(SanitaryRegionPatientMapCoordinatesBo sanitaryRegionPatientMapCoordinatesBo);

	@IterableMapping(qualifiedByName = "toGetPatientCoordinatesByAddedInstitutionDto")
	@Named("toGetPatientCoordinatesByAddedInstitutionDtoList")
	List<SanitaryRegionPatientMapCoordinatesDto> toGetPatientCoordinatesByAddedInstitutionDtoList(List<SanitaryRegionPatientMapCoordinatesBo> sanitaryRegionPatientMapCoordinatesBo);
	
	@Mapping(target = "institutionId", expression = "java(institutionId)")
	@Named("fromGetPatientCoordinatesByOutpatientConsultationFilterDto")
	GetPatientCoordinatesByOutpatientConsultationFilterBo fromGetPatientCoordinatesByOutpatientConsultationFilterDto(@Context Integer institutionId, GetPatientCoordinatesByOutpatientConsultationFilterDto getPatientCoordinatesByAddedInstitutionFilterDto);

}
