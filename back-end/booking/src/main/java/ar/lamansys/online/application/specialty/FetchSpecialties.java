package ar.lamansys.online.application.specialty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;

@Service
public class FetchSpecialties {

    private final Logger logger;
    private final BookingSpecialtyStorage practiceStorage;

    public FetchSpecialties(BookingSpecialtyStorage practiceStorage) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.practiceStorage = practiceStorage;
    }

    public List<BookingSpecialtyBo> run() {
        var result = practiceStorage.findAllSpecialties();
        logger.debug("Find all specialties => {}", result);
        return result;
    }
}
