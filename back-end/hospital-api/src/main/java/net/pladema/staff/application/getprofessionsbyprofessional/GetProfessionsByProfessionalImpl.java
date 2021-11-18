package net.pladema.staff.application.getprofessionsbyprofessional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.service.HealthcareProfessionalSpecialtyService;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetProfessionsByProfessionalImpl implements GetProfessionsByProfessional {

    private final HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService;

    public List<HealthcareProfessionalSpecialtyBo> execute(Integer professionalId) {
        log.debug("Input parameters -> {}", professionalId);
        List<HealthcareProfessionalSpecialtyBo> result = healthcareProfessionalSpecialtyService.getProfessionsByProfessional(professionalId);
        log.debug("Output -> {}", result);
        return result;
    }
}
