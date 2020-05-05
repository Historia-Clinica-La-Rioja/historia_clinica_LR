package net.pladema.staff.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import net.pladema.staff.service.domain.HealthcarePerson;

@Mapper(uses = {PersonMapper.class})
public interface HealthcareProfessionalMapper {

	@Mapping(target = "person", source = "person", qualifiedByName="toBasicDataPersonDto")
	HealthcareProfessionalDto fromHealthcarePerson(HealthcarePerson healthcarePerson);
	
	List<HealthcareProfessionalDto> fromHealthcarePersonList(List<HealthcarePerson> healthcarePersonList);
	
}
