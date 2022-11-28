package net.pladema.staff.controller.mapper;

import java.util.List;
import java.util.stream.Collectors;

import net.pladema.staff.controller.dto.HealthcareProfessionalCompleteDto;

import net.pladema.staff.service.domain.HealthcareProfessionalCompleteBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.LicenseNumberDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import jdk.jfr.Name;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.domain.LicenseNumberBo;
import net.pladema.staff.domain.ProfessionBo;
import net.pladema.staff.domain.ProfessionSpecialtyBo;
import net.pladema.staff.domain.ProfessionalCompleteBo;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

@Mapper
public interface HealthcareProfessionalMapper {


	@Named("fromHealthcarePersonBo")
	@Mapping(target = "person", source = "person")
	HealthcareProfessionalDto fromHealthcarePersonBo(HealthcarePersonBo healthcarePerson);

	@Named("fromHealthcareProfessionalCompleteDto")
	HealthcareProfessionalCompleteBo fromHealthcareProfessionalCompleteDto(HealthcareProfessionalCompleteDto healthcareProfessionalCompleteDto);

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

	@Name("fromProfessionalCompleteBo")
	default ProfessionalCompleteDto fromProfessionalCompleteBo(ProfessionalCompleteBo professionalCompleteBo) {
		return new ProfessionalCompleteDto(professionalCompleteBo.getId(), professionalCompleteBo.getPersonId(), professionalCompleteBo.getFirstName(),
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
				mapLicenses(professionBo.getLicenses()),
				mapProfessionalSpecialties(professionBo.getSpecialties()));
	}

	private List<ProfessionSpecialtyDto> mapProfessionalSpecialties(List<ProfessionSpecialtyBo> specialties) {
		return specialties.stream().map(this::mapProfessionalSpecialty).collect(Collectors.toList());
	}

	private ProfessionSpecialtyDto mapProfessionalSpecialty(ProfessionSpecialtyBo professionSpecialtyBo){
		return new ProfessionSpecialtyDto(professionSpecialtyBo.getId(),
				mapClinicalSpecialty(professionSpecialtyBo.getSpecialty()),
				mapLicenses(professionSpecialtyBo.getLicenses()));
	}

	private ClinicalSpecialtyDto mapClinicalSpecialty(ClinicalSpecialtyBo specialty){
		return specialty != null ?
				new ClinicalSpecialtyDto(specialty.getId(), specialty.getName()) :
				null;
	}

	private List<LicenseNumberDto> mapLicenses(List<LicenseNumberBo> licenses) {
		return licenses.stream()
				.map(this::mapLicense)
				.collect(Collectors.toList());
	}

	private LicenseNumberDto mapLicense(LicenseNumberBo licenseNumberBo) {
		return licenseNumberBo != null ?
				new LicenseNumberDto(licenseNumberBo.getId(),
				licenseNumberBo.getNumber(),
				licenseNumberBo.getType().getAcronym()) : null;
	}
}
