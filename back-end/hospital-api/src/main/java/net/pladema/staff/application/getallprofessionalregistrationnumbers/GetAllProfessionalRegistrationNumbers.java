package net.pladema.staff.application.getallprofessionalregistrationnumbers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.ProfessionalLicenseNumberStorage;
import net.pladema.staff.domain.ProfessionalRegistrationNumbersBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetAllProfessionalRegistrationNumbers {

    private final ProfessionalLicenseNumberStorage professionalLicenseNumberStorage;

    public List<ProfessionalRegistrationNumbersBo> run(Integer institutionId) {
        log.debug("Get all professional registration license numbers without specialty licenses");
        List<ProfessionalRegistrationNumbersBo> result = professionalLicenseNumberStorage.getAllProfessionalRegistrationNumbersBo(institutionId);
        log.debug("Output -> {}", result);
        return result;
    }
}
