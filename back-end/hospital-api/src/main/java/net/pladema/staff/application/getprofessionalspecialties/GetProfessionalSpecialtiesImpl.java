package net.pladema.staff.application.getprofessionalspecialties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.service.ProfessionalSpecialtyService;
import net.pladema.staff.service.domain.ProfessionalSpecialtyBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetProfessionalSpecialtiesImpl implements GetProfessionalSpecialties {

    private final ProfessionalSpecialtyService professionalSpecialtyService;

    @Override
    public List<ProfessionalSpecialtyBo> execute(){
        log.debug("No input parameters");
        List<ProfessionalSpecialtyBo> result = professionalSpecialtyService.getAll();
        log.debug("Output -> {}",result);
        return result;
    }
}
