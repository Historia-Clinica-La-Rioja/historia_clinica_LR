package ar.lamansys.online.application.professional;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.online.domain.professional.ProfessionalAvailabilityBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FetchAvailabilityByPractice {

    private final ProfessionalAvailabilityStorage professionalAvailabilityStorage;

    public FetchAvailabilityByPractice(ProfessionalAvailabilityStorage professionalAvailabilityStorage){
        this.professionalAvailabilityStorage = professionalAvailabilityStorage;
    }

    public List<ProfessionalAvailabilityBo> run(
            Integer institutionId,
            Integer clinicalSpecialtyId,
            Integer practiceId,
            Integer medicalCoverageId
    ) {
        log.debug("Fetch availability");
        var result = professionalAvailabilityStorage.findAvailabilityByPractice(
                institutionId,
                clinicalSpecialtyId,
                practiceId,
				medicalCoverageId
        ).orElse(new ArrayList<>());
		log.trace("Availability result -> {}", result);
        return result;
    }
}
