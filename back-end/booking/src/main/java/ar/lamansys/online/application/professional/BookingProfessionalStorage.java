package ar.lamansys.online.application.professional;

import ar.lamansys.online.domain.professional.BookingProfessionalBo;

import java.util.List;
import java.util.Optional;

public interface BookingProfessionalStorage {
    Optional<List<BookingProfessionalBo>> findBookingProfessionals(
            Integer institutionId,
            Integer medicalCoverageId,
            boolean all
    );
}
