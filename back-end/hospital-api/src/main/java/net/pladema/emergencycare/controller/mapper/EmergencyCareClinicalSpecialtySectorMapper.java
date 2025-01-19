package net.pladema.emergencycare.controller.mapper;

import net.pladema.emergencycare.controller.dto.EmergencyCareClinicalSpecialtySectorDto;

import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface EmergencyCareClinicalSpecialtySectorMapper {

	@Named("toDtoList")
	@IterableMapping(qualifiedByName = "toDto")
	List<EmergencyCareClinicalSpecialtySectorDto> toDtoList(List<ClinicalSpecialtySectorBo> clinicalSpecialtySectorBoList);

	@Named("toDto")
	EmergencyCareClinicalSpecialtySectorDto toDto(ClinicalSpecialtySectorBo clinicalSpecialtySectorBo);
}
