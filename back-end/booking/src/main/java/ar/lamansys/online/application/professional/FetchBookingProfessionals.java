package ar.lamansys.online.application.professional;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.online.domain.professional.BookingProfessionalBo;

@Service
public class FetchBookingProfessionals {
    private final Logger logger;

    private final BookingProfessionalStorage bookingProfessionalStorage;

    public FetchBookingProfessionals(BookingProfessionalStorage bookingProfessionalStorage) {
        this.bookingProfessionalStorage = bookingProfessionalStorage;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public List<BookingProfessionalBo> run(
            Integer institutionId,
            Integer medicalCoverageId,
            boolean all
    ) {
        logger.debug("Fetch professionals");
        var result = bookingProfessionalStorage
                .findBookingProfessionals(institutionId, medicalCoverageId, all)
                .orElse(new ArrayList<>());
        logger.trace("Professionals result -> {}", result);
        return result;
    }
}
