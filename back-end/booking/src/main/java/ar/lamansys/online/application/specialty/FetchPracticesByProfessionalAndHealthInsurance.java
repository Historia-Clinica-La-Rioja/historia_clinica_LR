package ar.lamansys.online.application.specialty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import ar.lamansys.online.domain.specialty.PracticeBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
public class FetchPracticesByProfessionalAndHealthInsurance {

    private final BookingSpecialtyStorage practiceStorage;

    public FetchPracticesByProfessionalAndHealthInsurance(BookingSpecialtyStorage practiceStorage) {
        this.practiceStorage = practiceStorage;
    }

    public List<PracticeBo> run(
            Integer healthcareProfessionalId,
            Integer medicalCoverageId,
            Integer clinicalSpecialtyId,
            boolean all
    ) {
        log.debug("FetchPracticesByProfessionalAndHealthInsurance healthcareProfessionalId {} medicalCoverageId {}, clinicalSpecialtyID{}", healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId);
        var result = practiceStorage.findByProfessionalAndHealthInsurance(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all)
                .orElse(new ArrayList<>());
        log.trace("Practices result -> {}", result);
        return result;
    }
}
