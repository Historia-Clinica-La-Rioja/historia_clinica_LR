package net.pladema.staff.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.staff.controller.dto.ProfessionalProfessionsDto;
import net.pladema.staff.service.domain.ProfessionalProfessionsBo;

@Mapper
public interface HealthcareProfessionalSpecialtyMapper {
	
	@Named("fromProfessionalProfessionsBoList")
	List<ProfessionalProfessionsDto> fromProfessionalProfessionsBoList(List<ProfessionalProfessionsBo> healthcareProfessionalSpecialtiesBo);

}
