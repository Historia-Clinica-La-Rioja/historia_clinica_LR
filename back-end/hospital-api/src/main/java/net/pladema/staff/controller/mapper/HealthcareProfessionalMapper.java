package net.pladema.staff.controller.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.LicenseNumberDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import jdk.jfr.Name;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.domain.LicenseNumberBo;
import net.pladema.staff.domain.ProfessionBo;
import net.pladema.staff.domain.ProfessionalCompleteBo;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

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


	@Name("fromListProfessionalCompleteBo")
	default List<ProfessionalCompleteDto> fromListProfessionalCompleteBo(List<ProfessionalCompleteBo> professionalCompleteBos) {
		return professionalCompleteBos.stream()
				.map(this::fromProfessionalCompleteBo)
				.collect(Collectors.toList());
	}
	@Name("fromProfessionalCompleteBo")
	default ProfessionalCompleteDto fromProfessionalCompleteBo(ProfessionalCompleteBo professionalCompleteBo) {
		return new ProfessionalCompleteDto(professionalCompleteBo.getPersonId(), professionalCompleteBo.getFirstName(),
				professionalCompleteBo.getLastName(), professionalCompleteBo.getNameSelfDetermination(),
				mapProfessions(professionalCompleteBo.getProfessions()));
	}

	private List<ProfessionCompleteDto> mapProfessions(List<ProfessionBo> professions) {
		return professions.stream()
				.map(this::mapProfession)
				.collect(Collectors.toList());
	}

	private ProfessionCompleteDto mapProfession(ProfessionBo professionBo) {
		return new ProfessionCompleteDto(professionBo.getId(),
				professionBo.getProfessionId(),
				professionBo.getDescription(),
				mapLicenses(professionBo.getLicenses()));
	}

	private List<LicenseNumberDto> mapLicenses(List<LicenseNumberBo> licenses) {
		return licenses.stream()
				.map(this::mapLicense)
				.collect(Collectors.toList());
	}

	private LicenseNumberDto mapLicense(LicenseNumberBo licenseNumberBo) {
		return new LicenseNumberDto(licenseNumberBo.getId(),
				licenseNumberBo.getNumber(),
				licenseNumberBo.getType().getAcronym());
	}
}
