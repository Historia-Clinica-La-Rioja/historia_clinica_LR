package net.pladema.staff.application.saveprofessional;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.ProfessionalProfessionStorage;
import net.pladema.staff.application.saveprofessional.exceptions.CreateHealthcareProfessionalSpecialtyEnumException;
import net.pladema.staff.application.saveprofessional.exceptions.CreateHealthcareProfessionalSpecialtyException;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalCompleteBo;
import net.pladema.staff.service.domain.ProfessionalProfessionsBo;
import net.pladema.staff.service.exceptions.HealthcareProfessionalEnumException;
import net.pladema.staff.service.exceptions.HealthcareProfessionalException;
import net.pladema.user.application.port.HospitalUserStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveHealthcareProfessional {

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final ProfessionalProfessionStorage professionalProfessionStorage;

	private final HospitalUserStorage hospitalUserStorage;

	public Integer run(HealthcareProfessionalCompleteBo healthcareProfessionalCompleteBo) {
		log.debug("Input parameters -> healthcareProfessionalCompleteBo {}", healthcareProfessionalCompleteBo);
		if (healthcareProfessionalCompleteBo.getProfessionalProfessions().isEmpty())
			assertHasProfessionalRole(healthcareProfessionalCompleteBo.getPersonId());
		validateProfessionsAndSpecialties(healthcareProfessionalCompleteBo);
		List<ProfessionalProfessionsBo> newProfessions = healthcareProfessionalCompleteBo.getProfessionalProfessions();
		assertHasSpecialty(newProfessions);
		Integer healthcareProfessionalId = healthcareProfessionalService.getProfessionalIdByPersonId(healthcareProfessionalCompleteBo.getPersonId())
				.orElseGet(()->healthcareProfessionalService.saveProfessional(healthcareProfessionalCompleteBo));
		newProfessions.forEach(p-> p.setHealthcareProfessionalId(healthcareProfessionalId));
		List<ProfessionalProfessionsBo> oldProfessions = professionalProfessionStorage.getProfessionsByHealthcareProfessionalId(healthcareProfessionalId);
		// delete professions and specialties
		oldProfessions.forEach(oldProfession ->
				newProfessions.stream()
						.filter(p -> p.equalsProfession(oldProfession))
						.findFirst()
						.ifPresentOrElse(p -> oldProfession.getSpecialties()
								.forEach(specialty -> {
			if (p.getSpecialties().stream()
					.noneMatch(s -> s.equalsClinicalSpecialty(specialty)))
				professionalProfessionStorage.deleteHealthcareProfessionalSpecialty(specialty.getId());
		}), () -> professionalProfessionStorage.delete(oldProfession.getId())));
		newProfessions.forEach(professionalProfessionStorage::save);
		log.debug("Output -> {}", healthcareProfessionalId);
		return healthcareProfessionalId;
	}

	private void validateProfessionsAndSpecialties(HealthcareProfessionalCompleteBo bo){
		bo.getProfessionalProfessions().forEach(pp->{
			if(pp.getProfession().getId()==null)
				throw new CreateHealthcareProfessionalSpecialtyException(CreateHealthcareProfessionalSpecialtyEnumException.PROFESSION_SPECIALTY_ID_REQUIRED,"El id de la profesión es requerido");
			pp.getSpecialties().forEach(s->{
				if(s.getClinicalSpecialty().getId()==null)
					throw new CreateHealthcareProfessionalSpecialtyException(CreateHealthcareProfessionalSpecialtyEnumException.SPECIALTY_ID_REQUIRED,"El id de la especialidad es requerido");
			});
		});
	}
	private void assertHasProfessionalRole(Integer personId) {
		hospitalUserStorage.getUserDataByPersonId(personId).ifPresent(u -> {
			if (hospitalUserStorage.hasProfessionalRole(u.getId()))
				throw new HealthcareProfessionalException(HealthcareProfessionalEnumException.HEALTHCARE_PROFESSIONAL_HAS_ROLE, "El profesional que quiere eliminar tiene un rol asociado");
		});
	}


	private void assertHasSpecialty(List<ProfessionalProfessionsBo> professionalProfessionsBo) {
		professionalProfessionsBo.forEach(pp -> {
			if (pp.getSpecialties().isEmpty())
				throw new CreateHealthcareProfessionalSpecialtyException(CreateHealthcareProfessionalSpecialtyEnumException.PROFESSIONAL_SPECIALTY_REQUIRED, "Se requiere al menos una especialidad por profesion");
		});
	}

}
