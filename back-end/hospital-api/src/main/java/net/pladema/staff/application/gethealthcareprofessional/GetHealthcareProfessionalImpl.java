package net.pladema.staff.application.gethealthcareprofessional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetHealthcareProfessionalImpl implements GetHealthcareProfessional {

    private final HealthcareProfessionalService healthcareProfessionalService;

    public HealthcareProfessionalBo execute(Integer personId) {
        log.debug("Input parameters -> {}", personId);
        HealthcareProfessionalBo result = healthcareProfessionalService.findProfessionalByPersonId(personId);
        log.debug("Output -> {}", result);
        return result;
    }
}
