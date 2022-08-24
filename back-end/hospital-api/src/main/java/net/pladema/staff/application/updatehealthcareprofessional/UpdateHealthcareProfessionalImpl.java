package net.pladema.staff.application.updatehealthcareprofessional;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.updatehealthcareprofessional.exceptions.UpdateHealthcareProfessionalEnumException;
import net.pladema.staff.application.updatehealthcareprofessional.exceptions.UpdateHealthcareProfessionalException;
import net.pladema.staff.controller.dto.HealthcareProfessionalCompleteDto;
import net.pladema.staff.controller.dto.HealthcareProfessionalSpecialtyDto;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.HealthcareProfessionalSpecialtyService;
import net.pladema.staff.service.domain.HealthcareProfessionalCompleteBo;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateHealthcareProfessionalImpl implements UpdateHealthcareProfessional {

    private final HealthcareProfessionalService healthcareProfessionalService;

    private final HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService;


    public void execute(HealthcareProfessionalCompleteDto professionalCompleteDto) {
        log.debug("Input parameters -> {}", professionalCompleteDto);
        if (professionalCompleteDto.getProfessionalSpecialtyDtos().isEmpty())
            throw new UpdateHealthcareProfessionalException(UpdateHealthcareProfessionalEnumException.PROFESSIONAL_SPECIALTY_REQUIRED,
                    "El profesional requiere al menos una especialidad");
        healthcareProfessionalService.saveProfessional(mapProfessionalToBo(professionalCompleteDto));
        List<HealthcareProfessionalSpecialtyBo> professions = healthcareProfessionalSpecialtyService.
                getProfessionsByProfessional(professionalCompleteDto.getId());
        professions.forEach(profession -> healthcareProfessionalSpecialtyService.delete(profession.getId()));
        professionalCompleteDto.getProfessionalSpecialtyDtos()
                .forEach(healthcareProfessionalSpecialtyDto -> {
                    healthcareProfessionalSpecialtyService.saveProfessionalSpeciality(mapProfessionalSpecialtyToBo(healthcareProfessionalSpecialtyDto));
                });
    }

    private HealthcareProfessionalCompleteBo mapProfessionalToBo(HealthcareProfessionalCompleteDto professionalDto) {
        return new HealthcareProfessionalCompleteBo(
                professionalDto.getId(),
                professionalDto.getPersonId(),
                professionalDto.getLicenseNumber());
    }

    private HealthcareProfessionalSpecialtyBo mapProfessionalSpecialtyToBo(HealthcareProfessionalSpecialtyDto dto) {
        return new HealthcareProfessionalSpecialtyBo(
                dto.getId(),
                dto.getHealthcareProfessionalId(),
                dto.getProfessionalSpecialtyId(),
                dto.getClinicalSpecialtyId()
        );
    }
}
