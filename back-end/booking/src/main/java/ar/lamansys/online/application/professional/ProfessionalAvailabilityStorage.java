package ar.lamansys.online.application.professional;

import java.util.List;
import java.util.Optional;

import ar.lamansys.online.domain.professional.ProfessionalAvailabilityBo;

public interface ProfessionalAvailabilityStorage {

    Optional<ProfessionalAvailabilityBo> findAvailabilityByPracticeAndProfessional(
            Integer institutionId,
            Integer professionalId,
            Integer clinicalSpecialtyId,
            Integer practiceId
    );

    Optional<List<ProfessionalAvailabilityBo>> findAvailabilityByPractice(
            Integer institutionId,
            Integer clinicalSpecialtyId,
            Integer practiceId,
            Integer medicalCoverageId
    );

}
