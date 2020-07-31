package net.pladema.staff.controller.mapper;

import java.util.List;

import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import org.mapstruct.Named;

@Mapper(uses = {PersonMapper.class})
public interface HealthcareProfessionalMapper {

	@Mapping(target = "person", source = "person", qualifiedByName="toBasicDataPersonDto")
	HealthcareProfessionalDto fromHealthcarePerson(HealthcarePersonBo healthcarePerson);
	
	List<HealthcareProfessionalDto> fromHealthcarePersonList(List<HealthcarePersonBo> healthcarePersonList);

	@Named("fromProfessionalBo")
	ProfessionalDto fromProfessionalBo(HealthcareProfessionalBo healthcareProfessionalBo);

	@Named("fromProfessionalBoList")
	@IterableMapping(qualifiedByName = "fromProfessionalBo")
	List<ProfessionalDto> fromProfessionalBoList(List<HealthcareProfessionalBo> healthcareProfessionalBos);
}
