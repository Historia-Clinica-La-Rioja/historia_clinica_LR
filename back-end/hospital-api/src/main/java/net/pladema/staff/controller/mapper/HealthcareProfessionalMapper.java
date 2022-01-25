package net.pladema.staff.controller.mapper;

import jdk.jfr.Name;
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


	@Named("fromHealthcarePersonBo")
	@Mapping(target = "person", source = "person")
	HealthcareProfessionalDto fromHealthcarePersonBo(HealthcarePersonBo healthcarePerson);

	@Named("fromHealthcarePersonList")
	@IterableMapping(qualifiedByName = "fromHealthcarePersonBo")
	List<HealthcareProfessionalDto> fromHealthcarePersonList(List<HealthcarePersonBo> healthcarePersonList);

	@Named("toProfessionalDto")
	ProfessionalDto fromProfessionalBo(HealthcareProfessionalBo healthcareProfessionalBo);

	@Named("fromProfessionalBoList")
	@IterableMapping(qualifiedByName = "toProfessionalDto")
	List<ProfessionalDto> fromProfessionalBoList(List<HealthcareProfessionalBo> healthcareProfessionalBos);

	@Name("toHealthcareProfessionalDto")
	@Mapping(target = "person.firstName", source = "firstName")
	@Mapping(target = "person.lastName", source = "lastName")
	HealthcareProfessionalDto fromHealthcareProfessionalBo(HealthcareProfessionalBo healthcareProfessionalBo);
}
