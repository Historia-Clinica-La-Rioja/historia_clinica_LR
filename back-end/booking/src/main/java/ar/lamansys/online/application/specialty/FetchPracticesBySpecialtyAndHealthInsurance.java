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
public class FetchPracticesBySpecialtyAndHealthInsurance {

    private final BookingSpecialtyStorage practiceStorage;

    public FetchPracticesBySpecialtyAndHealthInsurance(BookingSpecialtyStorage practiceStorage) {
        this.practiceStorage = practiceStorage;
    }

    public List<PracticeBo> run(
            Integer clinicalSpecialtyId,
            Integer medicalCoverageId,
            boolean all
    ) {
        log.debug("FetchPracticesBySpecialtyAndHealthInsurance clinicalSpecialtyId {}, medicalCoverageId {}",
                clinicalSpecialtyId, medicalCoverageId);
        var result = practiceStorage.findBySpecialtyAndHealthInsurance(clinicalSpecialtyId, medicalCoverageId, all)
                .orElse(new ArrayList<>());
        log.trace("Practices result -> {}", result);
        return result;
    }
}
