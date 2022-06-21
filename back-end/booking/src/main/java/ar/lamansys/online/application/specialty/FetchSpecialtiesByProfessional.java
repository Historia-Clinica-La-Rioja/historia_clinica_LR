package ar.lamansys.online.application.specialty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FetchSpecialtiesByProfessional {

    private final BookingSpecialtyStorage practiceStorage;

    public FetchSpecialtiesByProfessional(BookingSpecialtyStorage practiceStorage) {
        this.practiceStorage = practiceStorage;
    }

    public List<BookingSpecialtyBo> run(Integer healthcareProfessionalId) {
        var result = practiceStorage.findAllSpecialtiesByProfessional(healthcareProfessionalId).orElse(new ArrayList<>());
        log.debug("Find all specialties => {}", result);
        return result;
    }
}
