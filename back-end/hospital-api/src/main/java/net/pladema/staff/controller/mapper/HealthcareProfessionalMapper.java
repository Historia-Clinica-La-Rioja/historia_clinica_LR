package net.pladema.staff.controller.mapper;

import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface HealthcareProfessionalMapper {


	@Named("fromProfessionalBo")
	@Mapping(target = "person", source = "person", qualifiedByName="toBasicDataPersonDto")
	HealthcareProfessionalDto fromHealthcarePerson(HealthcarePersonBo healthcarePerson);

	@Named("fromHealthcarePersonList")
	@IterableMapping(qualifiedByName = "fromProfessionalBo")
	List<HealthcareProfessionalDto> fromHealthcarePersonList(List<HealthcarePersonBo> healthcarePersonList);

	@Named("fromProfessionalBo")
	ProfessionalDto fromProfessionalBo(HealthcareProfessionalBo healthcareProfessionalBo);

	@Named("fromProfessionalBoList")
	@IterableMapping(qualifiedByName = "fromProfessionalBo")
	List<ProfessionalDto> fromProfessionalBoList(List<HealthcareProfessionalBo> healthcareProfessionalBos);
}
