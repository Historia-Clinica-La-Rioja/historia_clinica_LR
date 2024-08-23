package net.pladema.emergencycare.infrastructure.input.rest.mapper;

import net.pladema.emergencycare.controller.mapper.EmergencyCareClinicalSpecialtySectorMapper;
import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceBo;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareAttentionPlaceDto;

import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareBedDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareDoctorsOfficeDto;


import net.pladema.establishment.domain.bed.EmergencyCareBedBo;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {EmergencyCareClinicalSpecialtySectorMapper.class})
public interface EmergencyCareAttentionPlaceMapper {

	@Named("toDtoList")
	@IterableMapping(qualifiedByName = "toDto")
	List<EmergencyCareAttentionPlaceDto> toDtoList(List<EmergencyCareAttentionPlaceBo> emergencyCareAttentionPlaceBoList);

	@Named("toDto")
	EmergencyCareAttentionPlaceDto toDto(EmergencyCareAttentionPlaceBo emergencyCareAttentionPlaceBo);

	@Named("toDoctorsOfficeDtoList")
	@IterableMapping(qualifiedByName = "toDoctorsOfficeDto")
	List<EmergencyCareDoctorsOfficeDto> toDoctorsOfficeDtoList(List<DoctorsOfficeBo> doctorsOfficeBoList) ;

	@Named("toDoctorsOfficeDto")
	EmergencyCareDoctorsOfficeDto toDoctorsOfficeDto(DoctorsOfficeBo doctorsOfficeBo);

	@Named("toEmergencyCareBedDtoList")
	@IterableMapping(qualifiedByName = "toEmergencyCareBedDto")
	List<EmergencyCareBedDto> toEmergencyCareBedDtoList(List<EmergencyCareBedBo> emergencyCareBedBoList) ;

	@Named("toEmergencyCareBedDto")
	EmergencyCareBedDto toEmergencyCareBedDto(EmergencyCareBedBo emergencyCareBedBo);
}
