package net.pladema.staff.application.createprofessional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.createprofessional.exceptions.CreateHealthcareProfessionalSpecialtyEnumException;
import net.pladema.staff.application.createprofessional.exceptions.CreateHealthcareProfessionalSpecialtyException;
import net.pladema.staff.controller.dto.HealthcareProfessionalCompleteDto;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.HealthcareProfessionalSpecialtyService;
import net.pladema.staff.service.domain.HealthcareProfessionalCompleteBo;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateHealthcareProfessionalImpl implements CreateHealthcareProfessional {

    private final HealthcareProfessionalService healthcareProfessionalService;

    private final HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService;

    @Override
    public Integer execute(HealthcareProfessionalCompleteDto professionalDto) {
        log.debug("Input parameters -> {}", professionalDto);
        if (professionalDto.getProfessionalSpecialtyDtos().isEmpty())
            throw new CreateHealthcareProfessionalSpecialtyException(CreateHealthcareProfessionalSpecialtyEnumException.PROFESSIONAL_SPECIALTY_REQUIRED,
                    "El profesional requiere al menos una especialidad");
        Integer professionalId = healthcareProfessionalService.saveProfessional(mapProfessionalToBo(professionalDto));
        professionalDto.getProfessionalSpecialtyDtos()
                .forEach(healthcareProfessionalSpecialtyDto ->
                        healthcareProfessionalSpecialtyService.saveProfessionalSpeciality(
                                new HealthcareProfessionalSpecialtyBo(
                                        professionalId,
                                        healthcareProfessionalSpecialtyDto.getProfessionalSpecialtyId(),
                                        healthcareProfessionalSpecialtyDto.getClinicalSpecialtyId()
                                )
                        )
                );

        log.debug("Output -> {}", professionalId);
        return professionalId;
    }

    private HealthcareProfessionalCompleteBo mapProfessionalToBo(HealthcareProfessionalCompleteDto professionalDto) {
        return new HealthcareProfessionalCompleteBo(
                professionalDto.getId(),
                professionalDto.getPersonId(),
                professionalDto.getLicenseNumber());
    }
}
