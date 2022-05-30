package ar.lamansys.online.application.professional;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.online.domain.professional.BookingProfessionalBo;
import ar.lamansys.online.domain.professional.ProfessionalAvailabilityBo;

@Service
public class FetchAvailabilityByPracticeAndProfessional {
    private final Logger logger;

    private final ProfessionalAvailabilityStorage professionalAvailabilityStorage;

    public FetchAvailabilityByPracticeAndProfessional(
            ProfessionalAvailabilityStorage professionalAvailabilityStorage
    ) {
        this.professionalAvailabilityStorage = professionalAvailabilityStorage;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public ProfessionalAvailabilityBo run(
            Integer institutionId,
            Integer professionalId,
            Integer clinicalSpecialtyId,
            Integer practiceId
    ) {
        logger.debug("Fetch availability");
        var result = professionalAvailabilityStorage.findAvailabilityByPracticeAndProfessional(
                institutionId, professionalId, clinicalSpecialtyId, practiceId)
                .orElse(new ProfessionalAvailabilityBo(new ArrayList<>(), new BookingProfessionalBo(professionalId, "empty", true)));
        logger.trace("Availability result -> {}", result);
        return result;
    }
}
